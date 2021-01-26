package com.duykypaul.wmanage_api.algorithm.fast;

import com.duykypaul.wmanage_api.common.CommonConst;
import com.duykypaul.wmanage_api.common.Utils;
import lombok.val;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FastToriai {
    
    /**
     * chon ra phuong phap co phan thua tot nhat trong list danh sach co cung so luong thanh nguyen lieu can dung
     *
     * @param order                           danh sach san pham yeu cau
     * @param stock                           danh sach nguyen lieu co the dung
     * @return String
     */
    public static String getMessageFromFastCut(String order, String stock) {
        if (stock.trim().equals(CommonConst.BLANK)) return CommonConst.BLANK;

        List<FastCutBean> listArn = new ArrayList<>();
        int[] messageFromArnAlgorithm;
        /*
         * arrOrderInit = 2000, 3000, 5000, 1500, 7000
         */
        val arrOrderInit = Utils.parseArrayInt(order);

        /*
         * sắp xếp mảng order theo thứ tự từ cao đến thấp
         * arrOrder = 7000, 5000, 3000, 2000, 1500
         */
        int[] arrOrder = IntStream.of(arrOrderInit)
            .boxed()
            .sorted(Comparator.reverseOrder())
            .mapToInt(i -> i)
            .toArray();

        /*
         * sắp xếp lại chỉ số index trong mảng order ban đầu theo thứ tự từ cao đến thấp (của giá trị)
         * sortedIndicesOrder = 4, 2, 1, 0, 3
         */
        int[] sortedIndicesOrder = IntStream.range(0, arrOrderInit.length)
            .boxed()
            .sorted(Comparator.comparing(i -> -arrOrderInit[i]))
            .mapToInt(ele -> ele)
            .toArray();

        val arrStockInit = Utils.parseArrayInt(stock);

        /*
         * sắp xếp mảng stock theo thứ tự từ cao đến thấp
         */
        int[] arrStock = IntStream.of(arrStockInit)
            .boxed().sorted(Comparator.reverseOrder())
            .mapToInt(i -> i)
            .toArray();

        /*
         * chỉ số index trong mảng stock theo thứ tự từ cao đến thấp
         */
        int[] sortedIndicesStock = IntStream.range(0, arrStockInit.length)
            .boxed()
            .sorted(Comparator.comparing(i -> -arrStockInit[i]))
            .mapToInt(ele -> ele)
            .toArray();

        int[] arrCheckMaterialCanBeCut = new int[arrStock.length];
        int numberMaterialRemoved = 0;
        Arrays.fill(arrCheckMaterialCanBeCut, 1);

        fastCutMain(listArn, arrCheckMaterialCanBeCut, numberMaterialRemoved, arrStock, arrOrder);

        listArn = listArn.stream()
            .sorted(Comparator.comparing(FastCutBean::getNumberMaterial)
                .thenComparing(FastCutBean::getRemain))
            .collect(Collectors.toList());

        if (listArn.size() > 0) {
            messageFromArnAlgorithm = listArn.get(0).getArrIndexStockUsed();
            Map<Integer, Integer> mapOutputArn = new HashMap<>();
            Map<Integer, Integer> mapIndexSortedIndicesOrder = new HashMap<>();
            for (int i = 0; i < arrOrder.length; i++) {
                mapOutputArn.put(i, messageFromArnAlgorithm[i]);
                mapIndexSortedIndicesOrder.put(sortedIndicesOrder[i], i);
            }
            int[] arrMessageConvertByOrder = new int[arrOrder.length];
            for (int i = 0; i < arrOrder.length; i++) {
                arrMessageConvertByOrder[i] = mapOutputArn.get(mapIndexSortedIndicesOrder.get(i));
            }
            int[] arrMessageConvertForStock = new int[arrMessageConvertByOrder.length];
            for (int i = 0; i < arrMessageConvertByOrder.length; i++) {
                arrMessageConvertForStock[i] = sortedIndicesStock[arrMessageConvertByOrder[i]];
            }
            return Arrays.stream(arrMessageConvertForStock).mapToObj(String::valueOf).collect(Collectors.joining(CommonConst.COMMA));
        } else {
            return CommonConst.BLANK;
        }
    }

    /**
     * Thực hiện cắt bắt đầu từ các thanh có độ dài lớn nhất, sau đó cứ giảm bớt đi các thanh có độ dài từ cao đến thấp (numberMaterialRemoved++)
     * cho đến khi không thể cắt được nữa -> dừng lại.
     * Trong đó có các thanh được đánh dấu là môt bộ(phải được cắt từ 1 thanh nguyên liệu)
     *
     * @param listArn                         luu danh sach cac ket qua tot nhat
     * @param arrCheckMaterialCanBeCut        dung de loai bo dan cac thanh co do dai lon
     * @param numberMaterialRemoved           so luong cac thanh sat da bi loai bo so voi ban dau
     * @param arrStock                        danh sach nguyen lieu trong kho
     * @param arrOrder                        danh sach san pham yeu cau
     * 
     */
    private static void fastCutMain(List<FastCutBean> listArn, int[] arrCheckMaterialCanBeCut,
                                int numberMaterialRemoved, int[] arrStock, int[] arrOrder) {
        int unit = Integer.parseInt(CommonConst.TORIAI.STEEL_BLADE_THICKNESS);
        while (true) {
            int[] arrRemain = new int[arrStock.length];
            OptionalInt minOder = Arrays.stream(arrOrder).min();
            /*
             * mảng lưu cách cắt toriai
             */
            int[] arrIndexStockUsed = new int[arrOrder.length];
            Arrays.fill(arrIndexStockUsed, -1);
            for (int i = 0; i < arrStock.length; i++) {
                if (arrCheckMaterialCanBeCut[i] == 1) {
                    int remaining = arrStock[i];
                    for (int j = 0; j < arrOrder.length; j++) {
                        if (remaining < minOder.getAsInt()) break;
                        if (remaining >= arrOrder[j] && arrIndexStockUsed[j] == -1) {
                            arrIndexStockUsed[j] = i;
                            remaining -= (arrOrder[j] + unit);
                            if (remaining < 0) {
                                remaining = 0;
                            }
                        }
                    }
                    arrRemain[i] = remaining;
                }
            }
            /*
             * kiểm tra nếu không thể cắt được nữa thì dừng lại
             */
            if (Arrays.stream(arrIndexStockUsed).anyMatch(item -> item == -1)) {
                break;
            } else {
                /*
                 * số lượng thanh sắt cần dùng
                 */
                int numberMaterial = (int) Arrays.stream(arrIndexStockUsed).distinct().count();
                /*
                 * tổng phần thừa với cách cắt tương ứng
                 */
                int remain = IntStream.range(0, arrRemain.length).filter(i -> arrRemain[i] != arrStock[i]).mapToObj(i -> arrRemain[i]).mapToInt(Integer::intValue).sum();
                listArn.add(new FastCutBean(numberMaterial, remain, arrIndexStockUsed));
                arrCheckMaterialCanBeCut[numberMaterialRemoved++] = 0;
                fastCutMain(listArn, arrCheckMaterialCanBeCut, numberMaterialRemoved, arrStock, arrOrder);
            }
        }
    }
}
