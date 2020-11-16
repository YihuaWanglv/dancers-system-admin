package ${basePackage}.api;


import ${basePackage}.domain.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import io.swagger.annotations.*;
import javax.annotation.Resource;
import java.util.List;

/**
* Created by ${author} on ${date}.
*/
@Api(value="${modelNameUpperCamel}",tags={"${modelNameUpperCamel}"})
@RestController
@RequestMapping("/v1${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller {

    @Resource
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @ApiOperation(value = "新增", httpMethod = "POST", notes = "新增")
    @PostMapping
    public ResponseEntity<?> create(${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.create(${modelNameLowerCamel});
        return ResponseEntity.ok().body(true);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ${modelNameLowerCamel}Service.delete(id);
        return ResponseEntity.ok().body(true);
    }

    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.update(${modelNameLowerCamel});
        return ResponseEntity.ok().body(true);
    }

    @ApiOperation(value = "通过ID获取对象详情")
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        ${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.detail(id);
        return ResponseEntity.ok().body(${modelNameLowerCamel});
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "size", value = "页大小", required = false, dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "sortType", value = "排序依据", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "sortBy", value = "升降序", required = false, dataType = "String", paramType = "query")
        })
    @ApiOperation(value = "列表查询（可分页和不分页）", httpMethod = "GET", notes = "page为分页标记，page>0表示要获取分页数据")
    @GetMapping
    public ResponseEntity<?> list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size
            , ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        if (page != null && page > 0) {
            PageHelper.startPage(page, size);
            List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.list(${modelNameLowerCamel});
            PageInfo<${modelNameUpperCamel}> pageInfo = new PageInfo<${modelNameUpperCamel}>(list);
            return ResponseEntity.ok().body(pageInfo);
        } else {
            List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.list(${modelNameLowerCamel});
            return ResponseEntity.ok().body(list);
        }

    }
}
