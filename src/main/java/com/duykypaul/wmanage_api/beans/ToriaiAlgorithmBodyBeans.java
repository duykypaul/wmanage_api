package com.duykypaul.wmanage_api.beans;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
@ToString
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ToriaiAlgorithmBodyBeans {
    /**
     * Do dai thanh thep
     */
    private Integer steelLength;

    /**
     * So lan cat cua thanh thep tuong ung
     */
    private Integer cutCounting;
    private MaterialBean materialBean;

    /**
     * value: so lan cat voi do dai tuong ung
     * key: do dai thanh sat can cat
     */
    Map<String, Integer> detailCutting = new HashMap<>();
}
