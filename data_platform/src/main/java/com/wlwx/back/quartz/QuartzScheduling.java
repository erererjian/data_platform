package com.wlwx.back.quartz;

import java.text.ParseException;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.wlwx.back.quartz.job.DelHistoryDataJob;

/**
 * 调度azkaban定时任务
 * @author zjj
 * @date 2017年7月17日 上午9:53:29
 */
public class QuartzScheduling {
	
	/**
	 * 定时删除历史数据
	 * @throws ParseException 
	 * @throws SchedulerException 
	 * @date 2017年7月17日 上午9:54:38
	 */
	public static void delHistoryDataOnTime() throws ParseException, SchedulerException {
		
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDetail jobDetail = new JobDetail("helloQuartzJob", 
                Scheduler.DEFAULT_GROUP, DelHistoryDataJob.class);

        String cronExpression = "0 0 3 * * ?"; // 每天凌晨三点，一天执行一遍
        CronTrigger cronTrigger = new CronTrigger("cronTrigger", 
                Scheduler.DEFAULT_GROUP, cronExpression);

        scheduler.scheduleJob(jobDetail, cronTrigger);

        scheduler.start();
        
	}
	
}
