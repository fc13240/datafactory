package com.moonkin.ui;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.moonkin.util.Config;
import com.moonkin.util.SystemUtil;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.*;

/**
 * 初始化类
 * @author xuduo on 2017/6/15.
 */
public class Init {

    private static final Log logger = LogFactory.get();

    // 配置文件管理器对象
    public static Config configer = Config.getInstance();

    /**
     * 设置全局字体
     */
    public static void initGlobalFont() {
        // 低分辨率屏幕字号初始化
        String lowDpiKey = "lowDpiInit";
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸
        if (screenSize.width <= 1366 && StringUtils.isEmpty(configer.getProps(lowDpiKey))) {
            configer.setFontSize(15);
            configer.setProps(lowDpiKey, "true");
            configer.save();
        }
        // Mac高分辨率屏幕字号初始化
        String highDpiKey = "highDpiInit";
        if (SystemUtil.isMacOs() && StringUtils.isEmpty(configer.getProps(highDpiKey))) {
            configer.setFontSize(15);
            configer.setProps(highDpiKey, "true");
            configer.save();
        }
        Font fnt = new Font(configer.getFont(), Font.PLAIN, configer.getFontSize());
        FontUIResource fontRes = new FontUIResource(fnt);
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
                UIManager.put(key, fontRes);
        }
    }

    /**
     * 初始化look and feel
     */
    public static void initTheme() {
        try {
            switch (configer.getTheme()) {
                case "系统默认":
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case "Windows":
                    UIManager.setLookAndFeel(WindowsLookAndFeel.class.getName());
                    break;
                case "Nimbus":
                    UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
                    break;
                case "Metal":
                    UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
                    break;
                case "Motif":
                    UIManager.setLookAndFeel(MotifLookAndFeel.class.getName());
                    break;
                default:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            logger.error(e);
        }

    }

    /**
     * 自定义单元格单选框渲染器
     */
    public static class MyCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Boolean b = (Boolean) value;//这一列必须都是integer类型(0-100)
            setSelected(b);
            return this;
        }
    }

}
