package com.group19.orderprocessingservice.util;

import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.domain.model.order.Portfolio;
import com.group19.orderprocessingservice.domain.repository.auth.UserRepository;
import com.group19.orderprocessingservice.domain.repository.order.PortfolioRepository;
import com.group19.orderprocessingservice.exceptions.CustomException;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

@Log4j2
public class Utils {

    public static User checkIfUserExists(long id, UserRepository userRepository) {
        log.info("Checking if user with id-{} exists in the database", id);
        Optional<User> user = userRepository.findById(id);
        //user exists ? return user
        if (user.isEmpty()) {
            // else -->>  throw exception
            log.info("User with id-{} doesn't exists in the database", id);

            throw new CustomException("User does not exist.");
        }
        log.info("Returning portfolio for {}", user.get().getFirstName());
        return user.get();
    }
    public static Portfolio checkIfPortfolioExists(long id, PortfolioRepository portfolioRepository) {
        log.info("Checking if portfolio with id-{} exists in the database", id);
        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
        //portfolio exists ? return portfolio
        if (portfolio.isEmpty()) {
            // else -->>  throw exception
            log.info("Portfolio with id-{} doesn't exists in the database", id);

            throw new CustomException("Portfolio does not exist.");
        }
        log.info("Returning portfolio for portfolio id - {}", portfolio.get().getId());
        return portfolio.get();
    }


}
