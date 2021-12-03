package com.group19.orderprocessingservice.controller;

import com.group19.orderprocessingservice.domain.model.order.Order;
import com.group19.orderprocessingservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-processing")
public class OrderController {

    @Autowired
    private OrderService orderService;
//    {
//        "product":"AAPL",
//            "quantity":9,
//            "side":"SELL",
//            "useId": 1,
//            "portfolio": 3
//    }
    @PostMapping("/order/place")
    public ResponseEntity<Order> placeOrderTransaction(@RequestBody Order order){
        return new ResponseEntity<>(orderService.placeTransaction(order), HttpStatus.OK);
    }

    @GetMapping("/order/all")
    public ResponseEntity<List<Order>> fetchAllOthers(){
        return new ResponseEntity<>(orderService.fetchAllOrders(), HttpStatus.OK);
    }
}
