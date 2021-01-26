package com.duykypaul.wmanage_api.services;


import com.duykypaul.wmanage_api.beans.ToriaiHeadBean;
import com.duykypaul.wmanage_api.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ToriaiHeadService {
    ResponseEntity<?> findAll();

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> findAll(Integer pageNo, Integer pageSize, String sortBy);

    ResponseEntity<?> getNewToriaiHeadNo(String branchName);

    ResponseEntity<?> deleteAllByIdIn(Long[] ids);

    ResponseEntity<?> saveToriai(ToriaiHeadBean toriaiHeadBean);

    ResponseEntity<?> exeAlgorithm(ToriaiHeadBean toriaiHeadBean, User user);

    ResponseEntity<?> updateToriai(ToriaiHeadBean toriaiHeadBean, boolean isDelete, boolean isUpdate);

    ResponseEntity<?> deleteAllByToriaiHeadNoIn(List<String> toriaiHeadNos);
}
