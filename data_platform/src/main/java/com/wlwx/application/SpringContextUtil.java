package com.wlwx.application;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 上下文工具
 * @author zjj
 * @date 2017年7月13日 上午11:49:48
 */
@Component
public class SpringContextUtil implements ApplicationContextAware{
	private static ApplicationContext applicationContext;  
	  
	//设置上下文  
	@Override
    public void setApplicationContext(ApplicationContext applicationContext) {  
        SpringContextUtil.applicationContext = applicationContext;  
    }  
	
    //获取上下文  
    public static ApplicationContext getApplicationContext() {  
        return applicationContext;  
    }  
  
    //通过名字获取上下文中的bean  
    public static Object getBean(String name){  
        return applicationContext.getBean(name);  
    }  
}
