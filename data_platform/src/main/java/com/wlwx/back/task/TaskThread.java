package com.wlwx.back.task;

import org.apache.log4j.Logger;

public class TaskThread extends Thread {
	public static final Logger LOGGER = Logger.getLogger(TaskThread.class);
	private ThreadPoolService service;

	public TaskThread(ThreadPoolService tps) {
		this.service = tps;
	}

	public void run() {
		System.out.println("TaskThread run() in");
		while (service.isRunning()) {
			try {
				TaskQueue queue = service.getTaskQueue();
				Task task = queue.getTask();
				if (task != null) {
					try {
						task.deal();
					} catch (Exception e) {
						LOGGER.error("TaskThread run() error[task.deal()]:"
								+ e.getMessage());
					}
					queue.finishTask(task);
				} else {
					sleep(500L);
				}

			} catch (Exception e) {
				LOGGER.error("TaskThread run() error[TaskThread run error]:"
						+ e.getMessage());
			}
		}

		service.removeThread(this);
		System.out.println("TaskThread run() out");
	}
}