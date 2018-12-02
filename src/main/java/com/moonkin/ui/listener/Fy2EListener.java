package com.moonkin.ui.listener;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.moonkin.task.*;
import com.moonkin.ui.MainWindow;

import javax.swing.*;
import java.io.File;

/**
 * Fy2E数据规整窗体事件监控
 */
public class Fy2EListener {
    private static final Log logger = LogFactory.get();
    private static Fy2ETask fy2ETask;

    public static void addListeners() {
        //原始目录
        MainWindow.mainWindow.getFy2eSourceButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getFy2eSourceTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getFy2ePanel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getFy2eSourceTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        //目标目录
        MainWindow.mainWindow.getFy2eDestButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getFy2eDestTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getFy2ePanel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getFy2eDestTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        //启动
        MainWindow.mainWindow.getFy2eStartButton().addActionListener(e -> {
            boolean hourSelected = MainWindow.mainWindow.getFy2eHourRadio().isSelected();
            boolean minSelected = MainWindow.mainWindow.getFy2e10minRadio().isSelected();
            if(hourSelected) {
                if(fy2ETask != null) {
                    TaskManager.removeTask(fy2ETask.getTaskId());
                }
                fy2ETask = new Fy2ETask();
                fy2ETask.setTaskSchedule(TaskScheduleBuilder.getEveryHourSchedule(1));
                TaskManager.addTask(fy2ETask);
            }
            if(minSelected) {
                if(fy2ETask != null) {
                    TaskManager.removeTask(fy2ETask.getTaskId());
                }
                fy2ETask = new Fy2ETask();
                fy2ETask.setTaskSchedule(TaskScheduleBuilder.getEveryMinuteSchedule(10));
                TaskManager.addTask(fy2ETask);
            }
        });

        //停止
        MainWindow.mainWindow.getFy2eStopButton().addActionListener(e -> {
            TaskManager.pauseTask(fy2ETask.getTaskId());
        });
    }
}
