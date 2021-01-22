package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.ToriaiRetsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToriaiRetsuRepository extends JpaRepository<ToriaiRetsu, Long> {

    List<ToriaiRetsu> findAllByIsDeletedIsFalse();

    @Modifying
    @Query("UPDATE ToriaiRetsu o SET o.isDeleted = true WHERE o.id IN ?1")
    void deleteByIdIn(Long[] ids);

    @Modifying
    @Query("UPDATE ToriaiRetsu o SET o.isDeleted = true WHERE o.toriaiHeadNo = ?1")
    void deleteLogicByToriaiHeadNo(String toriaiHeadNo);

    @Modifying
    @Query(value = "FROM ToriaiRetsu as gyo WHERE gyo.toriaiHeadNo = ?1 and gyo.isDeleted = false ORDER BY gyo.retsuNo")
    List<ToriaiRetsu> findByToriaiHeadNo(String toriaiHeadNo);

    @Modifying
    @Query(value = "DELETE FROM ToriaiRetsu as gyo WHERE gyo.toriaiHeadNo = ?1")
    void deleteByToriaiHeadNo(String toriaiHeadNo);
}
