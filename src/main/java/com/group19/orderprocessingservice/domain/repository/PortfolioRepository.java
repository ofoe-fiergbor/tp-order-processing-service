package com.group19.orderprocessingservice.domain.repository;

import com.group19.orderprocessingservice.domain.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> { }
