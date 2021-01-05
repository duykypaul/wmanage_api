package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByIsDeletedIsFalse();
}
