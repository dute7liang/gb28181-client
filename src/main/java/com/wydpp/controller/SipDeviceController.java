package com.wydpp.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wydpp.controller.model.R;
import com.wydpp.keeplive.KeepLiveKit;
import com.wydpp.model.SipDevice;
import com.wydpp.service.SipDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/video/sip/device")
public class SipDeviceController {
    @Autowired
    private SipDeviceService sipDeviceService;
    @Autowired
    private KeepLiveKit keepLiveKit;

    @GetMapping("/page")
    public R<IPage<SipDevice>> page(Page page, SipDevice sipDevice){
        LambdaQueryWrapper<SipDevice> wrapper = Wrappers.lambdaQuery(sipDevice).orderByDesc(SipDevice::getRegisterTime);
        IPage<SipDevice> data = sipDeviceService.page(page, wrapper);
        return R.ok(data);
    }

    @GetMapping("/flushAll")
    public R<Void> flushAll(){
        sipDeviceService.remove(Wrappers.emptyWrapper());
        keepLiveKit.flushAll();
        return R.ok();
    }
}
