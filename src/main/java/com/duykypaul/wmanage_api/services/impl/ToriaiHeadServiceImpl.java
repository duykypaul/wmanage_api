package com.duykypaul.wmanage_api.services.impl;

import com.duykypaul.wmanage_api.algorithm.fast.FastToriai;
import com.duykypaul.wmanage_api.beans.*;
import com.duykypaul.wmanage_api.common.CommonConst;
import com.duykypaul.wmanage_api.common.Utils;
import com.duykypaul.wmanage_api.model.*;
import com.duykypaul.wmanage_api.payload.respone.ResponseBean;
import com.duykypaul.wmanage_api.repository.*;
import com.duykypaul.wmanage_api.services.MaterialService;
import com.duykypaul.wmanage_api.services.ToriaiHeadService;
import lombok.extern.log4j.Log4j2;
import org.javatuples.Pair;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class ToriaiHeadServiceImpl implements ToriaiHeadService {

    @Autowired
    ToriaiHeadRepository toriaiHeadRepository;
    
    @Autowired
    ToriaiRetsuRepository toriaiRetsuRepository;

    @Autowired
    ToriaiGyoRepository toriaiGyoRepository;

    @Autowired
    ToriaiKankeiRepository toriaiKankeiRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    MaterialTypeRepository materialTypeRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    MaterialService materialService;

    @Autowired
    ConsignmentRepository consignmentRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ModelMapper modelMapper;

    /**
     * dùng để lưu trữ các thanh sắt ảo mà hệ thống tự rạo ra
     */
    private List<Material> zaikoMastersYotei = new ArrayList<>();

    @Override
    public ResponseEntity<?> findAll() {
        List<ToriaiHeadBean> toriaiHeadBeans = new ArrayList<>();
        try {
            List<ToriaiHead> toriaiHeads = toriaiHeadRepository.findAllByIsDeletedIsFalse();
            List<ToriaiRetsu> toriaiRetsus = toriaiRetsuRepository.findAllByIsDeletedIsFalse();
            List<ToriaiGyo> toriaiGyos = toriaiGyoRepository.findAllByIsDeletedIsFalse();
            List<ToriaiKankei> toriaiKankeis = toriaiKankeiRepository.findAllByIsDeletedIsFalse();
            toriaiHeads.forEach(toriaiHead -> {
                ToriaiHeadBean toriaiHeadBean = modelMapper.map(toriaiHead, ToriaiHeadBean.class);

                List<ToriaiRetsu> toriaiRetsusPerToriai = toriaiRetsus.stream()
                    .filter(item -> item.getToriaiHeadNo().equals(toriaiHead.getToriaiHeadNo()))
                    .sorted(Comparator.comparing(ToriaiRetsu::getRetsuNo))
                    .collect(Collectors.toList());
                List<ToriaiRetsuBean> toriaiRetsusBeanPerToriai = modelMapper.map(toriaiRetsusPerToriai, new TypeToken<List<ToriaiRetsuBean>>(){}.getType());
                toriaiHeadBean.setListToriaiRetsu(toriaiRetsusBeanPerToriai);

                List<ToriaiGyo> toriaiGyosPerToriai = toriaiGyos.stream()
                    .filter(item -> item.getToriaiHeadNo().equals(toriaiHead.getToriaiHeadNo()))
                    .sorted(Comparator.comparing(ToriaiGyo::getGyoNo))
                    .collect(Collectors.toList());
                List<ToriaiGyoBean> toriaiGyosBeanPerToriai = modelMapper.map(toriaiGyosPerToriai, new TypeToken<List<ToriaiGyoBean>>(){}.getType());
                toriaiHeadBean.setListToriaiGyo(toriaiGyosBeanPerToriai);

                List<ToriaiKankei> toriaiKankeisPerToriai = toriaiKankeis.stream()
                    .filter(item -> item.getToriaiHeadNo().equals(toriaiHead.getToriaiHeadNo()))
                    .sorted(Comparator.comparing(ToriaiKankei::getGyoNo)
                        .thenComparing(ToriaiKankei::getRetsuNo))
                    .collect(Collectors.toList());

                List<ToriaiKankeiBean> toriaiKankeisBeanPerToriai = modelMapper.map(toriaiKankeisPerToriai, new TypeToken<List<ToriaiKankeiBean>>(){}.getType());
                toriaiHeadBean.setListToriaiKankei(toriaiKankeisBeanPerToriai);
                toriaiHeadBeans.add(toriaiHeadBean);
            });

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(toriaiHeadBeans);
    }

    @Override
    public ResponseEntity<?> saveToriai(ToriaiHeadBean toriaiHeadBean) {
        try {
            String toriaiHeadNo = toriaiHeadBean.getToriaiHeadNo();
            Branch branch = branchRepository.findByBranchName(toriaiHeadBean.getBranch().getBranchName())
                .orElseThrow(() -> new RuntimeException("Branch name notfound"));
            MaterialTypeBean materialTypeBean = toriaiHeadBean.getMaterialType();
            MaterialType materialType = materialTypeRepository.findByMaterialTypeNameAndDimension(materialTypeBean.getMaterialTypeName(), materialTypeBean.getDimension())
                .orElseThrow(() -> new RuntimeException("Branch name notfound"));

            ToriaiHead toriaiHead = modelMapper.map(toriaiHeadBean, ToriaiHead.class);
            toriaiHead.setBranch(branch);
            toriaiHead.setMaterialType(materialType);
            List<ToriaiRetsu> toriaiRetsus = modelMapper.map(toriaiHeadBean.getListToriaiRetsu(), new TypeToken<List<ToriaiRetsu>>() {}.getType());
            List<ToriaiKankei> toriaiKankeis = modelMapper.map(toriaiHeadBean.getListToriaiKankei(), new TypeToken<List<ToriaiKankei>>() {}.getType());

            List<Consignment> consignmentsPerToriai = new ArrayList<>();
            Set<Order> ordersPerToriai = new HashSet<>();
            List<ToriaiGyo> toriaiGyos = new ArrayList<>();
            toriaiHeadBean.getListToriaiGyo().forEach(toriaiGyoBean -> {
                ToriaiGyo toriaiGyo = modelMapper.map(toriaiGyoBean, ToriaiGyo.class);
                Consignment consignment = consignmentRepository
                    .findByConsignmentNoAndLength(toriaiGyoBean.getConsignment().getConsignmentNo(), toriaiGyoBean.getLength())
                    .orElseThrow(() -> new RuntimeException("consignmentNo and length notfound"));
                toriaiGyo.setConsignment(consignment);
                consignment.setStatus(CommonConst.ORDER.INVENTORY_STATUS.PLAN.name());
                consignmentsPerToriai.add(consignment);
                ordersPerToriai.add(consignment.getOrder());
                toriaiGyos.add(toriaiGyo);
            });

            ordersPerToriai.forEach(item -> {
                item.setStatus(CommonConst.ORDER.INVENTORY_STATUS.PLAN.name());
            });

            /*
             * Check Conflict with another toriai
             */
            List<Material> listMaterial = materialRepository
                .findByMaterialNoAndStatus(toriaiHeadNo, CommonConst.MATERIAL.STATUS.PLAN.name());

            List<Material> lstMaterialUsedByAnotherToriai = listMaterial.stream()
                .filter(
                    item -> !Utils.NullToBlank(item.getToriaiHeadNoUsed()).isEmpty() && !Utils.NullToBlank(item.getToriaiHeadNoUsed()).equals(toriaiHeadNo)
                ).collect(Collectors.toList());

            if (lstMaterialUsedByAnotherToriai.size() > 0) {
                List<String> lstZaiToriaiNo = lstMaterialUsedByAnotherToriai.stream().map(Material::getToriaiHeadNoUsed).distinct().collect(Collectors.toList());
                String message = String.join(CommonConst.COMMA, lstZaiToriaiNo) + " are using materials from " + toriaiHeadNo;
                return ResponseEntity.ok(new ResponseBean(HttpStatus.BAD_REQUEST.value(), null, message));
            } else {
                FreeRawMaterialInput(toriaiHeadNo, listMaterial);
            }

            /*
             * release toriai body after re-algorithm and save it to db
             */
            toriaiGyoRepository.deleteByToriaiHeadNo(toriaiHeadNo);
            toriaiRetsuRepository.deleteByToriaiHeadNo(toriaiHeadNo);
            toriaiKankeiRepository.deleteByToriaiHeadNo(toriaiHeadNo);

            /*
             * update zaiKo master
             */
            for (int i = 0; i < toriaiRetsus.size(); i++) {
                ToriaiRetsu retsuBean = toriaiRetsus.get(i);
                List<String> listMaterialNo = Arrays.stream(retsuBean.getListMaterialNo().trim().split(CommonConst.SPACE, -1)).distinct().collect(Collectors.toList());
                List<ToriaiKankei> listKankei = toriaiKankeis.stream().filter(item -> item.getRetsuNo().equals(retsuBean.getRetsuNo())).collect(Collectors.toList());
                for (int j = 0; j < listMaterialNo.size(); j++) {
                    for (Material master : zaikoMastersYotei) {
                        if (listMaterialNo.get(j).equals(master.getMaterialNo())) {
                            materialRepository.save(master);
                            break;
                        }
                    }
                    String materialNo = "";
                    Material masterBean = materialRepository.getMaterialByMaterialNoForCompute(listMaterialNo.get(j), toriaiHeadNo);
                    if(masterBean == null) {
                        System.out.println("null yeah");
                    } else {
                        materialNo = masterBean.getMaterialNo();
                    }

                    int positionCutMaterialNo = materialNo.length() > 10 ? 11 : materialNo.length();

                    /*int countGeneratedZaikoP = zaikoService.getCountGeneratedZaiko(zaiKanrinoMaster, CommonConst.PARAMETER_ZAIKO.ZAI_SEIKBN.P);*/
                    /*
                     * đếm số lượng mã kanrino đã được sinh ra ứng với sản phẩm hoặc vật bảo lưu (seikbn = PZR)
                     */
                    int countGeneratedMaterialP = materialService.getCountGeneratedMaterial(materialNo);
                    int numberGeneratedThisBatch = 0;
                    /*
                     *  create zaiko seiKBN real is P
                     */
                    for (ToriaiKankei kankeiBean : toriaiKankeis) {
                        ToriaiGyo gyoItem = toriaiGyos.stream().filter(item -> item.getGyoNo().equals(kankeiBean.getGyoNo())).collect(Collectors.toList()).get(0);
                        for (int l = 0; l < kankeiBean.getQuantity(); l++) {
                            String newMaterialNo = CommonConst.MATERIAL.SEI_KBN.P.name() + materialNo.substring(1, positionCutMaterialNo)
                                + 1 + Utils.LeadZeroNumber(l + 1 + numberGeneratedThisBatch + countGeneratedMaterialP, 2);
                            assert masterBean != null;
                            Material materialBeanPItem = Material.builder()
                                .materialType(masterBean.getMaterialType())
                                .branch(masterBean.getBranch())
                                .seiKbn(CommonConst.MATERIAL.SEI_KBN.Y.name())
                                .materialNo(newMaterialNo)
                                .toriaiHeadNo(toriaiHeadNo)
                                .toriaiRetsuNo(retsuBean.getRetsuNo())
                                .toriaiGyoNo(gyoItem.getGyoNo())
                                .toriaiRetsuNoIndex((listMaterialNo.size() > 1) ? (j + 1) : null)
                                .length(gyoItem.getLength())
                                .status(CommonConst.MATERIAL.STATUS.PLAN.name())
                                .build();
                            materialRepository.saveAndFlush(materialBeanPItem);
                        }
                        numberGeneratedThisBatch += kankeiBean.getQuantity();
                    }

                    /*
                     * create zaiko seiKBN real is R
                     */
                    String newMaterialNo;
                    if(materialNo.length() == 14) {
                        newMaterialNo = CommonConst.MATERIAL.SEI_KBN.R.name() + materialNo.substring(1);
                    } else {
                        newMaterialNo = CommonConst.MATERIAL.SEI_KBN.R.name() + materialNo.substring(1, positionCutMaterialNo) + 1;
                    }
                    Material materialBeanRItem = Material.builder()
                        .materialType(masterBean.getMaterialType())
                        .branch(masterBean.getBranch())
                        .seiKbn(CommonConst.MATERIAL.SEI_KBN.Y.name())
                        .materialNo(newMaterialNo)
                        .toriaiHeadNo(toriaiHeadNo)
                        .toriaiRetsuNo(retsuBean.getRetsuNo())
                        .toriaiRetsuNoIndex((listMaterialNo.size() > 1) ? (j + 1) : null)
                        .length(retsuBean.getLengthRemaining())
                        .status(CommonConst.MATERIAL.STATUS.PLAN.name())
                        .build();

                    // phase #1
                    if (Utils.NullToBlank(masterBean.getToriaiHeadNo()).equals(CommonConst.BLANK)) {
                        if (listMaterialNo.size() > 1) {
                            materialBeanRItem.setToriaiUsedRetsuNoIndex(j + 1);
                        } else {
                            materialBeanRItem.setToriaiUsedRetsuNoIndex(null);
                        }
                    } else { // phase #2
                        materialBeanRItem.setToriaiRetsuNo(retsuBean.getRetsuNo());
                        if (listMaterialNo.size() > 1) {
                            materialBeanRItem.setToriaiUsedRetsuNoIndex(j + 1);
                        } else {
                            materialBeanRItem.setToriaiUsedRetsuNoIndex(null);
                        }
                    }

                    materialRepository.saveAndFlush(materialBeanRItem);

                    masterBean.setToriaiHeadNoUsed(toriaiHeadNo);
                    masterBean.setStatus(CommonConst.MATERIAL.STATUS.PLAN.name());
                    masterBean.setToriaiUsedRetsuNo(retsuBean.getRetsuNo());
                    if (listMaterialNo.size() > 1) {
                        masterBean.setToriaiUsedRetsuNoIndex(j + 1);
                    } else {
                        masterBean.setToriaiUsedRetsuNoIndex(null);
                    }
                    materialRepository.saveAndFlush(masterBean);
                }
            }

            toriaiHeadRepository.save(toriaiHead);
            toriaiRetsuRepository.saveAll(toriaiRetsus);
            toriaiGyoRepository.saveAll(toriaiGyos);
            toriaiKankeiRepository.saveAll(toriaiKankeis);

            consignmentRepository.saveAll(consignmentsPerToriai);
            orderRepository.saveAll(ordersPerToriai);

            return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), null, "success"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseBean(HttpStatus.BAD_REQUEST.value(), null, "error"));
    }

    private void FreeRawMaterialInput(String toriaiHeadNo, List<Material> listMaterial) {
        for (Material item : listMaterial) {
            /*
             * raw material input have zaiSeikbn as B or R or Y (if seikbn equals to Y then in the past it has zai_you_kou = 1)
             */
            String seiKbn = item.getSeiKbn();
            String toriaiHeadNoUsed = Utils.NullToBlank(item.getToriaiHeadNoUsed());
            String torisizi = Utils.NullToBlank(item.getToriaiHeadNo());
            boolean seiKbnBRY = seiKbn.equals(CommonConst.MATERIAL.SEI_KBN.B.name())
                || seiKbn.equals(CommonConst.MATERIAL.SEI_KBN.R.name())
                || (seiKbn.equals(CommonConst.MATERIAL.SEI_KBN.Y.name())
                && torisizi.equals(CommonConst.BLANK));
            /*
             * raw material input have zaiSeikbn as Y and it has zai_you_kou = 5
             */
            boolean zanzaiY = seiKbn.equals(CommonConst.MATERIAL.SEI_KBN.Y.name())
                && item.getStatus().equals(CommonConst.MATERIAL.STATUS.PLAN.name())
                && toriaiHeadNoUsed.equals(toriaiHeadNo)
                && !torisizi.equals(CommonConst.BLANK);

            if (seiKbnBRY || zanzaiY) {
                if (seiKbnBRY) {
                    item.setStatus(CommonConst.MATERIAL.STATUS.ACTIVE.name());
                }
                if (zanzaiY) {
                    item.setStatus(CommonConst.MATERIAL.STATUS.PLAN.name());
                }
                item.setToriaiHeadNoUsed(null);
                item.setToriaiUsedRetsuNo(null);
                materialRepository.save(item);
            } else {
                materialRepository.delete(item);
            }
        }
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
    public ResponseEntity<?> getNewToriaiHeadNo(String branchName) {
        try {
            Branch branch = branchRepository.findByBranchName(branchName)
                .orElseThrow(() -> new RuntimeException("Branch code notfound"));
            int maxNoCurrent = Integer.parseInt(toriaiHeadRepository.getMaxNoCurrent(branch.getId()));
            String newToriaiHeadNo = branch.getBranchCode() + Utils.LeadZeroNumber(maxNoCurrent + 1, 5);
            return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), newToriaiHeadNo, "success"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseBean(HttpStatus.BAD_REQUEST.value(), null, "error"));
    }

    @Override
    public ResponseEntity<?> deleteAllByIdIn(Long[] ids) {
        try {
            toriaiHeadRepository.deleteByIdIn(ids);
            return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), null, "success"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ResponseBean(HttpStatus.BAD_REQUEST.value(), null, "error"));
    }

    @Override
    public ResponseEntity<?> exeAlgorithm(ToriaiHeadBean toriaiHeadBean, User user) {
        List<ToriaiGyoBean> gyos = toriaiHeadBean.getListToriaiGyo();
        List<ToriaiRetsuBean> retsus = toriaiHeadBean.getListToriaiRetsu();
        Integer[][] algorithmResult = new Integer[gyos.size()][CommonConst.TORIAI.NUMBER_COLUMN_RETSU];
        String typeToriai = toriaiHeadBean.getTypeToriai();
        int retsuIndex = 0;
        try {
            Branch branch = branchRepository.findByBranchName(toriaiHeadBean.getBranch().getBranchName())
                .orElseThrow(() -> new RuntimeException("Branch code notfound"));
            //todo
            /*
             * check conflict with another toriai
             */

            for (Integer[] e : algorithmResult) {
                Arrays.fill(e, 0);
            }
            Pair<String, List<Integer>> orderAndArrIndexRowOfGyo = setOrderAndArrIndexRowOfGyo(gyos);
            String order = orderAndArrIndexRowOfGyo.getValue0();
            List<Integer> arrIndexRowOfGyo = orderAndArrIndexRowOfGyo.getValue1();

            StringBuilder stock = new StringBuilder();
            String messageFromAlgorithm;

            /*
             * lấy ra các thanh nguyên liệu gốc(đã nhập kho) hoặc dự kiến nhập kho
             * -> dùng để đưa vào arr arrayMaterialBeanBY phục vụ cho tính toán ma trận
             */
            List<MaterialBean> materialBeansBY = materialService.getAllBySeiKBN_B(toriaiHeadBean.getToriaiHeadNo(), toriaiHeadBean.getBranch().getBranchName(),
                    toriaiHeadBean.getMaterialType());
            
            /*
             * lấy ra các thanh nguyên liệu là phần thừa của toriai khác(zaiSeiKBN = R: đã có sẵn; hoặc zaiSeiKBN = Y: dự kiến đc sinh ra )
             * -> dùng để đưa vào arr arrayMaterialBeanYR phục vụ cho tính toán ma trận
             */
            List<MaterialBean> materialBeansYR = materialService.getAllBySeiKBN_YR(toriaiHeadBean.getToriaiHeadNo(), toriaiHeadBean.getBranch().getBranchName(),
                toriaiHeadBean.getMaterialType(), typeToriai, Utils.getMinInArray(order));
            
            List<MaterialBean> arrayMaterialBeanBY = new ArrayList<>();
            List<MaterialBean> arrayMaterialBeanYR = new ArrayList<>();
            
            if (typeToriai.equals(CommonConst.TORIAI.TYPE_TORIAI.FAST.name())) {
                /*
                 * ưu tiên chọn nguyên liệu gốc
                 */
                for (MaterialBean ele : materialBeansBY) {
                    arrayMaterialBeanBY.add(ele);
                    if (!stock.toString().equals(CommonConst.BLANK)) stock.append(CommonConst.COMMA);
                    stock.append(ele.getLength().intValue());
                }

                /*
                 * get Message From Algorithm
                 */
                String caseSpecialStr = caseSpecial(order, stock.toString());
                if (caseSpecialStr.equals(CommonConst.BLANK)) {
                    messageFromAlgorithm = FastToriai.getMessageFromFastCut(order, stock.toString());
                } else {
                    messageFromAlgorithm = caseSpecialStr;
                }

                /*
                 * dữ liệu từ nguyên liệu gốc không đủ để thực hiện thuật toán
                 * -> lấy thêm nguyên liệu phần thừa từ gia công khác (R, Y)
                 */
                if (StringUtils.isEmpty(messageFromAlgorithm)) {
                    stock = chooseMaterialFromAnotherToriai(order, stock, materialBeansYR, arrayMaterialBeanYR);
                    messageFromAlgorithm = FastToriai.getMessageFromFastCut(order, stock.toString());
                }
                
            } else {
                /*
                 * ưu tiên chọn nguyên liệu thừa từ gia công khác
                 */
                stock = chooseMaterialFromAnotherToriai(order, stock, materialBeansYR, arrayMaterialBeanYR);

                /*
                 * get Message From Algorithm
                 */
                String caseSpecialStr = caseSpecial(order, stock.toString());
                if (caseSpecialStr.equals(CommonConst.BLANK)) {
                    //todo
                    //messageFromAlgorithm = getMessageFromSocketServer(order, stock, strTuple, mUser);
                    messageFromAlgorithm = FastToriai.getMessageFromFastCut(order, stock.toString());
                } else {
                    messageFromAlgorithm = caseSpecialStr;
                }

                /*
                 * dữ liệu phần thừa từ gia công khác không đủ để thực hiện thuật toán
                 * -> lấy thêm nguyên liệu gốc (B, Y)
                 */
                if (StringUtils.isEmpty(messageFromAlgorithm)) {
                    for (MaterialBean ele : materialBeansBY) {
                        arrayMaterialBeanBY.add(ele);
                        if (!stock.toString().equals(CommonConst.BLANK)) stock.append(CommonConst.COMMA);
                        stock.append(ele.getLength());
                        if (isEnoughToUse(order, stock.toString())) break;
                    }

                    /*
                     * get Message From Algorithm
                     */
                    caseSpecialStr = caseSpecial(order, stock.toString());
                    if (caseSpecialStr.equals(CommonConst.BLANK)) {
                        //todo
                        //messageFromAlgorithm = getMessageFromSocketServer(order, stock, strTuple, mUser);
                        messageFromAlgorithm = FastToriai.getMessageFromFastCut(order, stock.toString());
                    } else {
                        messageFromAlgorithm = caseSpecialStr;
                    }
                }
            }

            /*
             * material in not enough to run algorithm -> fake and insert data to db to can run algorithm
             */
            if (StringUtils.isEmpty(messageFromAlgorithm)) {
                /*find number record need insert to db*/
                int numberInsert = findNumberNeed(order, stock);
                int maxMaterialNoCurrent = Integer.parseInt(materialRepository
                    .generateMaterialNo(branch.getId()));
                for (int i = 0; i < numberInsert; i++) {
                    String materialNo = CommonConst.MATERIAL.SEI_KBN.B.name() 
                        + branch.getBranchCode() + Utils.LeadZeroNumber(maxMaterialNoCurrent + i + 1, 8);
                    // todo
                    Material material  = Material.builder()
                        .materialNo(materialNo)
                        .seiKbn(CommonConst.MATERIAL.SEI_KBN.B.name() )
                        .length(CommonConst.LENGTH_DEFAULT)
                        .status(CommonConst.MATERIAL.STATUS.ACTIVE.name())
                        .toriaiHeadNo(CommonConst.BLANK)
                        .toriaiHeadNoUsed(CommonConst.BLANK)
                        .isDeleted(false)
                        .createdAt(new Date())
                        .createdBy(user.getUsername())
                        .build();

                    zaikoMastersYotei.add(material);
                    MaterialBean materialBean = modelMapper.map(material, MaterialBean.class);
                    arrayMaterialBeanBY.add(materialBean);
                    if (!stock.toString().equals(CommonConst.BLANK)) stock.append(CommonConst.COMMA);
                    stock.append(materialBean.getLength());
                }

                if (typeToriai.equals(CommonConst.TORIAI.TYPE_TORIAI.FAST.name())) {
                    messageFromAlgorithm = FastToriai.getMessageFromFastCut(order, stock.toString());
                } else {
                    String caseSpecialStr = caseSpecial(order, stock.toString());
                    if (caseSpecialStr.equals(CommonConst.BLANK)) {
                        //todo
                        //messageFromAlgorithm = getMessageFromSocketServer(order, stock, strTuple, mUser);
                        messageFromAlgorithm = FastToriai.getMessageFromFastCut(order, stock.toString());
                    } else {
                        messageFromAlgorithm = caseSpecialStr;
                    }
                }
            }

            /*
             * tính toán ma trận để hiển thị
             */
            retsuIndex = setBodyAlgorithm(toriaiHeadBean, gyos, retsus, algorithmResult, retsuIndex,
                messageFromAlgorithm, order, stock.toString(), arrayMaterialBeanBY,
                arrayMaterialBeanYR, arrIndexRowOfGyo);

            if (retsuIndex >= CommonConst.TORIAI.NUMBER_COLUMN_RETSU) {
                toriaiHeadBean.getMessage().add("data vượt quá số cột quy định, hãy cắt bớt lô hàng");
                return ResponseEntity.ok(new ResponseBean(HttpStatus.BAD_REQUEST.value(), toriaiHeadBean, "error"));
            }
            toriaiHeadBean.setAlgorithmResult(algorithmResult);

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
        return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), toriaiHeadBean, null));
    }

    @Override
    public ResponseEntity<?> updateToriai(ToriaiHeadBean toriaiHeadBean, boolean isDelete, boolean isUpdate) {
        try {
            String toriaiHeadNo = toriaiHeadBean.getToriaiHeadNo();
            List<String> listConsignmentNo = toriaiHeadBean.getListToriaiGyo().stream()
                .map(item -> item.getConsignment().getConsignmentNo())
                .distinct()
                .collect(Collectors.toList());

            List<Integer> listLengthSteel = toriaiHeadBean.getListToriaiGyo().stream()
                .map(ToriaiGyoBean::getLength)
                .distinct()
                .collect(Collectors.toList());

            if(isUpdate) {
                /*UPDATE MATERIAL*/
                List<Material> materials = materialRepository.getMaterialByToriai(toriaiHeadNo, CommonConst.MATERIAL.STATUS.PLAN.name());
                List<Material> listMaterialExpectPR
                    = materials.stream()
                    .filter(item -> Utils.NullToBlank(item.getToriaiHeadNo()).equals(toriaiHeadNo))
                    .collect(Collectors.toList());
                for (Material item : listMaterialExpectPR) {
                    String seiKbn = item.getMaterialNo().substring(0, 1);
                    item.setSeiKbn(seiKbn);
                    if (Utils.NullToBlank(item.getToriaiHeadNoUsed()).equals(CommonConst.BLANK)) {
                        item.setStatus(CommonConst.MATERIAL.STATUS.ACTIVE.name());
                    }
                    materialRepository.saveAndFlush(item);
                }
                /*UPDATE CONSIGNMENTS*/
                List<Consignment> consignments = consignmentRepository.findAllByConsignmentNoAndLength(listConsignmentNo, listLengthSteel);
                consignments.forEach(consignment -> {
                    consignment.setStatus(CommonConst.ORDER.INVENTORY_STATUS.TORIAI.name());
                });
                consignmentRepository.saveAll(consignments);

                /*UPDATE ORDER*/
                Set<Order> ordersPerToriai = consignments.stream().map(Consignment::getOrder).collect(Collectors.toSet());
                ordersPerToriai.forEach(order -> {
                    List<Consignment> consignmentsByOrder = consignmentRepository.findALlByOrder_Id(order.getId());
                    long numberToriai = consignmentsByOrder.stream().filter(item -> item.getStatus().equals(CommonConst.ORDER.INVENTORY_STATUS.TORIAI.name())).count();
                    if(numberToriai == consignmentsByOrder.size()) {
                        order.setStatus(CommonConst.ORDER.INVENTORY_STATUS.TORIAI.name());
                    }
                });
                orderRepository.saveAll(ordersPerToriai);

                toriaiHeadRepository.completeToriai(toriaiHeadNo, CommonConst.TORIAI.STATUS.COMPLETE.name());
            }

            return ResponseEntity.ok(new ResponseBean(HttpStatus.OK.value(), null, null));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.ok(new ResponseBean(HttpStatus.BAD_REQUEST.value(), null, null));
        }
    }

    private StringBuilder chooseMaterialFromAnotherToriai(String order, StringBuilder stock, List<MaterialBean> materialBeansYR, List<MaterialBean> arrayMaterialBeanYR) {
        for (MaterialBean ele : materialBeansYR) {
            /*
             * cấu trúc của stock: [...arrayMaterialBeanYR.getItem(), ...arrayMaterialBeanBY.getItem()]
             * trong dãy stock các nguyên liệu phần thừa luôn đứng trước nguyên liệu gốc
             */
            if (!stock.toString().equals(CommonConst.BLANK)) {
                List<Integer> lstStock = Utils.parseListInteger(stock.toString());

                List<Integer> lstStockOfYR = new ArrayList<>(lstStock.subList(0, arrayMaterialBeanYR.size()));
                List<Integer> lstStockOfBY = new ArrayList<>(lstStock.subList(arrayMaterialBeanYR.size(), lstStock.size()));

                lstStockOfYR.add(ele.getLength());
                lstStockOfYR.addAll(lstStockOfBY);

                lstStock = lstStockOfYR;

                stock = new StringBuilder(lstStock.stream().map(String::valueOf).collect(Collectors.joining(CommonConst.COMMA)));

            } else {
                stock.append(ele.getLength());
            }
            arrayMaterialBeanYR.add(ele);
            if (isEnoughToUse(order, stock.toString())) break;
        }
        return stock;
    }

    /**
     *
     * @param toriaiHeadBean
     * @param gyos                List<ToriaiShijiBodyGyoBean> results expect
     * @param retsus              List<ToriaiShijiBodyRetsuBean> The materials used for cutting are displayed on the screen
     * @param algorithmResult     Integer[][] Matrix of data displayed toriai screen
     * @param retsuIndex
     * @param messageFromAlgorithm String message <--> arn
     * @param order               Input mong muon cat (寸法 va 数量 tren man hinh toriai)
     * @param stock               Du lieu dau vao (input tai cac cot 取1 -> 取19)(hoac du lieu lay trong db)
     * @param arrayMaterialBeanBY  map du lieu tu input stock
     * @param arrayMaterialBeanYR  map du lieu tu input stock : select toriaiNo in screen
     * @param arrIndexRowOfGyo
     * @return
     */
    private int setBodyAlgorithm(ToriaiHeadBean toriaiHeadBean, List<ToriaiGyoBean> gyos, List<ToriaiRetsuBean> retsus, Integer[][] algorithmResult,
                                 int retsuIndex, String messageFromAlgorithm, String order, String stock,
                                 List<MaterialBean> arrayMaterialBeanBY, List<MaterialBean> arrayMaterialBeanYR,
                                 List<Integer> arrIndexRowOfGyo) {
        List<Integer> lstIndexSocket = Utils.parseListInteger(messageFromAlgorithm);
        List<Integer> lstOrder = Utils.parseListInteger(order);
        List<Integer> lstStock = Utils.parseListInteger(stock);

        /*
         * lưu trữ số lần sử dụng của các index stock, trả về từ lstIndexSocket
         */
        Map<Integer, Integer> mapProcessToriai = new HashMap<>();

        /*
         * lưu trữ list index stock được sử dụng, trả về từ lstIndexSocket
         */
        List<Integer> listKey = new ArrayList<>();

        setMapProcessToriai(lstIndexSocket, mapProcessToriai, listKey);

        List<ToriaiAlgorithmBodyBeans> algorithmBodyBeans = new ArrayList<>();

        List<List<ToriaiAlgorithmBodyBeans>> lstToriaiAlgorithmBodyBeans = new ArrayList<>();

        for (Integer item : listKey) {
            //dua vao vi tri cat tren mapProcessToriai de lay do dai thanh thep
            Integer lenSteel = lstStock.get(item);
            Integer cutCounting = mapProcessToriai.get(item);
            MaterialBean materialBean;
            if (item < arrayMaterialBeanYR.size()) {
                materialBean = arrayMaterialBeanYR.get(item);
            } else {
                materialBean = arrayMaterialBeanBY.get(item - arrayMaterialBeanYR.size());
            }
            Map<String, Integer> detailCutting = new HashMap<>();
            for (int i = 0; i < lstOrder.size(); i++) {
                if (lstIndexSocket.get(i).equals(item)) {
                    String key = lstOrder.get(i) + CommonConst.UNDERSCORE + arrIndexRowOfGyo.get(i);
                    detailCutting.merge(key, 1, Integer::sum);
                }
            }
            ToriaiAlgorithmBodyBeans bean = new ToriaiAlgorithmBodyBeans(lenSteel, cutCounting, materialBean, detailCutting);
            algorithmBodyBeans.add(bean);
        }

        /*
         * merge algorithmBodyBeans nếu có cách cắt giống nhau
         * 
         */
        lstToriaiAlgorithmBodyBeans.add(new ArrayList<>(Collections.singletonList(algorithmBodyBeans.get(0))));
        for (int i = 1; i < algorithmBodyBeans.size(); i++) {
            int index = compareToriaiAlgorithmBodyBeans(algorithmBodyBeans.get(i), lstToriaiAlgorithmBodyBeans);
            if (index != -1) {
                lstToriaiAlgorithmBodyBeans.get(index).add(algorithmBodyBeans.get(i));
            } else {
                lstToriaiAlgorithmBodyBeans.add(new ArrayList<>(Collections.singletonList(algorithmBodyBeans.get(i))));
            }
        }
        
        /*
         * set dữ liệu material vào List<> retsus
         */
        for (List<ToriaiAlgorithmBodyBeans> item : lstToriaiAlgorithmBodyBeans) {
            if(item.size() > 0) {
                ToriaiAlgorithmBodyBeans toriaiAlgorithmBodyBeans = item.get(0);
                if (retsuIndex == CommonConst.TORIAI.NUMBER_COLUMN_RETSU) {
                    return CommonConst.TORIAI.NUMBER_COLUMN_RETSU;
                }
                ToriaiRetsuBean retsuItem = retsus.get(retsuIndex);
                retsuItem.setToriaiHeadNo(toriaiHeadBean.getToriaiHeadNo());
                retsuItem.setRetsuNo(retsuIndex + 1);
                retsuItem.setLength(toriaiAlgorithmBodyBeans.getSteelLength());

                Integer lengthUsed = toriaiAlgorithmBodyBeans.getDetailCutting().entrySet().stream()
                    .map(ent -> Integer.parseInt(ent.getKey().split(CommonConst.UNDERSCORE)[0]) * ent.getValue())
                    .reduce(0, Integer::sum);

                int lengthRemaining = toriaiAlgorithmBodyBeans.getSteelLength() - lengthUsed - toriaiAlgorithmBodyBeans.getCutCounting() * Integer.parseInt(CommonConst.STEEL_BLADE_THICKNESS);
                if(lengthRemaining < 0) {
                    lengthRemaining = 0;
                }
                retsuItem.setLengthUsed(lengthUsed);
                retsuItem.setLengthRemaining(lengthRemaining);

                String seiKbn = toriaiAlgorithmBodyBeans.getMaterialBean().getSeiKbn();
                String materialNo = toriaiAlgorithmBodyBeans.getMaterialBean().getMaterialNo();
                if ((seiKbn.equals(CommonConst.MATERIAL.SEI_KBN.Y.name()) && materialNo.startsWith(CommonConst.MATERIAL.SEI_KBN.R.name()))
                    || seiKbn.equals(CommonConst.MATERIAL.SEI_KBN.R.name())) {
                    retsuItem.setBozaimotoToriaiHeadNo(toriaiAlgorithmBodyBeans.getMaterialBean().getToriaiHeadNo());
                } else {
                    retsuItem.setBozaimotoToriaiHeadNo(CommonConst.BLANK);
                }

                retsuItem.setQuantity(item.size());
                String listMaterialNo = item.stream().map(el -> el.getMaterialBean().getMaterialNo()).distinct().collect(Collectors.joining(CommonConst.SPACE));
                retsuItem.setListMaterialNo(listMaterialNo);
                retsuIndex++;
            }
        }

        /*
         * compute summary data in toriaihead
         */

        Integer totalLengthUsed = retsus.stream()
            .filter(f -> null !=  f.getLength() && null != f.getQuantity())
            .map(ent -> ent.getLength() * ent.getQuantity())
            .reduce(0, Integer::sum);

        Integer totalLengthRemain = retsus.stream()
            .filter(f -> null !=  f.getLengthRemaining() && null != f.getQuantity())
            .map(ent -> ent.getLengthRemaining() * ent.getQuantity())
            .reduce(0, Integer::sum);
        Integer rateUse = roundInteger((float) (toriaiHeadBean.getTotalLengthExpected() * 100 / totalLengthUsed));
        Integer rateRemain = roundInteger((float) (totalLengthRemain * 100 / totalLengthUsed));
        toriaiHeadBean.setTotalLengthUsed(totalLengthUsed);
        toriaiHeadBean.setTotalLengthRemain(totalLengthRemain);
        toriaiHeadBean.setRateUse(rateUse);
        toriaiHeadBean.setRateRemain(rateRemain);

        /*
         * set ma trận kankei
         */
        List<ToriaiGyoBean> gyosClone = gyos.stream().map(ToriaiGyoBean::Clone).collect(Collectors.toList());
        for (int j = 0; j < lstToriaiAlgorithmBodyBeans.size(); j++) {
            for (int i = 0; i < gyosClone.size(); i++) {
                String key = gyosClone.get(i).getLength() + CommonConst.UNDERSCORE + i;
                Integer timesCutting = lstToriaiAlgorithmBodyBeans.get(j).get(0).getDetailCutting().get(key);
                Integer quantity = gyosClone.get(i).getQuantity();
                if (timesCutting != null && quantity != 0) {
                    if (quantity <= timesCutting) {
                        algorithmResult[i][j] = quantity;
                        lstToriaiAlgorithmBodyBeans
                            .get(j)
                            .get(0)
                            .getDetailCutting()
                            .replace(key, timesCutting * lstToriaiAlgorithmBodyBeans.get(j).size() - quantity);
                        gyosClone.get(i).setQuantity(0);
                    } else {
                        algorithmResult[i][j] = timesCutting;
                        lstToriaiAlgorithmBodyBeans
                            .get(j)
                            .get(0)
                            .getDetailCutting()
                            .replace(key, 0);
                        gyosClone.get(i).setQuantity(quantity - timesCutting);
                    }
                }
            }
        }
        return retsuIndex;
    }

    /**
     * so sánh hai ToriaiAlgorithmBodyBeans
     *
     * @param item
     * @param lst
     * @return
     */
    private int compareToriaiAlgorithmBodyBeans(ToriaiAlgorithmBodyBeans item, List<List<ToriaiAlgorithmBodyBeans>> lst) {
        for (int j = 0; j < lst.size(); j++) {
            try {
                Integer cutCounting = lst.get(j).get(0).getCutCounting();
                Integer steelLength = lst.get(j).get(0).getSteelLength();
                Integer zaiLength = lst.get(j).get(0).getMaterialBean().getLength();
                String toriaiHeadNo = Utils.NullToBlank(lst.get(j).get(0).getMaterialBean().getToriaiHeadNo());
                Map<String, Integer> detailCutting = lst.get(j).get(0).getDetailCutting();
                boolean aBoolean = item.getCutCounting().equals(cutCounting)
                    && item.getSteelLength().equals(steelLength)
                    && Utils.NullToBlank(item.getMaterialBean().getToriaiHeadNo()).equals(toriaiHeadNo)
                    && item.getMaterialBean().getLength().equals(zaiLength)
                    && item.getDetailCutting().equals(detailCutting);
                if (aBoolean) {
                    return j;
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return -1;
    }

    /**
     * xét giá trị cho mapProcessToriai, và listKey
     * ex: lstIndexSocket = [0,0,0,1,1,2]
     * mapProcessToriai(key, value): [(0: 3), (1: 2), (2: 1)]
     * listKey: [0, 1, 2]
     *
     * @param lstIndexSocket   lstIndexSocket
     * @param mapProcessToriai mapProcessToriai
     * @param listKey          listKey
     */
    private void setMapProcessToriai(List<Integer> lstIndexSocket, Map<Integer, Integer> mapProcessToriai, List<Integer> listKey) {
        for (Integer item : lstIndexSocket) {
            Integer iMap = mapProcessToriai.get(item);
            if (null == iMap) {
                mapProcessToriai.put(item, 1);
                listKey.add(item);
            } else {
                mapProcessToriai.put(item, iMap + 1);
            }
        }
        Collections.sort(listKey);
    }

    /**
     * @param order
     * @param stock
     * @return
     */
    private int findNumberNeed(String order, StringBuilder stock) {
        List<Integer> lstOrder = Utils.parseListInteger(order);
        List<Integer> lstStock = new ArrayList<>();
        if (!stock.toString().equals(CommonConst.BLANK)) {
            lstStock = Utils.parseListInteger(stock.toString());
        }

        lstStock.sort(Collections.reverseOrder()); // desc
        lstOrder.sort(Collections.reverseOrder()); // desc

        Integer[] arrOrder = new Integer[lstOrder.size()];
        arrOrder = lstOrder.toArray(arrOrder);
        Integer[] arrStock = new Integer[lstStock.size()];
        arrStock = lstStock.toArray(arrStock);

        Optional<Integer> minOder = Arrays.stream(arrOrder).min(Integer::compareTo);
        int[] arrIndexStockUsed = new int[arrOrder.length];
        Arrays.fill(arrIndexStockUsed, -1);

        int unit = Integer.parseInt(CommonConst.STEEL_BLADE_THICKNESS);
        int returnValue = 0;
        int indexLoop = 0;
        while (Arrays.stream(arrIndexStockUsed).filter(item -> item == -1).count() > 0) {
            for (int i = indexLoop; i < arrStock.length; i++) {
                int remaining = arrStock[i];
                for (int j = 0; j < arrOrder.length; j++) {
                    if (remaining < minOder.get()) break;
                    if (remaining >= arrOrder[j] && arrIndexStockUsed[j] == -1) {
                        arrIndexStockUsed[j] = i;
                        remaining -= (arrOrder[j] + unit);
                        if (remaining < 0) {
                            remaining = 0;
                        }
                    }
                }
            }
            if (Arrays.stream(arrIndexStockUsed).filter(item -> item == -1).count() > 0) {
                returnValue++;
                arrStock = Arrays.copyOf(arrStock, arrStock.length + 1);
                arrStock[arrStock.length - 1] = CommonConst.LENGTH_DEFAULT;
            }
            indexLoop = arrStock.length - 1;
        }
        return returnValue;
    }

    /**
     * gioi han so luong thanh sat truyen vao thuat toan arn de giam thoi gian tinh toan
     *
     * @param order
     * @param stock
     * @return
     */
    private boolean isEnoughToUse(String order, String stock) {
        if (stock.equals(CommonConst.BLANK)) return false;
        List<Integer> lstOrder = Utils.parseListInteger(order);
        List<Integer> lstStock = Utils.parseListInteger(stock);
        Collections.sort(lstStock); // asc
        Collections.sort(lstOrder); // asc

        for (int i = 0; i < lstOrder.size(); i++) {
            for (int j = 0; j < lstStock.size(); j++) {
                if (lstOrder.get(i) <= lstStock.get(j)) {
                    lstOrder.remove(i--);
                    lstStock.remove(j);
                    break;
                }
            }
        }
        return lstOrder.size() == 0;
    }

    /**
     * tạo list order theo format 1000,1000,2000
     * mỗi thanh trong một hàng sẽ có vị trí zaiGyo được dánh dấu trong mảng arrIndexRowOfGyo
     *
     * @param gyos danh sách order bean
     * @return Pair<String, List<Integer>>
     */
    private Pair<String, List<Integer>> setOrderAndArrIndexRowOfGyo(List<ToriaiGyoBean> gyos) {
        String order = "";
        List<Integer> arrIndexRowOfGyo = new ArrayList<>();
        for (int i = 0; i < gyos.size(); i++) {
            ToriaiGyoBean gyoItem = gyos.get(i);
            int[] arrayOrder = new int[gyoItem.getQuantity()];
            Arrays.fill(arrayOrder, gyoItem.getLength());
            if (!StringUtils.isEmpty(order)) {
                order += CommonConst.COMMA;
            }
            order += Arrays.stream(arrayOrder).mapToObj(String::valueOf).collect(Collectors.joining(CommonConst.COMMA));

            for (int j = 0; j < gyoItem.getQuantity(); j++) {
                arrIndexRowOfGyo.add(i); // i is gyoNo index
            }
        }
        return Pair.with(order, arrIndexRowOfGyo);
    }

    /**
     * desc: xu li truong hop tat ca oder co cung chieu dai, tat ca stock co cung chieu dai
     *
     * @param order 1000 1000 1000 
     * @param stock 13000 13000
     * @return String
     */
    private String caseSpecial(String order, String stock) {
        if (stock.equals(CommonConst.BLANK)) return CommonConst.BLANK;
        List<Integer> lstOrder = Utils.parseListInteger(order);
        List<Integer> lstStock =  Utils.parseListInteger(stock);
        int unit = Integer.parseInt(CommonConst.STEEL_BLADE_THICKNESS);

        int numberOrder = lstOrder.size();
        StringBuilder returnValue = new StringBuilder();
        if (Utils.checkDistinctArray(lstOrder) && Utils.checkDistinctArray(lstStock)) {
            int numberIronBar;
            if ((lstOrder.get(0) + unit) > lstStock.get(0) / 2) {
                numberIronBar = lstOrder.size();
            } else {
                numberIronBar = (int) Math.ceil((double) lstOrder.size() / ((lstStock.get(0) + unit) / (lstOrder.get(0) + unit)));
            }
            if (numberIronBar <= lstStock.size()) {
                for (int i = 0; i < numberIronBar; i++) {
                    int cutTimes;
                    if(lstOrder.get(0).equals(lstStock.get(i))) {
                        cutTimes = 1;
                    } else {
                        cutTimes = lstStock.get(i) / (lstOrder.get(0) + unit);
                    }
                    if (cutTimes < numberOrder) {
                        numberOrder -= cutTimes;
                    } else {
                        cutTimes = numberOrder;
                        numberOrder = 0;
                    }
                    returnValue.append(generatingLstIndexStockUsed(String.valueOf(i), cutTimes));
                }
                return returnValue.substring(0, returnValue.length() - 1);
            }
        }
        return CommonConst.BLANK;
    }

    private String generatingLstIndexStockUsed(String index, int n) {
        StringBuilder returnValue = new StringBuilder();
        for (int i = 0; i < n; i++) {
            returnValue.append(index).append(CommonConst.COMMA);
        }
        return returnValue.toString();
    }

    public static int roundInteger(Float value) {
        int integerOnly = value.intValue();
        if (value.toString().split("\\.")[1].equals(CommonConst.NUMBER_5) && integerOnly % 2 == 0) {
            return integerOnly;
        }
        return Math.round(value);
    }
}
