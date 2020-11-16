package ${basePackage}.service;

import ${basePackage}.domain.${modelNameUpperCamel}Mapper;
import ${basePackage}.domain.${modelNameUpperCamel};
import org.springframework.stereotype.Service;
import ${basePackage}.core.AbstractService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by ${author} on ${date}.
 */
@Slf4j
@Service
@Transactional
public class ${modelNameUpperCamel}Service extends AbstractService<${modelNameUpperCamel}> {

    @Resource
    private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

    public void create(${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Mapper.insertSelective(${modelNameLowerCamel});
    }

    public void delete(Long id) {
        ${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Mapper.selectByPrimaryKey(id);
        if (${modelNameLowerCamel} != null && ${modelNameLowerCamel}.getIsDeleted() == 0) {
            ${modelNameLowerCamel}.setIsDeleted(1);
            ${modelNameLowerCamel}Mapper.updateByPrimaryKeySelective(${modelNameLowerCamel});
        }
    }

    public void update(${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Mapper.updateByPrimaryKeySelective(${modelNameLowerCamel});
    }

    public ${modelNameUpperCamel} detail(Long id) {
        ${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Mapper.selectByPrimaryKey(id);
        return ${modelNameLowerCamel};
    }

    public List<${modelNameUpperCamel}> list(${modelNameUpperCamel} ${modelNameLowerCamel}) {
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Mapper.select(${modelNameLowerCamel});
        return list;
    }
}
