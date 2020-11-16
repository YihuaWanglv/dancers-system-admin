package com.dancers.service.system.core;

import com.dancers.service.system.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * spring boot validation 对 Bean的字段进行注解式的校验
 */
public class BeanValidationHelper {

    Logger log = LoggerFactory.getLogger(getClass().getName());

    private static BeanValidationHelper beanValidationHelper = new BeanValidationHelper();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    private BeanValidationHelper() {
        super();
    }

    public static BeanValidationHelper getInstance() {
        return beanValidationHelper;
    }

    public <T> void valid(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        String error = null;
        for (ConstraintViolation<T> violation : violations) {
            log.error(violation.getMessage());
            if (error == null)
                error = violation.getMessage();
            else
                error += ";" + violation.getMessage();
        }
        if (error != null)
            throw new BusinessException("数据格式有误：" + error);
    }
}
