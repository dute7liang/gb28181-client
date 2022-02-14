package com.wydpp.gb28181.processor.response.impl;

import com.wydpp.cache.HiPotCache;
import com.wydpp.controller.model.HiPotQueue;
import com.wydpp.gb28181.bean.SipPlatform;
import com.wydpp.gb28181.commander.SIPCommander;
import com.wydpp.gb28181.event.SipSubscribe;
import com.wydpp.gb28181.processor.SIPProcessorObserver;
import com.wydpp.gb28181.processor.response.SIPResponseProcessorAbstract;
import com.wydpp.keeplive.KeepLiveKit;
import com.wydpp.model.SipDevice;
import com.wydpp.service.SipDeviceService;
import com.wydpp.utils.SipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sip.ResponseEvent;
import javax.sip.header.CallIdHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Response;

/**
 * Register响应处理器
 * @author wydpp
 * @date 2021年12月2日
 */
@Component
@Slf4j
public class RegisterResponseProcessor extends SIPResponseProcessorAbstract {

    private final static String method = "REGISTER";

    @Autowired
    private SIPProcessorObserver sipProcessorObserver;

    @Autowired
    private SIPCommander sipCommander;

    @Autowired
    private SipPlatform sipPlatform;

    @Autowired
    private SipSubscribe sipSubscribe;
    @Autowired
    private SipDeviceService sipDeviceService;
    @Autowired
    private KeepLiveKit keepLiveKit;

    @Override
    public void afterPropertiesSet() throws Exception {
        sipProcessorObserver.addResponseProcessor(method, this);
    }

    /**
     * 处理Register响应
     *
     * @param evt 事件
     */
    @Override
    public void process(ResponseEvent evt) {
        Response response = evt.getResponse();
        String deviceId = SipUtils.getUserIdFromFromHeader(response);
        SipDevice sipDevice = sipDeviceService.getByDeviceId(deviceId);
        if(sipDevice == null){
            log.error("sipDevice为空！deviceId={}",deviceId);
            return;
        }
        CallIdHeader callIdHeader = (CallIdHeader) response.getHeader(CallIdHeader.NAME);
        String callId = callIdHeader.getCallId();
        int statusCode = response.getStatusCode();
        SipPlatform dumpSipPlatForm = sipPlatform;
        String platformInfo = dumpSipPlatForm.getServerIP() + ":" + dumpSipPlatForm.getServerPort();
        if (statusCode == 401) {
            //携带验证信息
            WWWAuthenticateHeader authorizationHeader = (WWWAuthenticateHeader) response.getHeader(WWWAuthenticateHeader.NAME);
            if (sipDevice.getNeedRegister()) {
                log.info("向平台:{} 发送带认证信息的注册消息!", platformInfo);
                sipCommander.register(dumpSipPlatForm, sipDevice, callId, authorizationHeader, null);
            } else {
                log.info("向平台:{} 发送带认证信息的注销消息!", platformInfo);
                sipCommander.unRegister(dumpSipPlatForm, sipDevice, callId, authorizationHeader, null);
            }
        } else if (statusCode == 200) {
            if (sipDevice.getNeedRegister()){
                // 注册成功
                keepLiveKit.add(sipDevice);
                log.info("设备向平台:{} 注册成功!", platformInfo);
            }else {
                log.info("设备向平台:{} 注销成功!", platformInfo);
            }
        } else {
            log.info("设备向平台:{} 注册失败!", platformInfo);
            sipDevice.setOnline(false);
            sipDevice.setNeedRegister(false);
            sipDeviceService.updateDevice(sipDevice);
        }
    }


}
