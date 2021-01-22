package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.ToriaiGyo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToriaiGyoRepository extends JpaRepository<ToriaiGyo, Long> {

    List<ToriaiGyo> findAllByIsDeletedIsFalse();

    @Modifying
    @Query("UPDATE ToriaiGyo o SET o.isDeleted = true WHERE o.id IN ?1")
    void deleteByIdIn(Long[] ids);

    @Modifying
    @Query("UPDATE ToriaiGyo o SET o.isDeleted = true WHERE o.toriaiHeadNo = ?1")
    void deleteLogicByToriaiHeadNo(String toriaiHeadNo);
    
    @Modifying
    @Query(value = "FROM ToriaiGyo as gyo WHERE gyo.toriaiHeadNo = ?1 and gyo.isDeleted = false ORDER BY gyo.gyoNo", nativeQuery = false)
    List<ToriaiGyo> findByToriaiHeadNo(String toriaiHeadNo);

    @Modifying
    @Query(value = "DELETE FROM ToriaiGyo as gyo WHERE gyo.toriaiHeadNo = ?1")
    void deleteByToriaiHeadNo(String toriaiHeadNo);
}
