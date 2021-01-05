package com.duykypaul.wmanage_api.services.impl;

import com.duykypaul.wmanage_api.beans.ConsignmentBean;
import com.duykypaul.wmanage_api.model.Consignment;
import com.duykypaul.wmanage_api.repository.BranchRepository;
import com.duykypaul.wmanage_api.repository.ConsignmentRepository;
import com.duykypaul.wmanage_api.repository.MaterialTypeRepository;
import com.duykypaul.wmanage_api.repository.OrderRepository;
import com.duykypaul.wmanage_api.services.ConsignmentService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@Transactional
public class ConsignmentServiceImpl implements ConsignmentService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ConsignmentRepository consignmentRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    MaterialTypeRepository materialTypeRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> findAll() {
        List<ConsignmentBean> consignmentBeans = new ArrayList<>();
        try {
            List<Consignment> consignments = consignmentRepository.findAllByIsDeletedIsFalse();
            consignments.forEach(consignment -> {
                ConsignmentBean consignmentBean = modelMapper.map(consignment, ConsignmentBean.class);
                /*OrderBean orderBean = modelMapper.map(consignment.getOrder(), OrderBean.class);
                consignmentBean.setOrder(orderBean);
                MaterialTypeBean materialTypeBean = modelMapper.map(consignment.getMaterialType(), MaterialTypeBean.class);
                consignmentBean.setMaterialType(materialTypeBean);*/
                consignmentBeans.add(consignmentBean);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(consignmentBeans);
    }

    @Override
    public ResponseEntity<?> saveConsignment(ConsignmentBean consignmentBean) {
        return null;
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteAllByIdIn(Long[] ids) {
        return ResponseEntity.ok(null);
    }
}
