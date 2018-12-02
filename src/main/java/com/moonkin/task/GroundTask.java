package com.moonkin.task;

import com.moonkin.decode.GroundStationToMicaps1;
import com.moonkin.ui.MainWindow;
import org.quartz.JobDataMap;

/**
 * 地面绘图报解析任务
 * @author xuduo
 * @date 2018/9/27
 */
public class GroundTask extends BaseTask {

    @Override
    protected void config() {
        setTaskId("ground_task");
        setTaskName("地面绘图报转格式");
    }

    @Override
    protected void doWork(JobDataMap dataMap) {
        String sourceFile = MainWindow.mainWindow.getGroundSourceTextField().getText().trim();
        String destFile = MainWindow.mainWindow.getGroundDestTextField().getText().trim();
        GroundStationToMicaps1.writeToMicap1(sourceFile, destFile);
    }
}
