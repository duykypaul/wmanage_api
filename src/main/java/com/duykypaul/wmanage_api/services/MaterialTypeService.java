package com.duykypaul.wmanage_api.services;


import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MaterialTypeService {
    ResponseEntity<?> findDistinctMaterialTypeAndAndMaterialTypeName();

    ResponseEntity<List<String>> findDistinctDimension();
}
