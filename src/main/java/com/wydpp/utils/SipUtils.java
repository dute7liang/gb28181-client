package com.wydpp.utils;

import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.SipUri;

import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 * @author panlinlin
 * @version 1.0.0
 * @description JAIN SIP的工具类
 * @createTime 2021年09月27日 15:12:00
 */
public class SipUtils {

    public static String getUserIdFromFromHeader(Request request) {
        FromHeader fromHeader = (FromHeader)request.getHeader(FromHeader.NAME);
        return getUserIdFromFromHeader(fromHeader);
    }

    public static String getUserIdFromFromHeader(Response response) {
        FromHeader fromHeader = (FromHeader)response.getHeader(FromHeader.NAME);
        return getUserIdFromFromHeader(fromHeader);
//        AddressImpl address = (AddressImpl)toHeader.getAddress();
//        SipUri uri = (SipUri) address.getURI();
//        return uri.getUser();
    }

    public static String getUserIdFromFromHeader(FromHeader fromHeader) {
        AddressImpl address = (AddressImpl)fromHeader.getAddress();
        SipUri uri = (SipUri) address.getURI();
        return uri.getUser();
    }

    public static String getUserIdFromToHeader(Request request) {
        ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
        AddressImpl address = (AddressImpl)toHeader.getAddress();
        SipUri uri = (SipUri) address.getURI();
        return uri.getUser();
    }

}
