package com.wlwx.back.task;

import org.apache.log4j.Logger;

/**
 * 任务线程
 * @author zjj
 * @date 2017年7月11日 下午7:52:06
 */
public class TaskThread extends Thread {
	public static final Logger LOGGER = Logger.getLogger(TaskThread.class);
	private ThreadPoolService service;

	public TaskThread(ThreadPoolService tps) {
		this.service = tps;
	}

	public void run() {
		LOGGER.info("TaskThread run() in");
		while (service.isRunning()) {
			TaskQueue queue = service.getTaskQueue();
			Task task = queue.getTask();
			if (task != null) {
				try {
					task.deal();
				} catch (Exception e) {
					LOGGER.error("TaskThread run() error[task.deal()]:"
							+ e.getMessage(), e);
				} finally {
					queue.finishTask(task);
				}
			} else {
				try {
					sleep(500);
				} catch (InterruptedException e) {
					LOGGER.error("TaskThread run() InterruptedException]:"
							+ e.getMessage(), e);
				}
			}
			if (service.getRunThreadNum() > service.getThreadNum()){ //当任务线程数减少时则退出该线程
            	//退出线程
            	break;
            }
		}
		service.removeThread(this);
		LOGGER.info("TaskThread run() out");
	}
}