package com.wydpp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wydpp.controller.model.HiPotQueue;
import com.wydpp.gb28181.event.SipSubscribe;
import com.wydpp.model.SipDevice;

public interface SipDeviceService extends IService<SipDevice> {

    SipDevice getByDeviceId(String deviceId);

    void uniqueSave(SipDevice sipDevice);

    void updateDevice(SipDevice sipDevice);

    boolean register(HiPotQueue take, SipSubscribe.Event okEvent);
}
