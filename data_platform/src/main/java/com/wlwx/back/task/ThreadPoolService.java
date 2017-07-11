package com.wlwx.back.task;

import java.util.ArrayList;
import java.util.List;

import com.wlwx.model.TaskInfo;

public class ThreadPoolService {
	public int threadNum = 0;
	private int status = TaskInfo.NEW;
	private TaskQueue queue = new TaskQueue();
	private List<Thread> threads = new ArrayList<>();

	public ThreadPoolService(int threadNum) {
		this.threadNum = threadNum;
	}

	public void start() {
		this.status = TaskInfo.RUNNING;
		for (int i = this.threads.size(); i < this.threadNum; i++) {
			Thread t = new TaskThread(this);
			this.threads.add(t);
			t.start();
		}
	}

	public void stop(boolean b) {
		this.status = TaskInfo.TERMINATED;
	}

	public boolean isRunning() {
		return this.status == TaskInfo.RUNNING;
	}

	public int runTask(Task task) {
		int nRet = 0;
		if (this.queue.addTask(task)) {
			if (this.queue.getTaskSize() > this.threadNum)
				nRet = -2;
			else
				nRet = -1;
		} else {
			nRet = 0;
		}
		return nRet;
	}

	public TaskQueue getTaskQueue() {
		return this.queue;
	}

	public synchronized void setThreadNum(int threadNum) {
		int num1 = 0;
		num1 = threadNum - this.threadNum;
		this.threadNum = threadNum;
		for (int i = 0; i < num1; i++) {
			Thread t = new TaskThread(this);
			this.threads.add(t);
			if (this.status == TaskInfo.RUNNING)
				t.start();
		}
	}

	public synchronized int getThreadNum() {
		return this.threadNum;
	}

	public synchronized int getRunThreadNum() {
		return this.threads.size();
	}

	public synchronized int getTaskNum() {
		return this.queue.getTaskSize();
	}

	public synchronized int getRunTaskNum() {
		return this.queue.getRunTaskSize();
	}

	public synchronized int getNewTaskNum() {
		return this.queue.getNewTaskSize();
	}

	public synchronized void removeThread(Thread thread) {
		if (thread != null)
			this.threads.remove(thread);
	}
}