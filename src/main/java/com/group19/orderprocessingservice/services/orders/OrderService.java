package com.group19.orderprocessingservice.services.orders;

import com.group19.orderprocessingservice.domain.dto.*;
import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.domain.model.order.Order;
import com.group19.orderprocessingservice.domain.model.order.Portfolio;
import com.group19.orderprocessingservice.domain.repository.auth.UserRepository;
import com.group19.orderprocessingservice.domain.repository.order.OrderRepository;
import com.group19.orderprocessingservice.domain.repository.order.PortfolioRepository;
import com.group19.orderprocessingservice.enums.ResponseDTOStatus;
import com.group19.orderprocessingservice.exceptions.OrderSideException;
import com.group19.orderprocessingservice.services.redis.MessagingService;
import com.group19.orderprocessingservice.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class OrderService {

    private final RestTemplate restTemplate;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final PortfolioRepository portfolioRepository;

    private final MessagingService messagingService;

    private final PortfolioService portfolioService;



    @Value("${system.exchange_one}")
    private String exchange_one_url;

    @Value("${system.exchange_two}")
    private String exchange_two_url;

    @Value("${system.api_key}")
    private String key;

    @Autowired
    public OrderService(RestTemplate restTemplate, OrderRepository orderRepository, UserRepository userRepository, PortfolioRepository portfolioRepository, MessagingService messagingService, PortfolioService portfolioService) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.messagingService = messagingService;
        this.portfolioService = portfolioService;
    }


    public ResponseDto placeTransaction(CreateOrderDto codto) throws ExecutionException, InterruptedException {

        User user = Utils.checkIfUserExists(codto.getUserId(), userRepository);



        Portfolio portfolio = Utils.checkIfPortfolioExists(codto.getPortfolioId(), portfolioRepository);

        StockProduct stockProduct1 = restTemplate.getForObject("http://localhost:8086/market-data/fetch/exchange-one/"+codto.getProduct().toUpperCase(Locale.ROOT), StockProduct.class);
        StockProduct stockProduct2 = restTemplate.getForObject("http://localhost:8086/market-data/fetch/exchange-two/"+codto.getProduct().toUpperCase(Locale.ROOT), StockProduct.class);

        double orderValue;
        String orderToken;
        Order order = null;



        switch (codto.getSide().toString().toUpperCase()) {
            //check for order side and place trade respectively
            case "BUY":
                //if buy ? check for the lowest possible price to buy at from both exchanges
                messagingService.saveMessage("Trade:: Initiating a \"BUY\" trade execution for "+ codto.getQuantity()+ " shares of "+ codto.getProduct());

                assert stockProduct1 != null;
                assert stockProduct2 != null;




                //exchange 1
                if (stockProduct1.getAskPrice() == Math.min(stockProduct1.getAskPrice(), stockProduct2.getAskPrice())) {

                    if (stockProduct1.getBuyLimit() > codto.getQuantity()) {
                        messagingService.saveMessage("Trade:: Initiating a \"BUY\" trade execution on exchange failed - Transaction above trade limit");
                        return new ResponseDto(ResponseDTOStatus.FAILED,"The number of shares you wish to purchase are is above the limit");
                    }

                    messagingService.saveMessage("Trade:: Initiating a \"BUY\" trade execution on exchange one");

                    if(stockProduct1.getBuyLimit() < codto.getQuantity()) {
                        return new ResponseDto(ResponseDTOStatus.FAILED,"The number of shares you wish to purchase are unavailable");
                    }

                    codto.setPrice(stockProduct1.getAskPrice());

                    //checking if user has enough balance

                    orderValue = codto.getPrice() * codto.getQuantity();

                    if (user.getBalance() <= orderValue) {

//                        log.info("Insufficient account balance to execute the trade on exchange one");
                        messagingService.saveMessage("Trade:: You have insufficient funds to execute trade for "+ codto.getQuantity()+ " shares of "+ codto.getProduct());
                        return new ResponseDto(ResponseDTOStatus.FAILED, "You don't have enough balance to place this trade!");
                    }


                    orderToken = restTemplate.postForObject(exchange_one_url + key +"/order", codto, String.class);


                } else {
                    //exchange 2
                    messagingService.saveMessage("Trade:: Initiating a \"BUY\" trade execution on exchange two");
                    if (stockProduct2.getBuyLimit() > codto.getQuantity()) {
                        messagingService.saveMessage("Trade:: Initiating a \"BUY\" trade execution on exchange failed - Transaction above trade limit");
                        return new ResponseDto(ResponseDTOStatus.FAILED,"The number of shares you wish to purchase are is above the limit");
                    }


                    messagingService.saveMessage("Trade:: Initiating a \"SELL\" trade execution for "+ codto.getQuantity()+ " shares of "+ codto.getProduct());

                    if(stockProduct2.getBuyLimit() < codto.getQuantity()) {
                        return new ResponseDto(ResponseDTOStatus.FAILED,"The number of shares you wish to purchase are unavailable");
                    }

                    codto.setPrice(stockProduct2.getAskPrice());


                    //checking if user has enough balance


                    orderValue = codto.getPrice() * codto.getQuantity();


                    if (user.getBalance() <= orderValue) {
                        messagingService.saveMessage("Trade:: User "+codto.getUserId()+" has insufficient funds to execute trade for "+ codto.getQuantity()+ " shares of "+ codto.getProduct());


                        return new ResponseDto(ResponseDTOStatus.FAILED, "You don't have enough balance to place this trade!");
                    }
                    orderToken = restTemplate.postForObject(exchange_two_url + key +"/order", codto, String.class);

                    //withdraw trade value from user's account
                }
                user.withdraw(orderValue);

                 order = new Order(codto.getProduct(), codto.getQuantity(), codto.getSide(), codto.getPrice(), user, portfolio);

                messagingService.saveMessage("Trade:: Execution for "+ codto.getQuantity()+ " shares of "+ codto.getProduct() + " completed.");
                break;


            case "SELL":
                assert stockProduct1 != null;
                assert stockProduct2 != null;

                double tradeValue;

                Map<String, Holding> holdings = portfolioService.getPortfolio(codto.getPortfolioId());

                if (!holdings.containsKey(codto.getProduct())) {
                    return new ResponseDto(ResponseDTOStatus.FAILED, "You don't have this product in your portfolio");
                }
                Holding holding = holdings.get(codto.getProduct());

                //if sell ? check for the lowest possible price to buy at from both exchanges
                if (stockProduct1.getBidPrice() == Math.max(stockProduct1.getBidPrice(), stockProduct2.getBidPrice())) {

                    messagingService.saveMessage("Trade:: Initiating a \"SELL\" trade execution on exchange one");

                    //exchange 1
                    codto.setPrice(stockProduct1.getBidPrice());

                    orderToken = restTemplate.postForObject(exchange_one_url + key +"/order", codto, String.class);



                } else {
                    //exchange 2
                    messagingService.saveMessage("Trade:: Initiating a \"SELL\" trade execution on exchange two");

                    codto.setPrice(stockProduct2.getBidPrice());

                    orderToken = restTemplate.postForObject(exchange_two_url+ key +"/order", codto, String.class);

                }
                tradeValue = codto.getQuantity() * codto.getPrice();

                holding.decreaseQuantity(codto.getQuantity());

                holding.decreaseValue(tradeValue);

                user.deposit(tradeValue);

                order = new Order(codto.getProduct(), codto.getQuantity() * -1, codto.getSide(), codto.getPrice(), user, portfolio);
                break;
            default:
                throw new OrderSideException("Unexpected value: " + codto.getSide().toString().toUpperCase());
        }


        order.setId(orderToken);
        orderRepository.save(order);
        messagingService.saveMessage("Trade:: Transaction executed successfully.");
        return new ResponseDto(ResponseDTOStatus.SUCCESS, "Transaction executed successfully!", order);
    }

    public List<Order> fetchAllOrders(long userId) {
        Utils.checkIfUserExists(userId, userRepository);
        return orderRepository.findAllOrdersById(userId);
    }


}
