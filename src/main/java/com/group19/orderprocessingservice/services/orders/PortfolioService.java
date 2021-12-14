package com.group19.orderprocessingservice.services.orders;

import com.group19.orderprocessingservice.domain.dto.*;
import com.group19.orderprocessingservice.domain.model.auth.User;
import com.group19.orderprocessingservice.domain.model.order.Order;
import com.group19.orderprocessingservice.domain.model.order.Portfolio;
import com.group19.orderprocessingservice.domain.repository.auth.UserRepository;
import com.group19.orderprocessingservice.domain.repository.order.OrderRepository;
import com.group19.orderprocessingservice.domain.repository.order.PortfolioRepository;
import com.group19.orderprocessingservice.enums.PortfolioStatus;
import com.group19.orderprocessingservice.exceptions.CustomException;
import com.group19.orderprocessingservice.services.redis.MessagingService;
import com.group19.orderprocessingservice.util.Utils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final MessagingService messagingService;
    private final OrderRepository orderRepository;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, UserRepository userRepository, MessagingService messagingService, OrderRepository orderRepository) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.messagingService = messagingService;
        this.orderRepository = orderRepository;
    }

    public Portfolio createNewPortfolio(CreatePortfolioDto cpdto) {

        messagingService.saveMessage("Portfolio :: Creating portfolio for user-id - "+ cpdto.getUserId());

        //check if user exists
        User user = Utils.checkIfUserExists(cpdto.getUserId(), userRepository);
        //create new portfolio
        Portfolio portfolio = new Portfolio(user, cpdto.getName());
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

    public Map<String, Holding> getPortfolio(long portfolioId) {
        Map<String, Holding> holdings = new HashMap<>();
        List<Order> orderList = orderRepository.findAllByPortfolioId(portfolioId);

        Holding temp;

        for (Order order: orderList) {
            if (!holdings.containsKey(order.getProduct())) {
                holdings.put(order.getProduct().toUpperCase(), new Holding(order.getQuantity(), order.getQuantity() * order.getPrice()));
            } else {
                temp = holdings.get(order.getProduct().toUpperCase());
                temp.increaseQuantity(order.getQuantity());
                temp.increaseValue(order.getQuantity() * order.getPrice());

            }
        }
        return holdings;
    }




}
