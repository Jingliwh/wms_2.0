package com.gomi.bos.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.gomi.bos.service.base.PromotionService;

public class PromotionJob  implements  Job{
	@Autowired
	private  PromotionService  promotionService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("活动过期定时任务执行");
		promotionService.updateDate(new Date());
	}

}
