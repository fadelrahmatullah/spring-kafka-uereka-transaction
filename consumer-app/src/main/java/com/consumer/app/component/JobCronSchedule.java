package com.consumer.app.component;

import javax.annotation.Resource;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;


/**
 * JobCronSchedule
 * 
 * @author Fadel R.
 * @date 2024Feb03
 *
 */
@Component
@EnableScheduling
public class JobCronSchedule implements SchedulingConfigurer{
    
    @Resource
    FlowTask flowTask;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        Runnable task = () -> flowTask.flowExecute();
        Trigger trigger = triggerContext -> {
            String cron = "0 */2 * * * ?";
            CronTrigger cronTrigger = new CronTrigger(cron);
            return cronTrigger.nextExecutionTime(triggerContext);
        };
        taskRegistrar.addTriggerTask(task, trigger);


        Runnable taskThirdPartyLocation = () -> flowTask.taskLocation();
        Trigger triggerThirdPartyLocation = triggerContext -> {
            String cron = "0 */5 * * * ?";
            CronTrigger cronTrigger = new CronTrigger(cron);
            return cronTrigger.nextExecutionTime(triggerContext);
        };
        taskRegistrar.addTriggerTask(taskThirdPartyLocation, triggerThirdPartyLocation);
        
    }


}
