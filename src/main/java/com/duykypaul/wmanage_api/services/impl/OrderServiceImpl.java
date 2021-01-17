package com.duykypaul.wmanage_api.services.impl;

import com.duykypaul.wmanage_api.beans.ConsignmentBean;
import com.duykypaul.wmanage_api.beans.MaterialTypeBean;
import com.duykypaul.wmanage_api.beans.OrderBean;
import com.duykypaul.wmanage_api.model.Branch;
import com.duykypaul.wmanage_api.model.Consignment;
import com.duykypaul.wmanage_api.model.MaterialType;
import com.duykypaul.wmanage_api.model.Order;
import com.duykypaul.wmanage_api.payload.respone.ResponseBean;
import com.duykypaul.wmanage_api.repository.BranchRepository;
import com.duykypaul.wmanage_api.repository.ConsignmentRepository;
import com.duykypaul.wmanage_api.repository.MaterialTypeRepository;
import com.duykypaul.wmanage_api.repository.OrderRepository;
import com.duykypaul.wmanage_api.services.OrderService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

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
        List<OrderBean> orderBeans = new ArrayList<>();
        List<ConsignmentBean> consignmentBeans = new ArrayList<>();
        try {
            List<Consignment> consignments = consignmentRepository.findAllByIsDeletedIsFalse();
            consignments.forEach(consignment -> {
                ConsignmentBean consignmentBean = modelMapper.map(consignment, ConsignmentBean.class);
                consignmentBeans.add(consignmentBean);
            });
            List<Order> orders = orderRepository.findAllByIsDeletedIsFalse();
            orders.forEach(order -> {
                OrderBean orderBean = modelMapper.map(order, OrderBean.class);
                List<ConsignmentBean> consignmentBeansByOrderId = consignmentBeans.stream()
                                                                    .filter(item -> item.getOrder().getId().equals(orderBean.getId()))
                                                                    .collect(Collectors.toList());
                orderBean.setConsignments(consignmentBeansByOrderId);
                orderBean.setQuantity(consignmentBeansByOrderId.size());
                orderBeans.add(orderBean);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(orderBeans);
    }

    @Override
    public ResponseEntity<?> saveOrder(OrderBean orderBean) {
        try {
            Order order = modelMapper.map(orderBean, Order.class);
            Branch branch = branchRepository.findByBranchCode(orderBean.getBranch().getBranchCode())
                .orElseThrow(() -> new RuntimeException("Branch code notfound"));
            order.setBranch(branch);
            Order finalOrder = orderRepository.save(order);
            List<Consignment> consignments = new ArrayList<>();

            orderBean.getConsignments().forEach(item -> {
                Consignment consignment = modelMapper.map(item, Consignment.class);
                MaterialTypeBean materialTypeBean = item.getMaterialType();
                MaterialType materialType = materialTypeRepository.findByMaterialTypeAndDimension(materialTypeBean.getMaterialType(), materialTypeBean.getDimension())
                    .orElseThrow(() -> new RuntimeException("materialType notfound"));
                consignment.setConsignmentNo(consignment.getConsignmentNo() + "N" + finalOrder.getId());
                consignment.setMaterialType(materialType);
                consignment.setOrder(finalOrder);

                consignments.add(consignment);

            });
            consignmentRepository.saveAll(consignments);
            return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), null, "success"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseBean(HttpStatus.BAD_REQUEST.value(), null, "error"));
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
        try {
            consignmentRepository.deleteByIdIn(ids);
            orderRepository.deleteByIdIn(ids);
            return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), null, "success"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseBean(HttpStatus.BAD_REQUEST.value(), null, "error"));
    }
}
