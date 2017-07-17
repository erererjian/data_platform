package com.wlwx.back.quartz.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wlwx.application.SpringContextUtil;
import com.wlwx.back.util.FileUtil;
import com.wlwx.back.util.PlatformUtil;
import com.wlwx.service.TaskService;

/**
 * 删除历史数据
 * @author zjj
 * @date 2017年7月17日 上午9:51:03
 */
public class DelHistoryDataJob implements Job{
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		System.out.println("===============" + sdf.format(new Date()) + "===============================");
		
		TaskService tService = (TaskService) SpringContextUtil.getBean("taskService");
		
		//获取需要删除历史数据任务ID
		List<String> taskIds = tService.getDelTaskIds();
		System.out.println(taskIds.size());
		//定时删除数据库一个月前的任务
		tService.delHistoryData();
		//删除任务所保存的历史文件
		FileUtil.getFiles(PlatformUtil.getProperties("filePath"), taskIds);
		
	}

}
