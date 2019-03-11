package com.my.common.aop;

import com.my.common.CommonOperation;
import com.my.unitadmin.service.AdminlogService;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;

@Aspect
@Component
public class AdminLog {

    @Pointcut("within(com.my.unitadmin.controller.LoginController)")
    public void loginLog(){}

    @Pointcut("execution(public * com.my.*.service.*.*(..))"+
            "&& !execution(public * com.my.unitadmin.service.AdminlogService.*(..))")
    public void operationLog(){}

    @Autowired
    private AdminlogService adminlogService;

    @Around("loginLog()")
    public Object addloginlog(ProceedingJoinPoint joinPoint)throws Throwable{
        Object result = joinPoint.proceed();
        //获取访问信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ipAddr = request.getRemoteAddr();
        HttpSession session = ((HttpServletRequest) request).getSession();
        if(session.getAttribute("ADMIN_ACCOUNT")==null){
            return result;
        }
        //获取访问目标方法
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        String methodName = signature.getName();
        switch (methodName){
            case "login": //登录
                adminlogService.add(session.getAttribute("ADMIN_ACCOUNT").toString(), "管理员登录("+ipAddr+")");
                break;
            case "logout": //登出
                if(session.getAttribute("ADMIN_ACCOUNT")!=null)
                    adminlogService.add(session.getAttribute("ADMIN_ACCOUNT").toString(), "管理员退出登录");
                break;
            default:
                break;
        }
        return result;
    }

    @Around("operationLog()")
    public Object addoplog(ProceedingJoinPoint joinPoint)throws Throwable{
        Object result = joinPoint.proceed();

        //获取访问信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = ((HttpServletRequest) request).getSession();
        //获取访问目标方法
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        String methodName = signature.getName();
        String className = joinPoint.getTarget().getClass().getName().replace("ServiceImpl", "");
        String modelName = className.replace("service.serviceimpl", "model");
        className = className.substring(className.indexOf("serviceimpl")+12, className.length());

        //获取参数
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();

        String param = "";
        String checkMethodStr = "add,edit,remove";
        if(checkMethodStr.contains(methodName) && (result==null || !CommonOperation.checkId(Integer.parseInt(result.toString())))){
            return result;
        }
        switch (methodName){
            case "add":
                Field[] fields = Class.forName(modelName).getDeclaredFields();
                for(Field f :fields){
                    f.setAccessible(true);System.out.println(f.getName());
                    if(f.getName()=="name"){
                        param = f.get(args[0]).toString();
                    }
                }
                adminlogService.add(session.getAttribute("ADMIN_ACCOUNT").toString(), "添加【"+className+"】记录("+param+")");
                break;
            case "edit":
                param = getParamValue("id", args, parameterNames);
                adminlogService.add(session.getAttribute("ADMIN_ACCOUNT").toString(), "修改【"+className+"】记录("+param+")");
                break;
            case "remove":
                param = getParamValue("id", args, parameterNames);
                adminlogService.add(session.getAttribute("ADMIN_ACCOUNT").toString(), "删除【"+className+"】记录("+param+")");
                break;
        }
        return result;
    }

    private String getParamValue(String paramName, Object[] args, String[] paramNames){

        int index = ArrayUtils.indexOf(paramNames, paramName);
        if(index >-1){
           return args[index].toString();
        }
        return null;
    }
}
