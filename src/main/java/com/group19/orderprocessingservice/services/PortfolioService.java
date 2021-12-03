package com.group19.orderprocessingservice.services;

import com.group19.orderprocessingservice.domain.dto.CreatePortfolioDto;
import com.group19.orderprocessingservice.domain.dto.DeletePortfolioDto;
import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.domain.model.order.Portfolio;
import com.group19.orderprocessingservice.domain.repository.auth.UserRepository;
import com.group19.orderprocessingservice.domain.repository.order.PortfolioRepository;
import com.group19.orderprocessingservice.enums.PortfolioStatus;
import com.group19.orderprocessingservice.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    PortfolioRepository portfolioRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public Portfolio createNewPortfolio(CreatePortfolioDto cpdto) {
        //check if user exists
        User user = checkIfUserExists(cpdto.getUserId());
        //create new portfolio
        Portfolio portfolio = new Portfolio(user);
        //persist portfolio in database
        return portfolioRepository.save(portfolio);
    }

    @Transactional
    public Iterable<Portfolio> fetchAll(CreatePortfolioDto cpdto) {
        //check the existence of the user
        checkIfUserExists(cpdto.getUserId());
        //fetch all portfolios for the user
        return portfolioRepository.findAllById(cpdto.getUserId());
    }

    @Transactional
    public Portfolio deletePortfolio(DeletePortfolioDto dpdto) {
        //check if user exist
        User user = checkIfUserExists(dpdto.getUserId());
        //check if a portfolio exists for that user
        Portfolio portfolio = portfolioRepository.findPortfolioByUserIdAndPortfolioId(dpdto.getUserId(), dpdto.getPortfolioId());
        if (Objects.isNull(portfolio)) {
            throw new CustomException("Portfolio does not exist");
        }
        portfolio.setPortfolioStatus(PortfolioStatus.DELETED);

        return portfolioRepository.save(portfolio);

    }



    private User checkIfUserExists(Long cpdto) {
        Optional<User> user = userRepository.findById(cpdto);
        //user exists ? return user
        if (user.isEmpty()) {
        // else -->>  throw exception
            throw new CustomException("User does not exist.");
        }
        return user.get();
    }
    private Portfolio checkIfPortfolioExists(Long dpdto) {
        Optional<Portfolio> portfolio = portfolioRepository.findById(dpdto);
        //portfolio exists ? return user
        if (portfolio.isEmpty()) {
            // else -->>  throw exception
            throw new CustomException("Portfolio does not exist.");
        }
        return portfolio.get();
    }
}
