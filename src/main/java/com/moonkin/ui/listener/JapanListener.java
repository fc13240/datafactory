package com.moonkin.ui.listener;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.moonkin.task.*;
import com.moonkin.ui.MainWindow;

import javax.swing.*;
import java.io.File;

/**
 * 日本传真规整换名窗体事件监控
 */
public class JapanListener {
    private static final Log logger = LogFactory.get();
    private static JapanTask japanTask;

    public static void addListeners() {
        //原始目录
        MainWindow.mainWindow.getJapanSourceButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getJapanSourceTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getJapanPanel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getJapanSourceTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }

        });
        //目标目录
        MainWindow.mainWindow.getJapanDestButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getJapanDestTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getJapanPanel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getJapanDestTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        //启动
        MainWindow.mainWindow.getJapanStartButton().addActionListener(e -> {
            boolean hourSelected = MainWindow.mainWindow.getJapanHourRadio().isSelected();
            boolean minSelected = MainWindow.mainWindow.getJapan10minRadio().isSelected();
            if(hourSelected) {
                if(japanTask != null) {
                    TaskManager.removeTask(japanTask.getTaskId());
                }
                japanTask = new JapanTask();
                japanTask.setTaskSchedule(TaskScheduleBuilder.getEveryHourSchedule(1));
                TaskManager.addTask(japanTask);
            }
            if(minSelected) {
                if(japanTask != null) {
                    TaskManager.removeTask(japanTask.getTaskId());
                }
                japanTask = new JapanTask();
                japanTask.setTaskSchedule(TaskScheduleBuilder.getEveryMinuteSchedule(10));
                TaskManager.addTask(japanTask);
            }
        });

        //停止
        MainWindow.mainWindow.getJapanStopButton().addActionListener(e -> {
            TaskManager.pauseTask(japanTask.getTaskId());
        });

    }
}
