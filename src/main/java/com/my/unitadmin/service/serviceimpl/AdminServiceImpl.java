package com.my.unitadmin.service.serviceimpl;

import com.my.common.CommonOperation;
import com.my.common.result.AuthCode;
import com.my.common.result.ErrorCodes;
import com.my.common.exception.JsonException;
import com.my.unitadmin.mapper.AdminMapper;
import com.my.unitadmin.model.Admin;
import com.my.unitadmin.service.AdminService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${admin.session}")
    private String adminSession;

    @Value("${admin.account}")
    private String adminAccount;

    @Value("${admin.group}")
    private String adminGroup;

    @Value("${admin.id}")
    private String adminId;

    @Value("${admin.auth}")
    private String adminAuth;

    @Value("${login.vercode}")
    private String verCode;

    @Override
    public int add(Admin admin){
        if(admin.getAccount().isEmpty() ||
                admin.getGroupid().toString().equals("0") ||
                admin.getPassword().isEmpty()) throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        if(admin.getName()== null || admin.getName().isEmpty()){
            admin.setName(admin.getAccount());
        }
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
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        return adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Admin get(Integer id) {
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_ILLEGAL);
        Admin admin = adminMapper.selectByPrimaryKey(id);
        if(admin == null)throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        return admin;
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
        if(session.getAttribute(adminSession) != null) return;
        if(account.isEmpty() || password.isEmpty() || vercode.isEmpty()) throw  JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        //验证码
        if(session.getAttribute(verCode).toString().isEmpty()) throw JsonException.newInstance(ErrorCodes.SERVICE_ERROR);
        if(!session.getAttribute(verCode).toString().equals(vercode)) throw JsonException.newInstance(ErrorCodes.VERCODE_ERROR);
        if(isSystem(account, password, session)) return;

        Admin admin = get(account);
        String varifyPwd = CommonOperation.encodeStr(password, admin.getSalt());
        if(!varifyPwd.equals(admin.getPassword())) throw JsonException.newInstance(ErrorCodes.PASSWORD_ERROR);
        String sessionStr = CommonOperation.encodeStr(admin.getId().toString(), admin.getAccount());
        session.setAttribute(adminSession, sessionStr);
        session.setAttribute(adminAccount, admin.getAccount());
        session.setAttribute(adminAuth, admin.getAdmingroup().getAuth());
        session.setAttribute(adminGroup, admin.getAdmingroup().getId());
        session.setAttribute(adminId, admin.getId());
    }

    @Value("${system.account}")
    String sysAccount;
    @Value("${system.password}")
    String sysPassword;

    private Boolean isSystem(String account, String password, HttpSession session){
        if(account.equals(sysAccount) && password.equals(sysPassword)){
            String sessionStr = CommonOperation.encodeStr("0", account);
            session.setAttribute(adminSession, sessionStr);
            session.setAttribute(adminAccount, account);
            String auth = AuthCode.getAuthString();
            session.setAttribute(adminAuth, auth);
            session.setAttribute(adminGroup, "0");
            session.setAttribute(adminId, "0");
            return true;
        }
        return  false;
    }

    @Override
    public void editPassword(String oldpwd, String newpwd, String repwd, HttpSession session) {
        if(oldpwd.isEmpty() || newpwd.isEmpty() || repwd.isEmpty())throw  JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        if(!newpwd.equals(repwd))throw JsonException.newInstance(ErrorCodes.PASSWORD_NOT_IDENTICAL);
        Admin admin = get(session.getAttribute(adminAccount).toString());
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
        if(session.getAttribute(adminAccount) == null) throw JsonException.newInstance(ErrorCodes.UN_LOGIN_ERROR);
        return get(session.getAttribute(adminAccount).toString());
    }
}
