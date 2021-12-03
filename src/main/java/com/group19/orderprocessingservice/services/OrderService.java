package com.group19.orderprocessingservice.services;

import com.group19.orderprocessingservice.domain.dto.CreateOrderDto;
import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.domain.model.order.Order;
import com.group19.orderprocessingservice.domain.model.order.Portfolio;
import com.group19.orderprocessingservice.domain.model.order.StockProduct;
import com.group19.orderprocessingservice.domain.repository.auth.UserRepository;
import com.group19.orderprocessingservice.domain.repository.order.OrderRepository;
import com.group19.orderprocessingservice.domain.repository.order.PortfolioRepository;
import com.group19.orderprocessingservice.exceptions.OrderSideException;
import com.group19.orderprocessingservice.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;

@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;


    public Order placeTransaction(CreateOrderDto codto) {

        User user = Utils.checkIfUserExists(codto.getUserId(), userRepository);
        Portfolio portfolio = Utils.checkIfPortfolioExists(codto.getPortfolioId(), portfolioRepository);

        StockProduct stockProduct1 = restTemplate.getForObject("http://localhost:8086/market-data/fetch/exchange-one/"+codto.getProduct().toUpperCase(Locale.ROOT), StockProduct.class);
        StockProduct stockProduct2 = restTemplate.getForObject("http://localhost:8086/market-data/fetch/exchange-two/"+codto.getProduct().toUpperCase(Locale.ROOT), StockProduct.class);

        String orderToken;

        final String key = "d52528c5-1068-4ea0-a5e7-d4e02046b001";
        final String exchange_one_url = "https://exchange.matraining.com/";
        final String exchange_two_url = "https://exchange2.matraining.com/";


        switch (codto.getSide().toString().toUpperCase()) {
            //check for order side and place trade respectively
            case "BUY":
                //if buy ? check for the lowest possible price to buy at from both exchanges
                assert stockProduct1 != null;
                assert stockProduct2 != null;
                //exchange 1
                if (Math.max(stockProduct1.getAskPrice(), stockProduct2.getAskPrice()) == stockProduct1.getAskPrice()) {
                    codto.setPrice(stockProduct1.getAskPrice());
                    orderToken = restTemplate.postForObject(exchange_one_url + key +"/order", codto, String.class);
                } else {
                    //exchange 2
                    codto.setPrice(stockProduct2.getAskPrice());
                    orderToken = restTemplate.postForObject(exchange_two_url + key +"/order", codto, String.class);
                }

                break;
            case "SELL":
                assert stockProduct1 != null;
                assert stockProduct2 != null;
                //if sell ? check for the lowest possible price to buy at from both exchanges
                if (Math.min(stockProduct1.getBidPrice(), stockProduct2.getBidPrice()) == stockProduct1.getBidPrice()) {
                    //exchange 1
                    codto.setPrice(stockProduct1.getBidPrice());
                    orderToken = restTemplate.postForObject(exchange_one_url + key +"/order", codto, String.class);
                } else {
                    //exchange 2
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
        return order;
    }

    public List<Order> fetchAllOrders(long userId) {
        Utils.checkIfUserExists(userId, userRepository);
        return orderRepository.findAllOrdersById(userId);
    }
}
