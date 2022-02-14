package com.wydpp.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SipDevice {

	private Integer id;
	/**
	 * 设备Id
	 */
	private String deviceId;

	/**
	 * 设备名
	 */
	private String name;

	/**
	 * 生产厂商
	 */
	private String manufacturer;

	/**
	 * 型号
	 */
	private String model;

	/**
	 * 固件版本
	 */
	private String firmware;

	/**
	 * 传输协议
	 * UDP/TCP
	 */
	private String transport;

	/**
	 * 数据流传输模式
	 * UDP:udp传输
	 * TCP-ACTIVE：tcp主动模式
	 * TCP-PASSIVE：tcp被动模式
	 */
	private String streamMode;

	/**
	 * wan地址_ip
	 */
	private String  ip;

	/**
	 * wan地址_port
	 */
	private Integer port;

	/**
	 * wan地址
	 */
	private String  hostAddress;

	/**
	 * 在线
	 */
	private Boolean online;


	/**
	 * 注册时间
	 */
	private Long registerTime;

	private String registerTimeStr;


	/**
	 * 心跳时间
	 */
	private Long keepaliveTime;

	private String keepaLiveTimeStr;

	/**
	 * 通道个数
	 */
	private Integer channelCount;

	/**
	 * 注册有效期
	 */
	private Integer expires;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 更新时间
	 */
	private String updateTime;

	/**
	 * 设备使用的媒体id, 默认为null
	 */
	private String mediaServerId;

	/**
	 * 首次注册
	 */
	private Boolean firsRegister;

	/**
	 * 字符集, 支持 utf-8 与 gb2312
	 */
	private String charset ;

	/**
	 * 目录订阅周期，0为不订阅
	 */
	private Integer subscribeCycleForCatalog ;


	private Boolean needRegister;

}
