package com.duykypaul.wmanage_api.repository;

import com.duykypaul.wmanage_api.model.ToriaiHead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToriaiHeadRepository extends JpaRepository<ToriaiHead, Long> {

    List<ToriaiHead> findAllByIsDeletedIsFalse();

    @Modifying
    @Query("UPDATE ToriaiHead o SET o.isDeleted = true WHERE o.id IN ?1")
    void deleteByIdIn(Long[] ids);

    @Modifying
    @Query("UPDATE ToriaiHead o SET o.isDeleted = true WHERE o.toriaiHeadNo = ?1")
    void deleteByToriaiHeadNo(String toriaiHeadNo);

    @Query("SELECT COALESCE(MAX(SUBSTRING(m.toriaiHeadNo, 3, 5)), 0) FROM ToriaiHead m WHERE m.branch.id = ?1")
    String getMaxNoCurrent(Long id);

    @Modifying
    @Query("UPDATE ToriaiHead o SET o.status = ?2 WHERE o.toriaiHeadNo = ?1")
    void completeToriai(String toriaiHeadNo, String status);
}
