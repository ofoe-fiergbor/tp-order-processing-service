package com.group19.orderprocessingservice.domain.repository.order;

import com.group19.orderprocessingservice.domain.model.order.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @Query(value = "SELECT * FROM portfolios WHERE portfolios.user_id = :id", nativeQuery=true)
    List<Portfolio> findAllById(@Param("id") Long id);

    @Query(value = "SELECT * FROM portfolios WHERE portfolios.user_id = :userId AND portfolios.id = :portfolioId", nativeQuery=true)
    Portfolio findPortfolioByUserIdAndPortfolioId(@Param("userId") Long userId, @Param("portfolioId") Long portfolioId);

}
