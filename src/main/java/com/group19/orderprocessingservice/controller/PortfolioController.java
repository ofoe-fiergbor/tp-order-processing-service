package com.group19.orderprocessingservice.controller;


import com.group19.orderprocessingservice.domain.dto.CreatePortfolioDto;
import com.group19.orderprocessingservice.domain.dto.DeletePortfolioDto;
import com.group19.orderprocessingservice.domain.model.order.Portfolio;
import com.group19.orderprocessingservice.services.orders.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@CrossOrigin("*")
@RestController
@RequestMapping("/order-processing/portfolio")
public class PortfolioController {

    private final
    PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }


    @PostMapping("/add")
    @Operation(summary = "Create a new portfolio.")
    public ResponseEntity<Portfolio> addNewPortfolio(@RequestBody CreatePortfolioDto pdto) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(portfolioService.createNewPortfolio(pdto), HttpStatus.CREATED);
    }

    @GetMapping("/all/{userId}")
    @Operation(summary = "Fetch all portfolios for a user.")
    public ResponseEntity<List<Portfolio>> fetchAllPortfolio(@PathVariable long userId) {
        return new ResponseEntity<>((List<Portfolio>) portfolioService.fetchAll(userId), HttpStatus.OK);
    }


    @PostMapping("/close")
    @Operation(summary = "Close/Delete a portfolios for a user.")
    public ResponseEntity<Portfolio> removePortfolio(@RequestBody DeletePortfolioDto dpdto) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(portfolioService.deletePortfolio(dpdto), HttpStatus.OK);
    }


}
