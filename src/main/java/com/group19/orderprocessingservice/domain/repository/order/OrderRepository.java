package com.group19.orderprocessingservice.domain.repository.order;

import com.group19.orderprocessingservice.domain.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> { }
