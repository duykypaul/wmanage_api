package com.duykypaul.wmanage_api.services.impl;

import com.duykypaul.wmanage_api.algorithm.fast.FastToriai;
import com.duykypaul.wmanage_api.beans.*;
import com.duykypaul.wmanage_api.common.CommonConst;
import com.duykypaul.wmanage_api.common.Utils;
import com.duykypaul.wmanage_api.model.Branch;
import com.duykypaul.wmanage_api.model.Material;
import com.duykypaul.wmanage_api.model.User;
import com.duykypaul.wmanage_api.payload.respone.ResponseBean;
import com.duykypaul.wmanage_api.repository.BranchRepository;
import com.duykypaul.wmanage_api.repository.MaterialRepository;
import com.duykypaul.wmanage_api.repository.MaterialTypeRepository;
import com.duykypaul.wmanage_api.repository.ToriaiHeadRepository;
import com.duykypaul.wmanage_api.services.MaterialService;
import com.duykypaul.wmanage_api.services.ToriaiHeadService;
import lombok.extern.log4j.Log4j2;
import org.javatuples.Pair;
import org.modelmapper.ModelMapper;
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
    BranchRepository branchRepository;

    @Autowired
    MaterialTypeRepository materialTypeRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    MaterialService materialService;

    @Autowired
    ModelMapper modelMapper;

    /**
     * dùng để lưu trữ các thanh sắt ảo mà hệ thống tự rạo ra
     */
    private List<Material> zaikoMastersYotei = new ArrayList<>();

    @Override
    public ResponseEntity<?> findAll() {
        List<OrderBean> orderBeans = new ArrayList<>();
        /*List<ConsignmentBean> consignmentBeans = new ArrayList<>();
        try {
            List<Consignment> consignments = toriaiHeadRepository.findAllByIsDeletedIsFalse();
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
        }*/
        return ResponseEntity.ok(orderBeans);
    }

    @Override
    public ResponseEntity<?> saveToriai(ToriaiHeadBean toriaiHeadBean) {
        try {
            /*ToriaiHead toriaiHead = modelMapper.map(toriaiHeadBean, ToriaiHead.class);
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
            consignmentRepository.saveAll(consignments);*/
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
            
            for (Integer[] e : algorithmResult) {
                Arrays.fill(e, 0);
            }
            Pair<String, List<Integer>> orderAndArrIndexRowOfGyo = setOrderAndArrIndexRowOfGyo(gyos);
            String order = orderAndArrIndexRowOfGyo.getValue0();
            List<Integer> arrIndexRowOfGyo = orderAndArrIndexRowOfGyo.getValue1();

            StringBuilder stock = new StringBuilder();
            String messageFromAlgorithm = "";

            /*
             * lấy ra các thanh nguyên liệu gốc(đã nhập kho) hoặc dự kiến nhập kho
             * -> dùng để đưa vào arr arrayZaikoMasterBY phục vụ cho tính toán ma trận
             */
            List<MaterialBean> zaikoMasterBeansBY = materialService.getAllBySeiKBN_B(toriaiHeadBean.getToriaiHeadNo(), toriaiHeadBean.getBranch().getBranchName(),
                    toriaiHeadBean.getMaterialType());
            
            /*
             * lấy ra các thanh nguyên liệu là phần thừa của toriai khác(zaiSeiKBN = R: đã có sẵn; hoặc zaiSeiKBN = Y: dự kiến đc sinh ra )
             * -> dùng để đưa vào arr arrayZaikoMasterYR phục vụ cho tính toán ma trận
             */
            List<MaterialBean> zaiKoMaterialBeansYR = materialService.getAllBySeiKBN_YR(toriaiHeadBean.getToriaiHeadNo(), toriaiHeadBean.getBranch().getBranchName(),
                toriaiHeadBean.getMaterialType(), typeToriai, Utils.getMinInArray(order));
            
            List<MaterialBean> arrayZaikoMasterBY = new ArrayList<>();
            List<MaterialBean> arrayZaikoMasterYR = new ArrayList<>();
            
            if (typeToriai.equals(CommonConst.TORIAI.TYPE_TORIAI.FAST.name())) {
                /*
                 * ưu tiên chọn nguyên liệu gốc
                 */
                for (MaterialBean ele : zaikoMasterBeansBY) {
                    arrayZaikoMasterBY.add(ele);
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
                    for (MaterialBean ele : zaiKoMaterialBeansYR) {
                        /*
                         * cấu trúc của stock: [arrayZaikoMasterYR.getItem()..., arrayZaikoMasterBY.getItem()...]
                         * strong dãy stock các nguyên liệu phần thừa luôn đứng trước nguyên liệu gốc
                         */
                        if (!stock.toString().equals(CommonConst.BLANK)) {
                            List<Integer> lstStock = Utils.parseListInteger(stock.toString());

                            List<Integer> lstStockOfYR = new ArrayList<>(lstStock.subList(0, arrayZaikoMasterYR.size()));
                            List<Integer> lstStockOfBY = new ArrayList<>(lstStock.subList(arrayZaikoMasterYR.size(), lstStock.size()));

                            lstStockOfYR.add(ele.getLength());
                            lstStockOfYR.addAll(lstStockOfBY);

                            lstStock = lstStockOfYR;

                            stock = new StringBuilder(lstStock.stream().map(String::valueOf)
                                .collect(Collectors.joining(CommonConst.COMMA)));
                        } else {
                            stock.append(ele.getLength());
                        }
                        arrayZaikoMasterYR.add(ele);
                        if (isEnoughToUse(order, stock.toString())) break;
                    }
                    messageFromAlgorithm = FastToriai.getMessageFromFastCut(order, stock.toString());
                }
                
            } else {
                /*
                 * ưu tiên chọn nguyên liệu thừa từ gia công khác
                 */
                for (MaterialBean ele : zaiKoMaterialBeansYR) {
                    /*
                     * cấu trúc của stock: [...arrayZaikoMasterYR.getItem(), ...arrayZaikoMasterBY.getItem()]
                     * trong dãy stock các nguyên liệu phần thừa luôn đứng trước nguyên liệu gốc
                     */
                    if (!stock.toString().equals(CommonConst.BLANK)) {
                        List<Integer> lstStock = Utils.parseListInteger(stock.toString());

                        List<Integer> lstStockOfYR = new ArrayList<>(lstStock.subList(0, arrayZaikoMasterYR.size()));
                        List<Integer> lstStockOfBY = new ArrayList<>(lstStock.subList(arrayZaikoMasterYR.size(), lstStock.size()));

                        lstStockOfYR.add(ele.getLength());
                        lstStockOfYR.addAll(lstStockOfBY);

                        lstStock = lstStockOfYR;

                        stock = new StringBuilder(lstStock.stream().map(String::valueOf).collect(Collectors.joining(CommonConst.COMMA)));

                    } else {
                        stock.append(ele.getLength());
                    }
                    arrayZaikoMasterYR.add(ele);
                    if (isEnoughToUse(order, stock.toString())) break;
                }

                /*
                 * get Message From Algorithm
                 */
                String caseSpecialStr = caseSpecial(order, stock.toString());
                if (caseSpecialStr.equals(CommonConst.BLANK)) {
                    //todo
                    //messageFromAlgorithm = getMessageFromSocketServer(order, stock, strTuple, mUser);
                } else {
                    messageFromAlgorithm = caseSpecialStr;
                }

                /*
                 * dữ liệu phần thừa từ gia công khác không đủ để thực hiện thuật toán
                 * -> lấy thêm nguyên liệu gốc (B, Y)
                 */
                if (StringUtils.isEmpty(messageFromAlgorithm)) {
                    for (MaterialBean ele : zaikoMasterBeansBY) {
                        arrayZaikoMasterBY.add(ele);
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
                    arrayZaikoMasterBY.add(materialBean);
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
                messageFromAlgorithm, order, stock.toString(), arrayZaikoMasterBY,
                arrayZaikoMasterYR, arrIndexRowOfGyo);

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
     * @param arrayZaikoMasterBY  map du lieu tu input stock
     * @param arrayZaikoMasterYR  map du lieu tu input stock : select toriaiNo in screen
     * @param arrIndexRowOfGyo
     * @return
     */
    private int setBodyAlgorithm(ToriaiHeadBean toriaiHeadBean, List<ToriaiGyoBean> gyos, List<ToriaiRetsuBean> retsus, Integer[][] algorithmResult,
                                 int retsuIndex, String messageFromAlgorithm, String order, String stock,
                                 List<MaterialBean> arrayZaikoMasterBY, List<MaterialBean> arrayZaikoMasterYR,
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
            if (item < arrayZaikoMasterYR.size()) {
                materialBean = arrayZaikoMasterYR.get(item);
            } else {
                materialBean = arrayZaikoMasterBY.get(item - arrayZaikoMasterYR.size());
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
                retsus.get(retsuIndex).setLength(toriaiAlgorithmBodyBeans.getSteelLength());

                Integer lengthUsed = toriaiAlgorithmBodyBeans.getDetailCutting().entrySet().stream()
                    .map(ent -> Integer.parseInt(ent.getKey().split(CommonConst.UNDERSCORE)[0]) * ent.getValue())
                    .reduce(0, Integer::sum);

                int lengthRemaining = toriaiAlgorithmBodyBeans.getSteelLength() - lengthUsed - toriaiAlgorithmBodyBeans.getCutCounting() * Integer.parseInt(CommonConst.STEEL_BLADE_THICKNESS);
                if(lengthRemaining < 0) {
                    lengthRemaining = 0;
                }
                retsus.get(retsuIndex).setLengthUsed(lengthUsed);
                retsus.get(retsuIndex).setLengthRemaining(lengthRemaining);

                String seiKbn = toriaiAlgorithmBodyBeans.getMaterialBean().getSeiKbn();
                String materialNo = toriaiAlgorithmBodyBeans.getMaterialBean().getMaterialNo();
                if ((seiKbn.equals(CommonConst.MATERIAL.SEI_KBN.Y.name()) && materialNo.startsWith(CommonConst.MATERIAL.SEI_KBN.R.name()))
                    || seiKbn.equals(CommonConst.MATERIAL.SEI_KBN.R.name())) {
                    retsus.get(retsuIndex).setBozaimotoToriaiHeadNo(toriaiAlgorithmBodyBeans.getMaterialBean().getToriaiHeadNo());
                } else {
                    retsus.get(retsuIndex).setBozaimotoToriaiHeadNo(CommonConst.BLANK);
                }

                retsus.get(retsuIndex).setQuantity(item.size());
                String listMaterialNo = item.stream().map(el -> el.getMaterialBean().getMaterialNo()).distinct().collect(Collectors.joining(CommonConst.SPACE));
                retsus.get(retsuIndex).setListMaterialNo(listMaterialNo);
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
        for (int j = 0; j < lstToriaiAlgorithmBodyBeans.size(); j++) {
            for (int i = 0; i < gyos.size(); i++) {

                String key = gyos.get(i).getLength() + CommonConst.UNDERSCORE + i;
                Integer timesCutting = lstToriaiAlgorithmBodyBeans.get(j).get(0).getDetailCutting().get(key);
                Integer quantity = gyos.get(i).getQuantity();
                if (timesCutting != null && quantity != 0) {
                    if (quantity <= timesCutting) {
                        algorithmResult[i][j] = quantity;
                        lstToriaiAlgorithmBodyBeans
                            .get(j)
                            .get(0)
                            .getDetailCutting()
                            .replace(key, timesCutting * lstToriaiAlgorithmBodyBeans.get(j).size() - quantity);
                        gyos.get(i).setQuantity(0);
                    } else {
                        algorithmResult[i][j] = timesCutting;
                        lstToriaiAlgorithmBodyBeans
                            .get(j)
                            .get(0)
                            .getDetailCutting()
                            .replace(key, 0);
                        gyos.get(i).setQuantity(quantity - timesCutting);
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
