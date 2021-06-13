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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@Transactional
public class ConsignmentServiceImpl implements ConsignmentService {

    final
    OrderRepository orderRepository;

    final
    ConsignmentRepository consignmentRepository;

    final
    BranchRepository branchRepository;

    final
    MaterialTypeRepository materialTypeRepository;

    final
    ModelMapper modelMapper;

    public ConsignmentServiceImpl(OrderRepository orderRepository, ConsignmentRepository consignmentRepository, BranchRepository branchRepository, MaterialTypeRepository materialTypeRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.consignmentRepository = consignmentRepository;
        this.branchRepository = branchRepository;
        this.materialTypeRepository = materialTypeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<?> findAll() {
        List<ConsignmentBean> consignmentBeans = new ArrayList<>();
        try {
            List<Consignment> consignments = consignmentRepository.findAllByIsDeletedIsFalse();
            consignments.forEach(consignment -> {
                ConsignmentBean consignmentBean = modelMapper.map(consignment, ConsignmentBean.class);
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
