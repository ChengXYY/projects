package com.my.email.service;

import com.my.email.model.Email;
import com.my.email.model.EmailRead;

import java.util.List;
import java.util.Map;

public interface EmailService {
    //add
    int add(Email emil);
    //edit
    int edit(Map<String, Object> email);
    //remove
    int remove(Integer id);
    //get one
    Email get(Integer id);
    //get one
    Email get(String code);
    //get list
    List<Email> getList(Map<String, Object>filter);
    //get count
    int getCount(Map<String, Object> filter);
    //send email
    void send(String title, String body, String receiver, String code);

    //email read------------
    //add
    int addRead(EmailRead read);

}
