package com.my.formtool.service.serviceimpl;

import com.my.formtool.common.CommonOperation;
import com.my.formtool.exception.ErrorCodes;
import com.my.formtool.exception.JsonException;
import com.my.formtool.mapper.AdminMapper;
import com.my.formtool.model.Admin;
import com.my.formtool.service.AdminService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int add(Admin admin){
        if(admin.getAccount().isEmpty() ||
                admin.getGroupid().toString().equals("0") ||
                admin.getPassword().isEmpty()) throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        String pwd = admin.getPassword();
        Map<String,Object>pwdArr = CommonOperation.encodeStr(pwd);
        admin.setSalt(pwdArr.get("salt").toString());
        admin.setPassword(pwdArr.get("newstr").toString());
        int rs = adminMapper.insertSelective(admin);
        if(rs>0)
            return rs;
        else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }

    @Override
    public int edit(Map<String, Object> admin) {
        if(admin.get("id")==null || !CommonOperation.checkId(Integer.parseInt(admin.get("id").toString())))throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public int remove(Integer id) {
        return adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Admin get(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public Admin get(String account) {
        if(account.isEmpty()) throw  JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        Admin admin = adminMapper.selectByAccount(account);
        if(admin == null) throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        return admin;
    }

    @Override
    public List<Admin> getList(Map<String, Object> filter) {
        return adminMapper.selectByFilter(filter);
    }

    @Override
    public int resetPassword(Integer id) {
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        Admin admin = get(id);
        if(admin == null) throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        Map<String,Object>pwdArr = CommonOperation.encodeStr("123456");
        Map<String, Object> adminNew = new HashMap<>();
        adminNew.put("id", id);
        adminNew.put("salt",pwdArr.get("salt").toString());
        adminNew.put("password",pwdArr.get("newstr").toString());
        int rs = edit(adminNew);
        if(rs>=0)
            return 1;
        else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }

    @Override
    public void login(String account, String password, String vercode, HttpSession session) {
        if(session.getAttribute("ADMIN_SESSION") != null) return;
        if(account.isEmpty() || password.isEmpty() || vercode.isEmpty()) throw  JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        //验证码
        if(session.getAttribute("VERCODE").toString().isEmpty()) throw JsonException.newInstance(ErrorCodes.SERVICE_ERROR);
        if(!session.getAttribute("VERCODE").toString().equals(vercode)) throw JsonException.newInstance(ErrorCodes.VERCODE_ERROR);
        Admin admin = get(account);
        String varifyPwd = CommonOperation.encodeStr(password, admin.getSalt());
        if(!varifyPwd.equals(admin.getPassword())) throw JsonException.newInstance(ErrorCodes.PASSWORD_ERROR);
        String sessionStr = CommonOperation.encodeStr(admin.getId().toString(), admin.getAccount());
        session.setAttribute("ADMIN_SESSION", sessionStr);
        session.setAttribute("ADMIN_ACCOUNT", admin.getAccount());
        session.setAttribute("ADMIN_AUTH", admin.getAdmingroup().getAuth());
    }

    @Override
    public void editPassword(String oldpwd, String newpwd, String repwd, HttpSession session) {
        if(oldpwd.isEmpty() || newpwd.isEmpty() || repwd.isEmpty())throw  JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        if(!newpwd.equals(repwd))throw JsonException.newInstance(ErrorCodes.PASSWORD_NOT_IDENTICAL);
        Admin admin = get(session.getAttribute("ADMIN_ACCOUNT").toString());
        if(admin == null)throw  JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        String varifyPwd = CommonOperation.encodeStr(oldpwd, admin.getSalt());
        if(!varifyPwd.equals(admin.getPassword())) throw JsonException.newInstance(ErrorCodes.PASSWORD_ERROR);
        Map<String, Object> pwdArr = CommonOperation.encodeStr(newpwd);
        Map<String, Object> adminNew = new HashMap<>();
        adminNew.put("id", admin.getId());
        adminNew.put("salt",pwdArr.get("salt").toString());
        adminNew.put("password",pwdArr.get("newstr").toString());
        int rs = edit(adminNew);
        if(rs<=0)
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }

    @Override
    public Admin getCurrentUser() {
        ServletRequestAttributes attributes =   (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session= request.getSession();
        if(session.getAttribute("ADMIN_ACCOUNT") == null) throw JsonException.newInstance(ErrorCodes.UN_LOGIN_ERROR);
        return get(session.getAttribute("ADMIN_ACCOUNT").toString());
    }
}
