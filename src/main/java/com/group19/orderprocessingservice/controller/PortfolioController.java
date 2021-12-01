package com.group19.orderprocessingservice.controller;


import com.group19.orderprocessingservice.domain.model.Portfolio;
import com.group19.orderprocessingservice.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-processing-service/portfolio")
public class PortfolioController {

    @Autowired
    PortfolioService portfolioService;


    @PostMapping("/add")
    public ResponseEntity<Portfolio> addNewPortfolio(@RequestBody Portfolio portfolio) {
        return new ResponseEntity<>(portfolioService.createNewPortfolio(portfolio), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Portfolio>> fetchAllPortfolio() {
        return new ResponseEntity<>((List<Portfolio>) portfolioService.fetchAll(), HttpStatus.OK);
    }

    @PostMapping("/close")
    public ResponseEntity<Portfolio> removePortfolio(@RequestBody Portfolio portfolio) {
        return new ResponseEntity<>(portfolioService.removePortfolio(portfolio), HttpStatus.OK);
    }


}
