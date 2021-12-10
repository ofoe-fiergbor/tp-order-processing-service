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
import com.group19.orderprocessingservice.services.firebase.MessagingService;
import com.group19.orderprocessingservice.util.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class OrderService {

    private final RestTemplate restTemplate;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final PortfolioRepository portfolioRepository;

    private final MessagingService messagingService;


    @Value("${system.exchange_one}")
    private String exchange_one_url;

    @Value("${system.exchange_two}")
    private String exchange_two_url;

    @Value("${system.api_key}")
    private String key;

    @Autowired
    public OrderService(RestTemplate restTemplate, OrderRepository orderRepository, UserRepository userRepository, PortfolioRepository portfolioRepository, MessagingService messagingService) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.messagingService = messagingService;
    }


    public ResponseDto placeTransaction(CreateOrderDto codto) throws ExecutionException, InterruptedException {

        User user = Utils.checkIfUserExists(codto.getUserId(), userRepository);



        Portfolio portfolio = Utils.checkIfPortfolioExists(codto.getPortfolioId(), portfolioRepository);

        StockProduct stockProduct1 = restTemplate.getForObject("http://localhost:8086/market-data/fetch/exchange-one/"+codto.getProduct().toUpperCase(Locale.ROOT), StockProduct.class);
        StockProduct stockProduct2 = restTemplate.getForObject("http://localhost:8086/market-data/fetch/exchange-two/"+codto.getProduct().toUpperCase(Locale.ROOT), StockProduct.class);

        double orderValue;
        String orderToken;



        switch (codto.getSide().toString().toUpperCase()) {
            //check for order side and place trade respectively
            case "BUY":
                //if buy ? check for the lowest possible price to buy at from both exchanges
                messagingService.saveMessage(new MessageDto("Trade:: Initiating a \"BUY\" trade execution for "+ codto.getQuantity()+ " shares of "+ codto.getProduct()));
//                log.info("Initiating a \"BUY\" trade execution for "+ codto.getQuantity()+ " shares of "+ codto.getProduct());
                assert stockProduct1 != null;
                assert stockProduct2 != null;

                //exchange 1
                if (Math.max(stockProduct1.getAskPrice(), stockProduct2.getAskPrice()) == stockProduct1.getAskPrice()) {
                    messagingService.saveMessage(new MessageDto("Trade:: Initiating a \"BUY\" trade execution on exchange one"));

                    codto.setPrice(stockProduct1.getAskPrice());

                    //checking if user has enough balance

                    orderValue = codto.getPrice() * codto.getQuantity();

                    if (user.getBalance() <= orderValue) {

//                        log.info("Insufficient account balance to execute the trade on exchange one");
                        messagingService.saveMessage(new MessageDto("Trade:: You have insufficient funds to execute trade for "+ codto.getQuantity()+ " shares of "+ codto.getProduct()));
                        return new ResponseDto(ResponseDTOStatus.FAILED, "You don't have enough balance to place this trade!");
                    }


                    orderToken = restTemplate.postForObject(exchange_one_url + key +"/order", codto, String.class);


                } else {
                    //exchange 2
                    messagingService.saveMessage(new MessageDto("Trade:: Initiating a \"BUY\" trade execution on exchange two"));

                    messagingService.saveMessage(new MessageDto("Trade:: Initiating a \"SELL\" trade execution for "+ codto.getQuantity()+ " shares of "+ codto.getProduct()));
                    codto.setPrice(stockProduct2.getAskPrice());


                    //checking if user has enough balance


                    orderValue = codto.getPrice() * codto.getQuantity();


                    if (user.getBalance() <= orderValue) {
                        messagingService.saveMessage(new MessageDto("Trade:: You have insufficient funds to execute trade for "+ codto.getQuantity()+ " shares of "+ codto.getProduct()));


                        return new ResponseDto(ResponseDTOStatus.FAILED, "You don't have enough balance to place this trade!");
                    }
                    orderToken = restTemplate.postForObject(exchange_two_url + key +"/order", codto, String.class);

                    //withdraw trade value from user's account
                }

                user.withdraw(orderValue);

                messagingService.saveMessage(new MessageDto("Trade:: Execution for "+ codto.getQuantity()+ " shares of "+ codto.getProduct() + " completed."));

                break;


            case "SELL":
                log.info("Initiating a \"SELL\" trade execution for" + codto.getQuantity()+ " shares of "+ codto.getProduct());
                assert stockProduct1 != null;
                assert stockProduct2 != null;
                //if sell ? check for the lowest possible price to buy at from both exchanges
                if (Math.min(stockProduct1.getBidPrice(), stockProduct2.getBidPrice()) == stockProduct1.getBidPrice()) {
                    messagingService.saveMessage(new MessageDto("Trade:: Initiating a \"SELL\" trade execution on exchange one"));

                    //exchange 1
                    codto.setPrice(stockProduct1.getBidPrice());
                    orderToken = restTemplate.postForObject(exchange_one_url + key +"/order", codto, String.class);
                } else {
                    //exchange 2
                    messagingService.saveMessage(new MessageDto("Trade:: Initiating a \"SELL\" trade execution on exchange two"));

                    codto.setPrice(stockProduct2.getBidPrice());
                    orderToken = restTemplate.postForObject(exchange_two_url+ key +"/order", codto, String.class);
                }
                break;
            default:
                throw new OrderSideException("Unexpected value: " + codto.getSide().toString().toUpperCase());
        }

        Order order = new Order(codto.getProduct(), codto.getQuantity(),
                codto.getSide(), codto.getPrice(), user, portfolio);
        order.setId(orderToken);
        orderRepository.save(order);
        messagingService.saveMessage(new MessageDto("Trade:: Transaction executed successfully."));
        return new ResponseDto(ResponseDTOStatus.SUCCESS, "Transaction executed successfully!", order);
    }

    public List<Order> fetchAllOrders(long userId) {
        Utils.checkIfUserExists(userId, userRepository);
        log.info("Fetching all orders with user id - {}", userId);
        return orderRepository.findAllOrdersById(userId);
    }


}
