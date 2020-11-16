package com.dancers.service.system.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.dancers.service.system.core.tree.TreeBuilder;
import com.dancers.service.system.domain.SystemOrg;
import com.dancers.service.system.domain.SystemOrgRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Created by CodeGenerator on 2020-11-06.
 */
@Tag(name = "机构管理接口")
@Slf4j
@RestController
@RequestMapping("/orgs")
public class SystemOrgController {

    @Resource
    private SystemOrgRepository systemOrgRepository;

    @Operation(summary = "新增", description = "新增",
            parameters = {
                    @Parameter(name = "token", description = "请求头token", in = ParameterIn.HEADER),
                    @Parameter(name = "app", description = "对象参数参数")
            },
            responses = {@ApiResponse(responseCode = "200", description = "接口请求正常")},
            security = @SecurityRequirement(name = "需要认证"))
    @PostMapping
    public ResponseEntity<?> create(SystemOrg systemOrg) {
        systemOrg.fixDefaultField();
        systemOrgRepository.save(systemOrg);
        return ResponseEntity.ok().body(true);
    }

    @Operation(summary = "删除", description = "删除",
            parameters = {
                    @Parameter(name = "token", description = "请求头token", in = ParameterIn.HEADER),
                    @Parameter(name = "id", description = "id", in = ParameterIn.PATH)
            },
            responses = {@ApiResponse(responseCode = "200", description = "接口请求正常")},
            security = @SecurityRequirement(name = "需要认证"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        SystemOrg entity = systemOrgRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        entity.setIsDeleted(1);
        systemOrgRepository.save(entity);
        return ResponseEntity.ok().body(true);
    }

    @Operation(summary = "更新", description = "更新",
            parameters = {
                    @Parameter(name = "token", description = "请求头token", in = ParameterIn.HEADER),
                    @Parameter(name = "id", description = "id", in = ParameterIn.PATH),
                    @Parameter(name = "app", description = "对象参数参数")
            },
            responses = {@ApiResponse(responseCode = "200", description = "接口请求正常")},
            security = @SecurityRequirement(name = "需要认证"))
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, SystemOrg systemOrg) {
        systemOrg.setOrgId(id);
        SystemOrg entity = systemOrgRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        BeanUtil.copyProperties(systemOrg, entity, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        systemOrgRepository.save(entity);
        return ResponseEntity.ok().body(true);
    }

    @Operation(summary = "通过ID获取对象详情", description = "通过ID获取对象详情",
            parameters = {
                    @Parameter(name = "token", description = "请求头token", in = ParameterIn.HEADER),
                    @Parameter(name = "id", description = "id", in = ParameterIn.PATH)
            },
            responses = {@ApiResponse(responseCode = "200", description = "接口请求正常")},
            security = @SecurityRequirement(name = "需要认证"))
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        SystemOrg systemOrg = systemOrgRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.ok().body(systemOrg);
    }

    @Operation(summary = "对象列表，分页对象列表", description = "对象列表，如果需要分页，则传入page>-1",
            parameters = {
                    @Parameter(name = "token", description = "请求头token", in = ParameterIn.HEADER),
                    @Parameter(name = "page", description = "page"),
                    @Parameter(name = "size", description = "size")
            },
            responses = {@ApiResponse(responseCode = "200", description = "接口请求正常")},
            security = @SecurityRequirement(name = "需要认证"))
    @GetMapping
    public ResponseEntity<?> list(Pageable pageable, Integer page, SystemOrg systemOrg, boolean fetchTree) {
        if (page != null && page > -1) {
            Page<SystemOrg> list = systemOrgRepository.findAll(Example.of(systemOrg), pageable);
            if (list.hasContent())
                list.forEach(SystemOrg::fixNameFields);
            return ResponseEntity.ok().body(list);
        } else {
            List<SystemOrg> list = systemOrgRepository.findAll(Example.of(systemOrg));
            list.forEach(SystemOrg::fixNameFields);
            if (fetchTree) {
                list = new TreeBuilder<Long, SystemOrg>().toTree(list);
            }
            return ResponseEntity.ok().body(list);
        }
    }
}