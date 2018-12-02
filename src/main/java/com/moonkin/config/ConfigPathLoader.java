package com.moonkin.config;

import com.moonkin.util.JarToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class ConfigPathLoader{

    private static Logger logger = LoggerFactory.getLogger(ConfigPathLoader.class);

	private static Properties pro;
	
	static {
        try {
            pro = new Properties();
            String configPath = JarToolUtil.getJarDir() + "/ConfigPathLoader.properties";
            logger.info("初始化配置文件：" + configPath);
        	File config = new File(configPath);
        	if(!config.exists()) { //优先加载当前目录下的配置文件，如果没有就加载jar包config下面的
               configPath  = Thread.currentThread().getContextClassLoader().getResource("").getFile() + "config/ConfigPathLoader.properties";
                logger.info("配置文件不存在，读取类内部配置：" + configPath);
            }
            FileInputStream properties = new FileInputStream(configPath);
            InputStreamReader in = new InputStreamReader(properties, "UTF-8");
            pro.load(in);
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key) {
		return pro.getProperty(key);
	}
    
    public static void setProperty(String key, String value) {
		try {

			FileOutputStream oFile = new FileOutputStream(JarToolUtil.getJarDir() + "/ConfigPathLoader.properties",true);
			pro.setProperty(key,value);
			pro.store(oFile,"new properties file");
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
    }

	public static TaskProperties getTaskProperties(String taskName)
	{
		TaskProperties tpForRead = new TaskProperties();
		tpForRead.SetTaskName(taskName);
		tpForRead.SetPathIn(getProperty(taskName + ".pathin"));
		tpForRead.SetPathOut(getProperty(taskName + ".pathout"));
		tpForRead.SetDataKeepTime(getProperty(taskName + ".dataKeepTime"));
		tpForRead.SetRadioIndex(Integer.parseInt(getProperty(taskName + ".radioIndex")));
		return  tpForRead;
	}

	public static void setTaskProperties(TaskProperties task)
	{
		setProperty(task.GetTaskName() + ".pathin",task.GetPathIn());
		setProperty(task.GetTaskName() + ".pathout",task.GetPathOut());
		setProperty(task.GetTaskName() + ".dataKeepTime",task.GetDataKeepTime());
		setProperty(task.GetTaskName() + ".radioIndex",String.valueOf(task.GetRadioIndex()));
	}
}
