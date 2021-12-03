package com.group19.orderprocessingservice.domain.repository.order;

import com.group19.orderprocessingservice.domain.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM orders WHERE orders.user_id = :userId", nativeQuery=true)
    List<Order> findAllOrdersById(@Param("userId") long userId);
}
