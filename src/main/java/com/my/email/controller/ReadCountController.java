package com.my.email.controller;

import com.my.common.CommonOperation;
import com.my.email.model.EmailRead;
import com.my.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/emailread")
public class ReadCountController {

    @Autowired
    private EmailService emailService;

    @RequestMapping("")
    public void getRead(@RequestParam(value = "code", required = true, defaultValue = "")String code,
                        @RequestParam(value = "email", required = true, defaultValue = "")String emailAddr,
                        HttpServletRequest request){
        if(!CommonOperation.checkId(0) || !CommonOperation.checkEmail(emailAddr))return;
        EmailRead read = new EmailRead();
        read.setCode(code);
        read.setReceiver(emailAddr);
        read.setBrowser(request.getHeader("user-agen"));
        read.setOs(System.getProperty("os.name"));
        read.setOsversion(System.getProperty("os.version"));
        emailService.addRead(read);
    }
}
