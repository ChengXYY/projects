package com.my.email.service.serviceimpl;

import com.my.common.CommonOperation;
import com.my.common.exception.JsonException;
import com.my.common.result.ErrorCodes;
import com.my.email.mapper.EmailMapper;
import com.my.email.mapper.EmailReadMapper;
import com.my.email.model.Email;
import com.my.email.model.EmailRead;
import com.my.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private EmailReadMapper readMapper;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Value("${smtp.sender}")
    private String smtpSender;

    @Override
    public int add(Email emil) {
        if(emil.getTitle().isEmpty() || emil.getReceiver().isEmpty())throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        String[] receiverArr = emil.getReceiver().split(";");
        int count = 0;
        for(int i=0; i<receiverArr.length; i++){
            if(CommonOperation.checkEmail(receiverArr[i])){
                send(emil.getTitle(), emil.getContent(), receiverArr[i], emil.getCode());
                count++;
            }
        }
        emailMapper.insertSelective(emil);
        return count;
    }

    @Override
    public int edit(Map<String, Object> email) {
        return 0;
    }

    @Override
    public int remove(Integer id) {
        return 0;
    }

    @Override
    public Email get(Integer id) {
        return null;
    }

    @Override
    public Email get(String code) {
        if(code.isEmpty())throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        return emailMapper.selectByCode(code);
    }

    @Override
    public List<Email> getList(Map<String, Object> filter) {
        return emailMapper.selectByFilter(filter);
    }

    @Override
    public int getCount(Map<String, Object> filter) {
        return emailMapper.countByFilter(filter);
    }

    @Override
    public void send(String title, String body, String receiver, String code) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(receiver);
            mimeMessageHelper.setFrom(smtpSender);
            mimeMessageHelper.setSubject(title);

            StringBuilder sb = new StringBuilder();
            sb.append("<html><head></head><body>");
            sb.append(body);
            sb.append("<img style=\"display:none\" src=\"http://10.72.41.14:8080/emailread?id="+code+"&email="+receiver+"\" />");
            sb.append("</body></html>");
            // 启用html
            mimeMessageHelper.setText(sb.toString(), true);
            // 发送邮件
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public int addRead(EmailRead read) {
        return readMapper.insertSelective(read);
    }
}
