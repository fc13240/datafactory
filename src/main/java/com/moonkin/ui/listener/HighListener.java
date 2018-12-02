package com.moonkin.ui.listener;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.moonkin.task.HighTask;
import com.moonkin.task.TaskManager;
import com.moonkin.task.TaskScheduleBuilder;
import com.moonkin.ui.MainWindow;

import javax.swing.*;
import java.io.File;

/**
 * 地面绘图报窗体事件监控
 */
public class HighListener {
    private static final Log logger = LogFactory.get();
    private static HighTask highTask;
    public static void addListeners() {
        //原始目录
        MainWindow.mainWindow.getHighSourceButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getHighSourceTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getHighPanel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getHighSourceTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        //目标目录
        MainWindow.mainWindow.getHighDestButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getHighDestTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getHighPanel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getHighDestTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        //启动
        MainWindow.mainWindow.getHighStartButton().addActionListener(e -> {
            boolean hourSelected = MainWindow.mainWindow.getHighHourRadio().isSelected();
            boolean minSelected = MainWindow.mainWindow.getHigh10minRadio().isSelected();
            if(hourSelected) {
                if(highTask != null) {
                    TaskManager.removeTask(highTask.getTaskId());
                }
                highTask = new HighTask();
                highTask.setTaskSchedule(TaskScheduleBuilder.getEveryHourSchedule(1));
                TaskManager.addTask(highTask);
            }
            if(minSelected) {
                if(highTask != null) {
                    TaskManager.removeTask(highTask.getTaskId());
                }
                highTask = new HighTask();
                highTask.setTaskSchedule(TaskScheduleBuilder.getEveryMinuteSchedule(10));
                TaskManager.addTask(highTask);
            }
        });

        //停止
        MainWindow.mainWindow.getHighStopButton().addActionListener(e -> {
            TaskManager.pauseTask(highTask.getTaskId());
        });
    }
}
