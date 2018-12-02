package com.moonkin.ui.listener;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.moonkin.task.GroundTask;
import com.moonkin.task.TaskManager;
import com.moonkin.task.TaskScheduleBuilder;
import com.moonkin.ui.MainWindow;
import javax.swing.*;
import java.io.File;

/**
 * 地面绘图报窗体事件监控
 */
public class GroundListener {
    private static final Log logger = LogFactory.get();
    private static GroundTask groundTask;

    public static void addListeners() {
        //原始目录
        MainWindow.mainWindow.getGroundSourceButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getGroundSourceTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getGroundPanel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getGroundSourceTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }

        });
        //目标目录
        MainWindow.mainWindow.getGroundDestButton().addActionListener(e -> {
            File beforeFile = new File(MainWindow.mainWindow.getGroundDestTextField().getText());
            JFileChooser fileChooser;
            if (beforeFile.exists()) {
                fileChooser = new JFileChooser(beforeFile);
            } else {
                fileChooser = new JFileChooser();
            }
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int approve = fileChooser.showOpenDialog(MainWindow.mainWindow.getGroundPanel());
            if (approve == JFileChooser.APPROVE_OPTION) {
                MainWindow.mainWindow.getGroundDestTextField().setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        //启动
        MainWindow.mainWindow.getGroundStartButton().addActionListener(e -> {
            boolean hourSelected = MainWindow.mainWindow.getGroundHourRadio().isSelected();
            boolean minSelected = MainWindow.mainWindow.getGround10minRadio().isSelected();
            if(hourSelected) {
                if(groundTask != null) {
                    TaskManager.removeTask(groundTask.getTaskId());
                }
                groundTask = new GroundTask();
                groundTask.setTaskSchedule(TaskScheduleBuilder.getEveryHourSchedule(1));
                TaskManager.addTask(groundTask);
            }
            if(minSelected) {
                if(groundTask != null) {
                    TaskManager.removeTask(groundTask.getTaskId());
                }
                groundTask = new GroundTask();
                groundTask.setTaskSchedule(TaskScheduleBuilder.getEveryMinuteSchedule(10));
                TaskManager.addTask(groundTask);
            }
        });

        //停止
        MainWindow.mainWindow.getGroundStopButton().addActionListener(e -> {
            TaskManager.pauseTask(groundTask.getTaskId());
        });
    }
}
