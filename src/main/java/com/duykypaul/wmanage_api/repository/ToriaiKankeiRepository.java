package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.ToriaiKankei;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToriaiKankeiRepository extends JpaRepository<ToriaiKankei, Long> {

    List<ToriaiKankei> findAllByIsDeletedIsFalse();

    @Modifying
    @Query("UPDATE ToriaiKankei o SET o.isDeleted = true WHERE o.id IN ?1")
    void deleteByIdIn(Long[] ids);

    @Modifying
    @Query("UPDATE ToriaiKankei o SET o.isDeleted = true WHERE o.toriaiHeadNo = ?1")
    void deleteLogicByToriaiHeadNo(String toriaiHeadNo);

    @Modifying
    @Query(value = "FROM ToriaiKankei as gyo WHERE gyo.toriaiHeadNo = ?1 and gyo.isDeleted = false ORDER BY gyo.gyoNo, gyo.retsuNo")
    List<ToriaiKankei> findByToriaiHeadNo(String toriaiHeadNo);

    @Modifying
    @Query(value = "DELETE FROM ToriaiKankei as gyo WHERE gyo.toriaiHeadNo = ?1")
    void deleteByToriaiHeadNo(String toriaiHeadNo);
}
