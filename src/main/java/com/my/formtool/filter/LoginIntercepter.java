package com.my.formtool.filter;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@Controller
@Component
public class LoginIntercepter extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception{
        String basePath = request.getContextPath();
        String path = request.getRequestURI();
        if(!doLoginInterceptor(path, basePath) ){//是否进行登陆拦截
            return true;
        }
        HttpSession session = request.getSession();
        if(session.getAttribute("ADMIN_SESSION") == null){
            String requestType = request.getHeader("X-Requested-With");

            if(requestType!=null && requestType.equals("XMLHttpRequest")){
                response.setHeader("sessionstatus","timeout");

                response.getWriter().print("LoginTimeout");
                return false;
            } else {
                //尚未登录，跳转到登录界面;
                response.sendRedirect("/login/admin");
            }
            return false;
        }
        return true;
    }
    private boolean doLoginInterceptor(String path,String basePath){
        path = path.substring(basePath.length());
        Set<String> notLoginPaths = new HashSet<>();

        notLoginPaths.add("/login/**");
        notLoginPaths.add("/error/**");

        if(notLoginPaths.contains(path)) return false;
        return true;
    }
}
