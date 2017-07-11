package com.wlwx.back.system;

import com.wlwx.back.task.ThreadPoolService;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 系统初始化类
 * @author zjj
 * @date 2017年7月11日 下午4:54:05
 */
public class SystemInit {
	public static final Logger LOGGER = Logger.getLogger(SystemInit.class);

	public static Map<String, Object> sessionMap = new HashMap<>();
	public static ThreadPoolService taskService;

	/**
	 * 开启任务线程
	 * @date 2017年7月11日 下午4:54:15
	 */
	public static synchronized void start() {
		try {
			taskService = new ThreadPoolService(3);
			taskService.start();
		} catch (Exception e) {
			LOGGER.error("SystemInit start() error", e);
		}
	}

	/**
	 * 关闭任务线程
	 * @date 2017年7月11日 下午4:54:27
	 */
	public static synchronized void stop() {
		try {
			taskService.stop(true);
		} catch (Exception e) {
			LOGGER.error("SystemInit stop() error", e);
		}
	}
}