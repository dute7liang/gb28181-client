package com.wydpp.controller.model;

import com.wydpp.model.SipDevice;
import com.wydpp.gb28181.bean.SipPlatform;
import lombok.Data;

@Data
public class HiPotQueue {

    private SipPlatform sipPlatform;
    private SipDevice sipDevice;
}
