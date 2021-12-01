package com.group19.orderprocessingservice.services;

import com.group19.orderprocessingservice.domain.model.Order;
import com.group19.orderprocessingservice.domain.model.StockProduct;
import com.group19.orderprocessingservice.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class OrderService {

    private final String key = "d52528c5-1068-4ea0-a5e7-d4e02046b001";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;


    public Order placeTransaction(Order order) {

        StockProduct stockProduct1 = restTemplate.getForObject("http://localhost:8086/market-data/fetch/exchange-one/"+order.getProduct().toUpperCase(Locale.ROOT), StockProduct.class);
        StockProduct stockProduct2 = restTemplate.getForObject("http://localhost:8086/market-data/fetch/exchange-two/"+order.getProduct().toUpperCase(Locale.ROOT), StockProduct.class);

        String orderToken;

        switch (order.getSide()) {
            case "BUY":
                if (Math.max(stockProduct1.getAskPrice(), stockProduct2.getAskPrice()) == stockProduct1.getAskPrice()) {
                    order.setPrice(stockProduct1.getAskPrice());
                    orderToken = restTemplate.postForObject("https://exchange.matraining.com/"+key+"/order", order, String.class);
                } else {
                    order.setPrice(stockProduct2.getAskPrice());
                    orderToken = restTemplate.postForObject("https://exchange2.matraining.com/"+key+"/order", order, String.class);
                }
                order.setCreatedAt(new Timestamp(new Date().getTime()));
                order.setId(orderToken);
                orderRepository.save(order);
                return order;
            case "SELL":
                if (Math.min(stockProduct1.getBidPrice(), stockProduct2.getBidPrice()) == stockProduct1.getBidPrice()) {
                    order.setPrice(stockProduct1.getBidPrice());
                    orderToken = restTemplate.postForObject("https://exchange.matraining.com/"+key+"/order", order, String.class);
                } else {
                    order.setPrice(stockProduct2.getBidPrice());
                    orderToken = restTemplate.postForObject("https://exchange2.matraining.com/"+key+"/order", order, String.class);
                }
                order.setCreatedAt(new Timestamp(new Date().getTime()));
                order.setId(orderToken);
                orderRepository.save(order);
                return order;
        }
        return null;
    }

    public List<Order> fetchAllOrders() {
        System.err.println(orderRepository.findAll());
        return orderRepository.findAll();
    }
}
