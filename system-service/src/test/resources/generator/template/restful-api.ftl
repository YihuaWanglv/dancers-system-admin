package ${basePackage}.api;

import ${basePackage}.domain.${modelNameUpperCamel};
import ${basePackage}.domain.${modelNameUpperCamel}Repository;
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

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
* Created by ${author} on ${date}.
*/
@Tag(name = "${modelNameUpperCamel}管理接口")
@Slf4j
@RestController
@RequestMapping("${baseRequestMapping}s")
public class ${modelNameUpperCamel}Controller {

    @Resource
    private ${modelNameUpperCamel}Repository ${modelNameLowerCamel}Repository;

    @Operation(summary = "新增", description = "新增",
        parameters = {
            @Parameter(name = "token", description = "请求头token", in = ParameterIn.HEADER),
            @Parameter(name = "app", description = "对象参数参数")
        },
        responses = {@ApiResponse(responseCode = "200", description = "接口请求正常")},
        security = @SecurityRequirement(name = "需要认证"))
    @PostMapping
    public ResponseEntity<?> create(${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Repository.save(${modelNameLowerCamel});
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
        ${modelNameUpperCamel} entity = ${modelNameLowerCamel}Repository.findById(id).orElseThrow(EntityNotFoundException::new);
        entity.setIsDeleted(1);
        ${modelNameLowerCamel}Repository.save(entity);
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
    public ResponseEntity<?> update(@PathVariable Long id, ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        // TODO set id
        ${modelNameUpperCamel} entity = ${modelNameLowerCamel}Repository.findById(id).orElseThrow(EntityNotFoundException::new);
        ${modelNameLowerCamel}Repository.save(${modelNameLowerCamel});
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
        ${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.ok().body(${modelNameLowerCamel});
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
    public ResponseEntity<?> list(Pageable pageable, Integer page, ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        if (page != null && page > -1) {
            Page<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Repository.findAll(Example.of(${modelNameLowerCamel}), pageable);
            return ResponseEntity.ok().body(list);
        } else {
            List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Repository.findAll(Example.of(${modelNameLowerCamel}));
            return ResponseEntity.ok().body(list);
        }
    }
}