package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToriaiHeadRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByIsDeletedIsFalse();

    @Modifying
    @Query("UPDATE ToriaiHead o SET o.isDeleted = true WHERE o.id IN ?1")
    void deleteByIdIn(Long[] ids);

}
