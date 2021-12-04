package com.group19.orderprocessingservice.controller;

import com.group19.orderprocessingservice.domain.dto.CreateOrderDto;
import com.group19.orderprocessingservice.domain.dto.FetchOrdersDto;
import com.group19.orderprocessingservice.domain.model.order.Order;
import com.group19.orderprocessingservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-processing/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrderTransaction(@RequestBody CreateOrderDto codto){
        return new ResponseEntity<>(orderService.placeTransaction(codto), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> fetchAllOthers(@RequestBody FetchOrdersDto fetchOrdersDto){
        return new ResponseEntity<>(orderService.fetchAllOrders(fetchOrdersDto.getUserId()), HttpStatus.OK);
    }
}
