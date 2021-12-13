package com.group19.orderprocessingservice.services.orders;

import com.group19.orderprocessingservice.domain.dto.CreatePortfolioDto;
import com.group19.orderprocessingservice.domain.dto.DeletePortfolioDto;
import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.domain.model.order.Portfolio;
import com.group19.orderprocessingservice.domain.repository.auth.UserRepository;
import com.group19.orderprocessingservice.domain.repository.order.PortfolioRepository;
import com.group19.orderprocessingservice.enums.PortfolioStatus;
import com.group19.orderprocessingservice.exceptions.CustomException;
import com.group19.orderprocessingservice.services.redis.MessagingService;
import com.group19.orderprocessingservice.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final MessagingService messagingService;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, UserRepository userRepository, MessagingService messagingService) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.messagingService = messagingService;
    }

    public Portfolio createNewPortfolio(CreatePortfolioDto cpdto) {

        messagingService.saveMessage("Portfolio :: Creating portfolio for user-id - "+ cpdto.getUserId());

        //check if user exists
        User user = Utils.checkIfUserExists(cpdto.getUserId(), userRepository);
        //create new portfolio
        Portfolio portfolio = new Portfolio(user);
        //persist portfolio in database
        messagingService.saveMessage("Portfolio :: Portfolio created successfully for user-id "+ cpdto.getUserId());
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
        messagingService.saveMessage("Portfolio :: Portfolio with portfolio-id - "+ dpdto.getPortfolioId()+" deleted successfully.");
        portfolio.setPortfolioStatus(PortfolioStatus.DELETED);

        return portfolioRepository.save(portfolio);

    }




}
