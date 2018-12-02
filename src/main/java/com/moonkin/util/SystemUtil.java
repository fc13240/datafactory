package com.moonkin.util;

import java.io.File;

/**
 * 系统工具
 * @author xuduo
 * @since 2018-09-25
 */
public class SystemUtil {
    public static String osName = System.getProperty("os.name");
    public static String configHome = System.getProperty("user.home") + File.separator + ".datafactory"
            + File.separator;

    public static boolean isMacOs() {
        if (osName.contains("Mac")) {
            return true;
        } else {
            return false;
        }
    }
}
