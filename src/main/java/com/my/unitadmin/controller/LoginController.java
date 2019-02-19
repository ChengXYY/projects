package com.my.unitadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.my.common.exception.JsonException;
import com.my.unitadmin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    AdminService adminService;

    @Autowired
    private DefaultKaptcha captchaProducer;

    @RequestMapping("/admin")
    public String index(ModelMap model){
        model.addAttribute("pageTitle","登录 - 后台管理系统");
        return "admin/login";
    }

    @ResponseBody
    @RequestMapping(value = "/adminlogin", method = RequestMethod.POST)
    public JSONObject login(String account, String password, String vercode, HttpSession session){
        JSONObject result = new JSONObject();
        try {
            adminService.login(account, password, vercode, session);
            result.put("code", 1);
            result.put("msg", "登录成功");
            return result;
        }catch (JsonException e){
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
            return result;
        }
    }

    //生产验证码
    @ResponseBody
    @RequestMapping("/vercode")
    public void defaultKaptcha(HttpSession session, HttpServletResponse httpServletResponse) throws Exception{
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = captchaProducer.createText();
            session.setAttribute("VERCODE", createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = captchaProducer.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("ADMIN_SESSION");
        session.removeAttribute("ADMIN_ACCOUNT");
        session.removeAttribute("ADMIN_AUTH");
        return "redirect:admin";
    }
}
