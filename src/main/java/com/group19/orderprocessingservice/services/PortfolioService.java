package com.group19.orderprocessingservice.services;

import com.group19.orderprocessingservice.domain.model.Portfolio;
import com.group19.orderprocessingservice.domain.repository.PortfolioRepository;
import com.group19.orderprocessingservice.enums.PortfolioStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {

    @Autowired
    PortfolioRepository portfolioRepository;

    public Portfolio createNewPortfolio(Portfolio pf) {
        Portfolio portfolio = new Portfolio(pf.getUserId());
        return portfolioRepository.save(portfolio);
    }

    public Iterable<Portfolio> fetchAll() {
        return portfolioRepository.findAll();
    }

    public Portfolio removePortfolio(Portfolio pf) {
        Portfolio portfolio = portfolioRepository.getById(pf.getId());
        portfolio.setPortfolioStatus(PortfolioStatus.DELETED);
        System.err.println(portfolio);
        return portfolioRepository.save(portfolio);

    }
}
