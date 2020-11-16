package com.dancers.service.system.api;

import com.dancers.service.system.core.Keys;
import com.dancers.service.system.dto.DictDataDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CommonController {

    @GetMapping(value = "/keys")
    public ResponseEntity<?> fetchKeys(@RequestParam("type") String type, String returnType) {
        Map<String, String> map = new HashMap<>();
        if (type.equals("MENU_TYPE")) {
            map = Keys.MenuType.toNameAliasMap();
        }
        if (type.equals("ORG_TYPE")) {
            map = Keys.OrgType.toNameAliasMap();
        }
        if (StringUtils.isNotEmpty(returnType) && returnType.equals("MAP")) {
            return ResponseEntity.ok().body(map);
        }
        List<DictDataDTO> list = map.entrySet().stream()
                .map(e -> new DictDataDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(list);
    }
}
