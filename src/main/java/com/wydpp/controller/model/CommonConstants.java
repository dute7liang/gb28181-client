

package com.wydpp.controller.model;

/**
 * @author zhangliang
 * @date 2017/10/29
 */
public interface CommonConstants {

    /**
     * header 中租户ID
     */
    String TENANT_ID = "TENANT-ID";

    /**
     * header 中版本信息
     */
    String VERSION = "VERSION";

    /**
     * 租户ID
     */
    Integer TENANT_ID_1 = 1;

    /**
     * 菜单树根节点
     */
    Long MENU_TREE_ROOT_ID = -1L;

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * 公共参数
     */
    String PIG_PUBLIC_PARAM_KEY = "PIG_PUBLIC_PARAM_KEY";

    /**
     * 成功标记
     */
    Integer SUCCESS = 0;

    /**
     * 失败标记
     */
    Integer FAIL = 1;

    /**
     * 默认存储bucket
     */
    String BUCKET_NAME = "lengleng";

    /**
     * 滑块验证码
     */
    String IMAGE_CODE_TYPE = "blockPuzzle";

    /**
     * 验证码开关
     */
    String CAPTCHA_FLAG = "captcha_flag";

    /**
     * 密码传输是否加密
     */
    String ENC_FLAG = "enc_flag";


    //redis消息主题
    String TOPIC_SYSDEPT_UPDATE="topic_sysDept_update";
    String TOPIC_SYSDEPT_DELETE="topic_sysDept_delete";

    String TOPIC_SYSUSER_UPDATE="topic_sysUser_update";
}
