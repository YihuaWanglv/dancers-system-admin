package com.dancers.service.system.interceptor;

import com.dancers.service.system.core.ErrorResult;
import com.dancers.service.system.service.SystemUserAuthService;
import com.dancers.service.system.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求token校验过滤器
 */
@Component
public class TokenAuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthInterceptor.class);


    @Value("${application.token-on}")
    private boolean tokenOn = false;
    @Resource
    private SystemUserAuthService systemUserAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("--->>>TokenAuthInterceptor");
        String method = request.getMethod();
        String uri = request.getRequestURI();
        logger.info("检查请求: {} {}", method, uri);
        logger.info("tokenOn= {}", tokenOn);
        if (!tokenOn) {// 可以通过配置动态控制此开关字段，以此来控制是否打开token验证
            return true;
        }
        if (method != null && method.equalsIgnoreCase("OPTIONS")) {
            return true;
        }
        String token = request.getHeader("token");
        logger.info("token检查uri=" + uri);
        boolean isAuth = systemUserAuthService.valid(token);
        if (!isAuth) {
            logger.info("发现没有授权的uri请求,uri=" + uri);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JsonUtils.toJSON(new ErrorResult().setCode(HttpStatus.UNAUTHORIZED.value()).setMessage("UNAUTHORIZED")));
            return false;
        }
        return true;
    }
}