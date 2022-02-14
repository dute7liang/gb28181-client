package com.wydpp.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wydpp.controller.model.HiPotQueue;
import com.wydpp.gb28181.bean.SipPlatform;
import com.wydpp.gb28181.commander.SIPRequestHeaderPlatformProvider;
import com.wydpp.gb28181.event.SipSubscribe;
import com.wydpp.mapper.SipDeviceMapper;
import com.wydpp.model.SipDevice;
import com.wydpp.service.SipDeviceService;
import com.wydpp.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;

@Service
@Slf4j
public class SipDeviceServiceImpl extends ServiceImpl<SipDeviceMapper, SipDevice> implements SipDeviceService {
    @Override
    public SipDevice getByDeviceId(String deviceId) {
        return this.getOne(Wrappers.lambdaQuery(SipDevice.class).eq(SipDevice::getDeviceId,deviceId));
    }

    @Override
    public void uniqueSave(SipDevice sipDevice) {
        this.remove(Wrappers.lambdaQuery(SipDevice.class).eq(SipDevice::getDeviceId,sipDevice.getDeviceId()));
        this.save(sipDevice);
    }

    @Override
    public void updateDevice(SipDevice sipDevice){
        if(sipDevice.getId() == null){
            log.error("sipDevice更新失败 id为空");
        }
        sipDevice.setUpdateTime(DateUtil.getDateStr());
        this.updateById(sipDevice);
    }

    @Autowired
    @Lazy
    private SipProvider udpSipProvider;
    @Autowired
    private SIPRequestHeaderPlatformProvider headerProviderPlatformProvider;
    @Autowired
    private SipSubscribe sipSubscribe;

    @Override
    public boolean register(HiPotQueue take, SipSubscribe.Event okEvent) {
        SipDevice sipDevice = take.getSipDevice();
        SipPlatform sipPlatform = take.getSipPlatform();
        String tm = Long.toString(System.currentTimeMillis());
        Request request = null;
        CallIdHeader callIdHeader = udpSipProvider.getNewCallId();
        try {
            request = headerProviderPlatformProvider.createRegisterRequest(sipPlatform, sipDevice, 1L, "FromRegister" + tm, null, callIdHeader);
        } catch (Exception e) {
            log.error("createRegisterRequest error!", e);
        }
        if (request != null) {
            if (okEvent != null) {
                sipSubscribe.addOkSubscribe(callIdHeader.getCallId(), okEvent);
            }
            try {
                log.info("要发送的注册消息:deviceId={}", sipDevice.getDeviceId());
                udpSipProvider.sendRequest(request);
                return true;
            } catch (SipException e) {
                log.error("sendRequest error!", e);
            }
        }
        return false;
    }
}
