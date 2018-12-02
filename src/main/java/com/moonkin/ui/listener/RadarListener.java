package com.moonkin.ui.listener;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.moonkin.task.*;
import com.moonkin.ui.MainWindow;

import javax.swing.*;
import java.io.File;

/**
 * Radar图规整窗体事件监控
 */
public class RadarListener {
    private static final Log logger = LogFactory.get();
    private static RadarTask radarTask;

    public static void addListeners() {
        //原始目录
        MainWindow.mainWindow.getRadarSourceButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getRadarSourceTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getRadarPanel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getRadarSourceTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }

        });
        //目标目录
        MainWindow.mainWindow.getRadarDestButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getRadarDestTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getRadarPanel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getRadarDestTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        //启动
        MainWindow.mainWindow.getRadarStartButton().addActionListener(e -> {
            boolean hourSelected = MainWindow.mainWindow.getRadarHourRadio().isSelected();
            boolean minSelected = MainWindow.mainWindow.getRadar10minRadio().isSelected();
            if(hourSelected) {
                if(radarTask != null) {
                    TaskManager.removeTask(radarTask.getTaskId());
                }
                radarTask = new RadarTask();
                radarTask.setTaskSchedule(TaskScheduleBuilder.getEveryHourSchedule(1));
                TaskManager.addTask(radarTask);
            }
            if(minSelected) {
                if(radarTask != null) {
                    TaskManager.removeTask(radarTask.getTaskId());
                }
                radarTask = new RadarTask();
                radarTask.setTaskSchedule(TaskScheduleBuilder.getEveryMinuteSchedule(10));
                TaskManager.addTask(radarTask);
            }
        });

        //停止
        MainWindow.mainWindow.getRadarStopButton().addActionListener(e -> {
            TaskManager.pauseTask(radarTask.getTaskId());
        });

    }
}
