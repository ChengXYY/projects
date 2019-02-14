package com.my.formtool.aop;

import com.my.formtool.model.Admin;
import com.my.formtool.service.AdminService;
import com.my.formtool.service.AdminlogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Aspect
@Component
public class AdminLog {
    @Pointcut("within(com.my.formtool.controller.LoginController)")
    public void loginLog(){}

    @Pointcut("execution(public * com.my.formtool.service.*.*(..))"+
            "&& !execution(public * com.my.formtool.service.AdminlogService.*(..))")
    public void operationLog(){}

    @Autowired
    private AdminlogService adminlogService;

    @Around("loginLog()")
    public Object addloginlog(ProceedingJoinPoint joinPoint)throws Throwable{
        //获取访问信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = ((HttpServletRequest) request).getSession();
        //获取访问目标方法
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        String methodName = signature.getName();
        switch (methodName){
            case "login": //登录
                adminlogService.add(request.getParameter("account"), "管理员登录");
                break;
            case "logout": //登出
                if(session.getAttribute("ADMIN_ACCOUNT")!=null)
                    adminlogService.add(session.getAttribute("ADMIN_ACCOUNT").toString(), "管理员退出登录");
                break;
            default:
                break;
        }
        return joinPoint.proceed();
    }

    @Around("operationLog()")
    public Object addoplog(ProceedingJoinPoint joinPoint)throws Throwable{
        //获取访问信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = ((HttpServletRequest) request).getSession();
        //获取访问目标方法
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        String methodName = signature.getName();
        String className = joinPoint.getTarget().getClass().getName().replace("ServiceImpl", "");
        className = className.replace("com.my.formtool.service.serviceimpl.","");
        String param = "";
        switch (methodName){
            case "add":
                if(className.equals("Admin")){
                    param = request.getParameter("account");
                }else{
                    param = request.getParameter("name");
                }
                adminlogService.add(session.getAttribute("ADMIN_ACCOUNT").toString(), "添加【"+className+"】记录("+param+")");
                break;
            case "edit":
                param = request.getParameter("id");
                adminlogService.add(session.getAttribute("ADMIN_ACCOUNT").toString(), "修改【"+className+"】记录("+param+")");
                break;
            case "remove":
                param = request.getParameter("id");
                adminlogService.add(session.getAttribute("ADMIN_ACCOUNT").toString(), "删除【"+className+"】记录("+param+")");
                break;
        }
        return joinPoint.proceed();
    }

    private static HashMap<String, Class> map = new HashMap<String, Class>() {
        {
            put("java.lang.Integer", int.class);
            put("java.lang.Double", double.class);
            put("java.lang.Float", float.class);
            put("java.lang.Long", long.class);
            put("java.lang.Short", short.class);
            put("java.lang.Boolean", boolean.class);
            put("java.lang.Char", char.class);
            put("com.my.formtool.model.Admin", Admin.class);
        }
    };

}
