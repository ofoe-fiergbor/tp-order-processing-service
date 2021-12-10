package com.group19.orderprocessingservice.services;

import com.group19.orderprocessingservice.domain.dto.CreatePortfolioDto;
import com.group19.orderprocessingservice.domain.dto.DeletePortfolioDto;
import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.domain.model.order.Portfolio;
import com.group19.orderprocessingservice.domain.repository.auth.UserRepository;
import com.group19.orderprocessingservice.domain.repository.order.PortfolioRepository;
import com.group19.orderprocessingservice.enums.PortfolioStatus;
import com.group19.orderprocessingservice.exceptions.CustomException;
import com.group19.orderprocessingservice.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private UserRepository userRepository;

    public Portfolio createNewPortfolio(CreatePortfolioDto cpdto) {
        //check if user exists
        User user = Utils.checkIfUserExists(cpdto.getUserId(), userRepository);
        //create new portfolio
        Portfolio portfolio = new Portfolio(user);
        //persist portfolio in database
        return portfolioRepository.save(portfolio);
    }

    public Iterable<Portfolio> fetchAll(long userId) {
        //check the existence of the user
        Utils.checkIfUserExists(userId, userRepository);
        //fetch all portfolios for the user
        return portfolioRepository.findAllById(userId);
    }

    public Portfolio deletePortfolio(DeletePortfolioDto dpdto) {
        //check if user exist
        User user = Utils.checkIfUserExists(dpdto.getUserId(), userRepository);
        //check if a portfolio exists for that user
        Portfolio portfolio = portfolioRepository.findPortfolioByUserIdAndPortfolioId(dpdto.getUserId(), dpdto.getPortfolioId());
        if (Objects.isNull(portfolio)) {
            throw new CustomException("Portfolio does not exist");
        }
        portfolio.setPortfolioStatus(PortfolioStatus.DELETED);

        return portfolioRepository.save(portfolio);

    }




}
