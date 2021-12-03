package com.group19.orderprocessingservice.controller;


import com.group19.orderprocessingservice.domain.dto.CreatePortfolioDto;
import com.group19.orderprocessingservice.domain.dto.DeletePortfolioDto;
import com.group19.orderprocessingservice.domain.model.order.Portfolio;
import com.group19.orderprocessingservice.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-processing/portfolio")
public class PortfolioController {

    @Autowired
    PortfolioService portfolioService;



    @PostMapping("/add")
    public ResponseEntity<Portfolio> addNewPortfolio(@RequestBody CreatePortfolioDto pdto) {
        return new ResponseEntity<>(portfolioService.createNewPortfolio(pdto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Portfolio>> fetchAllPortfolio(@RequestBody CreatePortfolioDto pdto) {
        return new ResponseEntity<>((List<Portfolio>) portfolioService.fetchAll(pdto), HttpStatus.OK);
    }


    @PostMapping("/close")
    public ResponseEntity<Portfolio> removePortfolio(@RequestBody DeletePortfolioDto dpdto) {
        return new ResponseEntity<>(portfolioService.deletePortfolio(dpdto), HttpStatus.OK);
    }

//rev seth saah
    //
}
