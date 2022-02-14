package com.wydpp.gb28181.processor.request.impl.message;

import com.wydpp.model.SipDevice;
import com.wydpp.gb28181.bean.SipPlatform;
import com.wydpp.gb28181.processor.SIPProcessorObserver;
import com.wydpp.gb28181.processor.request.ISIPRequestProcessor;
import com.wydpp.gb28181.processor.request.SIPRequestProcessorParent;
import com.wydpp.service.SipDeviceService;
import com.wydpp.utils.SipUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageRequestProcessor extends SIPRequestProcessorParent implements InitializingBean, ISIPRequestProcessor {

    private final static Logger logger = LoggerFactory.getLogger(MessageRequestProcessor.class);

    private final String method = "MESSAGE";

    private static Map<String, IMessageHandler> messageHandlerMap = new ConcurrentHashMap<>();

    @Autowired
    private SIPProcessorObserver sipProcessorObserver;

    @Autowired
    private SipPlatform sipPlatform;

    @Autowired
    private SipDeviceService sipDeviceService;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 添加消息处理的订阅
        sipProcessorObserver.addRequestProcessor(method, this);
    }

    public void addHandler(String name, IMessageHandler handler) {
        messageHandlerMap.put(name, handler);
    }

    @Override
    public void process(RequestEvent evt) {
        logger.debug("接收到消息：" + evt.getRequest());
        String deviceId = SipUtils.getUserIdFromToHeader(evt.getRequest());
        try {
            SipDevice sipDevice = sipDeviceService.getByDeviceId(deviceId);
            if (sipDevice == null) {
                logger.error("设备id错误，返回404");
                responseAck(evt, Response.NOT_FOUND, "device id not found");
            } else {
                Element rootElement = getRootElement(evt);
                String name = rootElement.getName();
                IMessageHandler messageHandler = messageHandlerMap.get(name);
                if (messageHandler != null) {
                    messageHandler.handForPlatform(evt, sipPlatform, sipDevice, rootElement);
                } else {
                    // 不支持的message
                    // 不存在则回复415
                    responseAck(evt, Response.UNSUPPORTED_MEDIA_TYPE, "Unsupported message type, must Control/Notify/Query/Response");
                }
            }
        } catch (SipException e) {
            logger.warn("SIP 回复错误", e);
        } catch (InvalidArgumentException e) {
            logger.warn("参数无效", e);
        } catch (ParseException e) {
            logger.warn("SIP回复时解析异常", e);
        } catch (DocumentException e) {
            logger.warn("解析XML消息内容异常", e);
        }
    }


}
