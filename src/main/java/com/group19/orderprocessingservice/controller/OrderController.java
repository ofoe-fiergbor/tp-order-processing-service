package com.group19.orderprocessingservice.controller;

import com.group19.orderprocessingservice.domain.dto.CreateOrderDto;
import com.group19.orderprocessingservice.domain.dto.ResponseDto;
import com.group19.orderprocessingservice.domain.model.order.Order;
import com.group19.orderprocessingservice.services.orders.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@CrossOrigin("*")
@RestController
@RequestMapping("/order-processing/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    @Operation(summary = "Place an new buy or sell order.")
    public ResponseEntity<ResponseDto> placeOrderTransaction(@RequestBody CreateOrderDto codto) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(orderService.placeTransaction(codto), HttpStatus.OK);
    }

    @GetMapping("/all/{userId}")
    @Operation(summary = "Fetch all orders executed by a user.")
    public ResponseEntity<List<Order>> fetchAllOthers(@PathVariable long userId){
        return new ResponseEntity<>(orderService.fetchAllOrders(userId), HttpStatus.OK);
    }
}
