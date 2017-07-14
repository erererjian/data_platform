package com.wlwx.back.task;

import com.wlwx.back.system.SystemInit;
import com.wlwx.model.TaskInfo;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class TaskQueue {
	public static final Logger LOGGER = Logger.getLogger(TaskQueue.class);

	private List<Task> queue = Collections.synchronizedList(new LinkedList<Task>());

	public List<Task> getQueue() {
		return this.queue;
	}

	public synchronized boolean addTask(Task task) {
		boolean bReturn = false;
		int taskThreadNum = SystemInit.taskService.getRunThreadNum();
		
		if ((task != null) && (getTaskById(task.getTask_id()) == null)) {
			if (this.queue.size() == 0) {
				this.queue.add(task);
			} else if (this.queue.size() <= taskThreadNum) {
				this.queue.add(0, task);
			} else {
				boolean success = false;
				for (int i = getNewTaskSize() - 1; i >= 0; i--) {
					Task oldTask = (Task) this.queue.get(i);
					if (task.getPriority() >= oldTask.getPriority()) {
						this.queue.add(i + 1, task);
						success = true;
						break;
					}
				}
				if (!success) {
					this.queue.add(0, task);
				}
			}
			bReturn = true;
		}
		return bReturn;
	}

	public synchronized void finishTask(Task task) {
		if (task != null)
			this.queue.remove(task);
	}

	public synchronized Task getTask() {
		if (getNewTaskSize() > 0) {
			Task task = queue.get(0);
			task.setState(TaskInfo.RUNNING);
			queue.add(queue.size() - 1, queue.remove(0));
			return task;
		}
		return null;
	}

	public synchronized int getTaskSize() {
		return queue.size();
	}

	public synchronized int getRunTaskSize() {
		int runningNum = 0;
		Iterator<Task> it = queue.iterator();

		while (it.hasNext()) {
			Task task = (Task) it.next();
			if (task.state == TaskInfo.RUNNING) {
				runningNum++;
			}
		}
		return runningNum;
	}

	public synchronized int getNewTaskSize() {
		int runningNum = 0;
		Iterator<Task> it = queue.iterator();

		while (it.hasNext()) {
			Task task = (Task) it.next();
			if (task.state == TaskInfo.NEW){
				runningNum++;
			}
		}
		return runningNum;
	}

	public synchronized void setTaskState(String taskId, int state) {
		Iterator<Task> it = queue.iterator();

		while (it.hasNext()) {
			Task task = (Task) it.next();
			if (task.getTask_id().equals(taskId))
				task.setState(state);
		}
	}

	public synchronized Task getTaskById(String tid) {
		Iterator<Task> it = queue.iterator();

		while (it.hasNext()) {
			Task task = (Task) it.next();
			if (task.getTask_id().equals(tid)) {
				return task;
			}
		}
		return null;
	}
}