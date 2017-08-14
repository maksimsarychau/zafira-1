package com.qaprosoft.zafira.batchservices;

import com.qaprosoft.zafira.services.services.JenkinsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tanukisoftware.wrapper.WrapperSimpleApp;

public class JobService extends WrapperSimpleApp
{
	
	protected JobService(String[] args)
	{
		super(args);
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings({ "resource", "unused" })
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:zafira-batchservices.xml");

		JenkinsService jenkinsService = (JenkinsService) context.getBean("jenkinsService");
		System.out.println(jenkinsService.isConnected());
	}
}
