package com.wydpp.controller.model;

import lombok.Data;

@Data
public class HiPotRequest {
    /**
     * 运行的线程数
     */
    private Integer runThread;

    /**
     * 模拟的设备数量
     */
    private Integer deviceNums;

}
