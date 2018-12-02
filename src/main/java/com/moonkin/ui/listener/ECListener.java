package com.moonkin.ui.listener;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.moonkin.task.*;
import com.moonkin.ui.MainWindow;

import javax.swing.*;
import java.io.File;

/**
 * EC模式数据规整窗体事件监控
 */
public class ECListener {
    private static final Log logger = LogFactory.get();
    private static ECTask ecTask;

    public static void addListeners() {
        //原始目录
        MainWindow.mainWindow.getEcSourceButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getEcSourceTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getEcPanel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getEcSourceTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }

        });
        //目标目录
        MainWindow.mainWindow.getEcDestButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getEcDestTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getEcPanel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getEcDestTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        //启动
        MainWindow.mainWindow.getEcStartButton().addActionListener(e -> {
            boolean hourSelected = MainWindow.mainWindow.getEcHourRadio().isSelected();
            boolean minSelected = MainWindow.mainWindow.getEc10minRadio().isSelected();
            if(hourSelected) {
                if(ecTask != null) {
                    TaskManager.removeTask(ecTask.getTaskId());
                }
                ecTask = new ECTask();
                ecTask.setTaskSchedule(TaskScheduleBuilder.getEveryHourSchedule(1));
                TaskManager.addTask(ecTask);
            }
            if(minSelected) {
                if(ecTask != null) {
                    TaskManager.removeTask(ecTask.getTaskId());
                }
                ecTask = new ECTask();
                ecTask.setTaskSchedule(TaskScheduleBuilder.getEveryMinuteSchedule(10));
                TaskManager.addTask(ecTask);
            }
        });

        //停止
        MainWindow.mainWindow.getEcStopButton().addActionListener(e -> {
            TaskManager.pauseTask(ecTask.getTaskId());
        });

    }
}
