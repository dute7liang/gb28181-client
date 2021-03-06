package com.wydpp.gb28181;

import com.wydpp.config.SipDeviceConfig;
import com.wydpp.gb28181.processor.SIPProcessorObserver;
import gov.nist.javax.sip.SipProviderImpl;
import gov.nist.javax.sip.SipStackImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.sip.*;
import java.util.Properties;
import java.util.TooManyListenersException;

@Component
public class SipLayer {

    private final static Logger logger = LoggerFactory.getLogger(SipLayer.class);

    @Autowired
    private SipDeviceConfig sipConfig;

    @Autowired
    private SIPProcessorObserver sipProcessorObserver;

    private SipStackImpl sipStack;

    private SipFactory sipFactory;


    @Bean("sipFactory")
    private SipFactory createSipFactory() {
        sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("gov.nist");
        return sipFactory;
    }

    @Bean("sipStack")
    @DependsOn({"sipFactory"})
    private SipStack createSipStack() throws PeerUnavailableException {
        Properties properties = new Properties();
        properties.setProperty("javax.sip.STACK_NAME", "GB28181_SIP");
        properties.setProperty("javax.sip.IP_ADDRESS", sipConfig.getMonitorIp());
        sipStack = (SipStackImpl) sipFactory.createSipStack(properties);
        return sipStack;
    }

    @Bean(name = "tcpSipProvider")
    @DependsOn("sipStack")
    private SipProviderImpl startTcpListener() {
        ListeningPoint tcpListeningPoint = null;
        SipProviderImpl tcpSipProvider = null;
        try {
            tcpListeningPoint = sipStack.createListeningPoint(sipConfig.getMonitorIp(), sipConfig.getPort(), "TCP");
            tcpSipProvider = (SipProviderImpl) sipStack.createSipProvider(tcpListeningPoint);
            tcpSipProvider.addSipListener(sipProcessorObserver);
            logger.info("Sip Server TCP ???????????? port {" + sipConfig.getMonitorIp() + ":" + sipConfig.getPort() + "}");
        } catch (TransportNotSupportedException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            logger.error("???????????? [ {}:{} ]??????SIP[ TCP ]??????????????????: 1. sip.monitor-ip ?????????????????????IP; 2. sip.port ??????????????????"
                    , sipConfig.getMonitorIp(), sipConfig.getPort());
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        } catch (ObjectInUseException e) {
            e.printStackTrace();
        }
        return tcpSipProvider;
    }

    @Bean(name = "udpSipProvider")
    @DependsOn("sipStack")
    private SipProviderImpl startUdpListener() {
        ListeningPoint udpListeningPoint = null;
        SipProviderImpl udpSipProvider = null;
        try {
            udpListeningPoint = sipStack.createListeningPoint(sipConfig.getMonitorIp(), sipConfig.getPort(), "UDP");
            udpSipProvider = (SipProviderImpl) sipStack.createSipProvider(udpListeningPoint);
            udpSipProvider.addSipListener(sipProcessorObserver);
        } catch (TransportNotSupportedException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            logger.error("???????????? [ {}:{} ]??????SIP[ UDP ]??????????????????: 1. sip.monitor-ip ?????????????????????IP; 2. sip.port ??????????????????"
                    , sipConfig.getMonitorIp(), sipConfig.getPort());
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        } catch (ObjectInUseException e) {
            e.printStackTrace();
        }
        logger.info("Sip Server UDP ???????????? port [" + sipConfig.getMonitorIp() + ":" + sipConfig.getPort() + "]");
        return udpSipProvider;
    }

}
