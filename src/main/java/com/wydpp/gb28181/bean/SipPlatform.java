package com.wydpp.gb28181.bean;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SipPlatform {

    /**
     * id
     */
    private Integer id;

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 名称
     */
    private String name;

    /**
     * SIP服务国标编码
     */
    private String serverGBId;

    /**
     * SIP服务国标域
     */
    private String serverGBDomain;

    /**
     * SIP服务IP
     */
    private String serverIP;

    /**
     * SIP服务端口
     */
    private int serverPort;

//    /**
//     * 设备国标编号
//     */
//    private String deviceGBId;
//
//    /**
//     * 设备ip
//     */
//    private String deviceIp;

    /**
     * 设备端口
     */
    private String devicePort;

    /**
     * SIP认证用户名(默认使用设备国标编号)
     */
    private String username;

    /**
     * SIP认证密码
     */
    private String password;

    /**
     * 注册周期 (秒)
     */
    private String expires;

    /**
     * 心跳周期(秒)
     */
    private Integer keepTimeout;

    /**
     * 传输协议
     * UDP/TCP
     */
    private String transport;

    /**
     * 字符集
     */
    private String characterSet;

    /**
     * 允许云台控制
     */
    private boolean ptz;

    /**
     * 在线状态
     */
    private boolean status;

    /**
     * 在线状态
     */
    private int channelCount;

}
