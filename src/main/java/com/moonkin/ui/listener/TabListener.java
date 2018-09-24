package com.moonkin.ui.listener;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.moonkin.ui.Init;
import com.moonkin.ui.MainWindow;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * tab事件监听
 * @author xuduo
 * @date 2018-09-23
 */
public class TabListener {

    public static void addListeners() {
        // 暂时停止使用，仅留作demo，日后需要时再使用
        MainWindow.mainWindow.getTabbedPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int index = MainWindow.mainWindow.getTabbedPane().getSelectedIndex();
                switch (index) {
                    case 4:
                        MainWindow.mainWindow.setPushMsgName(MainWindow.mainWindow.getMsgNameField().getText());
                    default:
                        break;
                }
            }
        });
    }
}
