package com.moonkin.ui;

import java.awt.Image;
import java.awt.Toolkit;

/**
 * UI相关的常量
 *
 * @author rememberber(https : / / github.com / rememberber)
 */
public class ConstantsUI {

    /**
     * 软件名称,版本
     */
    public final static String APP_NAME = "数据处理";
    public final static String APP_VERSION = "v0.01";

    /**
     * 主窗口图标
     */
    public final static Image IMAGE_ICON = Toolkit.getDefaultToolkit()
            .getImage(MainWindow.class.getResource("/icon/logo-lg.png"));

    /**
     * 软件版本检查url
     */
    public final static String CHECK_VERSION_URL = "https://raw.githubusercontent.com/rememberber/WePush/master/src/main/resources/version_summary.json";

    /**
     * 用户案例url
     */
    public final static String USER_CASE_URL = "http://download.zhoubochina.com/file/user_case.json";

}
