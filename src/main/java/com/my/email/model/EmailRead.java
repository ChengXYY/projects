package com.my.email.model;

import java.sql.Timestamp;
import java.util.Date;

public class EmailRead {
    private Integer id;

    private String code;

    private String receiver;

    private String ip;

    private String os;

    private String osversion;

    private String browser;

    private Timestamp opentime=  new Timestamp(System.currentTimeMillis());

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os == null ? null : os.trim();
    }

    public String getOsversion() {
        return osversion;
    }

    public void setOsversion(String osversion) {
        this.osversion = osversion == null ? null : osversion.trim();
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser == null ? null : browser.trim();
    }

    public Timestamp getOpentime() {
        return opentime;
    }

    public void setOpentime(Timestamp opentime) {
        this.opentime = opentime;
    }
}