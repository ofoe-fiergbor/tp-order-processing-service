package com.group19.orderprocessingservice.util;

import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.domain.model.order.Portfolio;
import com.group19.orderprocessingservice.domain.repository.auth.UserRepository;
import com.group19.orderprocessingservice.domain.repository.order.PortfolioRepository;
import com.group19.orderprocessingservice.exceptions.CustomException;

import java.util.Optional;

public class Utils {

    public static User checkIfUserExists(long id, UserRepository userRepository) {
        Optional<User> user = userRepository.findById(id);
        //user exists ? return user
        if (user.isEmpty()) {
            // else -->>  throw exception
            throw new CustomException("User does not exist.");
        }
        return user.get();
    }
    public static Portfolio checkIfPortfolioExists(long id, PortfolioRepository portfolioRepository) {
        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
        //portfolio exists ? return portfolio
        if (portfolio.isEmpty()) {
            // else -->>  throw exception
            throw new CustomException("Portfolio does not exist.");
        }
        return portfolio.get();
    }


}
