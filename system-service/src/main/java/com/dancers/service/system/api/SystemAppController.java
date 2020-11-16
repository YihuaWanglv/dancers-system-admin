package com.dancers.service.system.api;

import com.dancers.service.system.domain.SystemApp;
import com.dancers.service.system.domain.SystemAppRepository;
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

@Tag(name = "应用管理接口")
@Slf4j
@RestController
@RequestMapping("/apps")
public class SystemAppController {
    @Resource
    SystemAppRepository systemAppRepository;

    @Operation(summary = "新增", description = "新增",
            parameters = {
                    @Parameter(name = "token", description = "请求头token", in = ParameterIn.HEADER),
                    @Parameter(name = "app", description = "对象参数参数")
            },
            responses = {@ApiResponse(responseCode = "200", description = "接口请求正常")},
            security = @SecurityRequirement(name = "需要认证"))
    @PostMapping
    public ResponseEntity<?> create(SystemApp app) {
        app.fixDefaultField();
        systemAppRepository.save(app);
        return ResponseEntity.ok().body(app);
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
        SystemApp app = systemAppRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        app.setIsDeleted(1);
        systemAppRepository.save(app);
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
    public ResponseEntity<?> update(@PathVariable Long id, SystemApp app) {
        app.setAppId(id);
        SystemApp exist = systemAppRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        systemAppRepository.save(app);
        return ResponseEntity.ok().body(app);
    }

    @Operation(summary = "通过ID获取对象详情", description = "通过ID获取对象详情",
            parameters = {
                    @Parameter(name = "token", description = "请求头token", in = ParameterIn.HEADER),
                    @Parameter(name = "id", description = "id", in = ParameterIn.PATH),
            },
            responses = {@ApiResponse(responseCode = "200", description = "接口请求正常")},
            security = @SecurityRequirement(name = "需要认证"))
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        SystemApp app = systemAppRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.ok().body(app);
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
    public ResponseEntity<?> list(Pageable pageable, SystemApp app, Integer page) {
        log.info("pageable={}", pageable.toString());
        log.info("page={}", page);
        if (page != null && page > -1) {
            Page<SystemApp> list = systemAppRepository.findAll(Example.of(app), pageable);
            return ResponseEntity.ok().body(list);
        } else {
            List<SystemApp> list = systemAppRepository.findAll(Example.of(app));
            return ResponseEntity.ok().body(list);
        }
    }
}
