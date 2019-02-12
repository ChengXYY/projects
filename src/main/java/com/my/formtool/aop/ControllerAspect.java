package com.my.formtool.aop;

import com.my.formtool.exception.JsonException;
import com.my.formtool.model.Admin;
import com.my.formtool.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Aspect
@Component
public class ControllerAspect {

    private final static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Autowired
    private AdminService adminService;
    /**
     * 定义切点
     */
    @Pointcut("within(com.my.formtool.controller..*)"+
                "&& !within(com.my.formtool.controller.LoginController)")
    public void privilege(){}

    /**
     * 权限环绕通知
     * @param joinPoint
     * @throws Throwable
     */
    @ResponseBody
    @Around("privilege()")
    public Object isAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Admin currentUser = new Admin();
        try {
            currentUser = adminService.getCurrentUser();
        } catch (JsonException e) {
            return "/admin/login";
        }

        //获取访问目标方法
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();

        try {
            final String methodAccess = signature.getMethod().getAnnotation(Permission.class).value();
            if (!StringUtils.isBlank(methodAccess) && !currentUser.getAdmingroup().getAuth().contains(methodAccess)){
                return "/error/403";
            }
        }catch (NullPointerException e){
            logger.info("method access is null");
        }

        //获取访问类
        try {
            final String classAccess = joinPoint.getTarget().getClass().getAnnotation(Permission.class).value();
            if (!StringUtils.isBlank(classAccess) && !currentUser.getAdmingroup().getAuth().contains(classAccess)){
                return "/error/403";
            }
        }catch (NullPointerException e){
            logger.info("class access is null");
        }

        return joinPoint.proceed();
    }
}
