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

import java.lang.reflect.Method;

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
    public Object isAccessMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Admin currentUser = new Admin();
        try {
            currentUser = adminService.getCurrentUser();
        }catch (JsonException e){
            return "/admin/login";
        }
        //获取访问目标方法
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        //得到方法的访问权限
        //final String methodAccess = AnnotationParse.privilegeParse(targetMethod);
        final String methodAccess = targetMethod.getAnnotation(Permission.class).value();

        //如果该方法上没有权限注解，直接调用目标方法
        if(StringUtils.isBlank(methodAccess)){
            return joinPoint.proceed();
        }else {
            if(currentUser.getAdmingroup().getAuth().contains(methodAccess)){
                return joinPoint.proceed();
            }else {
                return "/error/403";
            }
        }
    }
}
