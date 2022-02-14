package com.wydpp.controller;

import com.wydpp.controller.model.HiPotQueue;
import com.wydpp.service.SipDeviceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@AllArgsConstructor
public class InviteHiPotThread implements Runnable {

    private final LinkedBlockingDeque<HiPotQueue> queues;
    private final SipDeviceService sipDeviceService;
    private final AtomicBoolean stop;

    @Override
    public void run() {
        while (!stop.get()) {
            HiPotQueue hiPotQueue;
            try {
                hiPotQueue = queues.poll(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e.getMessage(), e);
                return;
            }
            if(hiPotQueue == null){
                continue;
            }
            boolean sendResult = sipDeviceService.register(hiPotQueue, eventResult -> {
                long time = System.currentTimeMillis();
                hiPotQueue.getSipDevice().setRegisterTime(time);
                hiPotQueue.getSipDevice().setKeepaliveTime(time);
                hiPotQueue.getSipDevice().setOnline(true);
                sipDeviceService.updateDevice(hiPotQueue.getSipDevice());
                log.info("注册成功!");
            });
            if (!sendResult) {
                log.info("注册指令发送失败!");
            }
        }
        log.warn("退出成功!已全部处理完成!");
    }
}
