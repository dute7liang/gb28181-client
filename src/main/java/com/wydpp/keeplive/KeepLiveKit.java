package com.wydpp.keeplive;

import com.wydpp.gb28181.bean.SipPlatform;
import com.wydpp.gb28181.commander.SIPCommander;
import com.wydpp.model.SipDevice;
import com.wydpp.service.SipDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

@Component
@Slf4j
public class KeepLiveKit {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SipDeviceService sipDeviceService;
    @Autowired
    private SipPlatform sipPlatform;
    @Autowired
    private SIPCommander sipCommander;


    public final static String KEEP_LIVE_REDIS_KEY = "KEEP_LIVE_%s";
    public final static String KEEP_LIVE_REDIS_KEY_STARTS = "KEEP_LIVE_";
    public final static String KEEP_LIVE_REDIS_KEY_MATCH = "KEEP_LIVE_*";

    public String getRedisKey(String deviceId){
        return String.format(KEEP_LIVE_REDIS_KEY,deviceId);
    }

    public boolean checkRedisKey(String redisKey){
        return redisKey.startsWith(KEEP_LIVE_REDIS_KEY_STARTS);
    }

    public void flushAll(){
        Set<String> keys = redisTemplate.keys(KEEP_LIVE_REDIS_KEY_MATCH);
        redisTemplate.delete(keys);
    }

    public void add(SipDevice sipDevice){
        redisTemplate.opsForValue().set(getRedisKey(sipDevice.getDeviceId()),sipDevice, Duration.ofMinutes(1));
    }

    public void add(String deviceIdRedisKey){
        String deviceId = deviceIdRedisKey.replaceAll(KEEP_LIVE_REDIS_KEY_STARTS, "");
        SipDevice sipDevice = sipDeviceService.getByDeviceId(deviceId);
        if(sipDevice != null){
            // 发送心跳消息
            if (sipDevice.getOnline() && sipDevice.getKeepaliveTime() != null) {
                long expires = sipPlatform.getKeepTimeout() * 1000;
                long keepaliveTime = sipDevice.getKeepaliveTime();
                long nowTime = System.currentTimeMillis();
                if (nowTime - keepaliveTime >= expires) {
                    log.info("发送心跳 deviceId={}",deviceId);
                    sipCommander.keepalive(sipPlatform, sipDevice,eventResult->{
                        sipDevice.setKeepaliveTime(nowTime);
                        sipDeviceService.updateDevice(sipDevice);
                    });
                }
            }
            this.add(sipDevice);
        }
    }

}
