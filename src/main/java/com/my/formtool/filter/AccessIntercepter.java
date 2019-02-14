package com.my.formtool.filter;

import com.my.formtool.config.Permission;
import com.my.formtool.model.Admin;
import com.my.formtool.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@Controller
@Component
public class AccessIntercepter extends HandlerInterceptorAdapter {

    @Autowired
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String basePath = request.getContextPath();
        String path = request.getRequestURI();
        if(!doLoginInterceptor(path, basePath) ){//是否进行登陆拦截
            return true;
        }
        HttpSession session = request.getSession(); //登陆拦截
        if(session.getAttribute("ADMIN_ACCOUNT") == null){
            //尚未登录，跳转到登录界面;
            response.sendRedirect("/login/admin");
            return false;
        }
        // 验证权限
        if (this.hasPermission(handler, session)) {
            return true;
        }
        //  null == request.getHeader("x-requested-with") TODO 暂时用这个来判断是否为ajax请求
        // 如果没有权限 则抛403异常 springboot会处理，跳转到 /error/403 页面
        response.sendError(HttpStatus.FORBIDDEN.value(), "无权限");
        return false;
    }

    /**
     * 是否有权限
     *
     * @param handler
     * @return
     */
    private boolean hasPermission(Object handler, HttpSession session) {
        Admin admin = adminService.get(session.getAttribute("ADMIN_ACCOUNT").toString());

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上的注解
            Permission requiredPermission = handlerMethod.getMethod().getAnnotation(Permission.class);
            // 如果方法上的注解为空 则获取类的注解
            if (requiredPermission == null) {
                requiredPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Permission.class);
            }
            // 如果标记了注解，则判断权限
            if (requiredPermission != null && StringUtils.isNotBlank(requiredPermission.value())) {
                // redis或数据库 中获取该用户的权限信息 并判断是否有权限
                String permissionStr = admin.getAdmingroup().getAuth();
                if (permissionStr.isEmpty()){
                    return false;
                }
                return permissionStr.contains(requiredPermission.value());
            }
        }
        return true;
    }

    //登陆拦截
    private boolean doLoginInterceptor(String path,String basePath){
        path = path.substring(basePath.length());
        Set<String> notLoginPaths = new HashSet<>();

        notLoginPaths.add("/login/**");
        notLoginPaths.add("/error/**");
        notLoginPaths.add("/form/**");

        if(notLoginPaths.contains(path)) return false;
        return true;
    }
}
