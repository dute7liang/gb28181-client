package com.wydpp.controller;

import com.wydpp.controller.model.HiPotQueue;
import com.wydpp.controller.model.HiPotRequest;
import com.wydpp.controller.model.R;
import com.wydpp.gb28181.bean.SipPlatform;
import com.wydpp.model.SipDevice;
import com.wydpp.service.SipDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 压力测试专用
 */
@RestController
@RequestMapping("/video/gb28181/hi-pot")
@Slf4j
public class HiPotController {

    @Autowired
    private SipPlatform sipPlatform;
    @Autowired
    private SipDevice sipDevice;
    @Autowired
    private SipDeviceService sipDeviceService;

    private final static ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(5));

    @GetMapping("/run")
    public R<Boolean> run(HiPotRequest hiPotRequest){
        Integer runThread = hiPotRequest.getRunThread();
        LinkedBlockingDeque<HiPotQueue> queues = new LinkedBlockingDeque<>(hiPotRequest.getRunThread()+5);
        AtomicBoolean stopFlag = new AtomicBoolean(false);
        for (int i = 0; i < runThread; i++) {
            InviteHiPotThread thread = new InviteHiPotThread(queues,sipDeviceService,stopFlag);
            executor.execute(thread);
        }
        Integer deviceMaxNums = hiPotRequest.getDeviceNums();
        int deviceNum = 0;
        while (deviceNum < deviceMaxNums) {
            deviceNum++;
            HiPotQueue hiPotQueue = new HiPotQueue();
            SipDevice dumpSipDevice = new SipDevice();
            BeanUtils.copyProperties(sipDevice,dumpSipDevice);
            dumpSipDevice.setPort(sipDevice.getPort());
            String deviceId = sipDevice.getDeviceId();
            BigDecimal bigDecimal = new BigDecimal(deviceId);
            BigDecimal newValue = bigDecimal.add(BigDecimal.valueOf(deviceNum));
            dumpSipDevice.setDeviceId(newValue.toString());
            hiPotQueue.setSipDevice(dumpSipDevice);
            hiPotQueue.setSipPlatform(sipPlatform);
            try {
                sipDeviceService.uniqueSave(dumpSipDevice);
                queues.put(hiPotQueue);
            } catch (InterruptedException e) {
                log.error(e.getMessage(),e);
            }
        }
        stopFlag.set(true);
        return R.ok(true);
    }
}
