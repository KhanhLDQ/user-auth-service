package org.tommap.tomuserloginrestapis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "emailTaskExecutor")
    public Executor emailTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3); //number of threads always kept alive in the pool
        executor.setMaxPoolSize(5); //max number of threads can go up when demand increases
        executor.setQueueCapacity(50); //number of tasks that can wait in queue before pool creates new thread (up to max pool size)
        executor.setThreadNamePrefix("async-email-task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //when pool is full -> calling thread executes task synchronously instead of rejecting it
        executor.initialize();

        return executor;
    }

    @Bean(name = "genericTaskExecutor")
    @Primary //default executor
    public Executor genericTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-generic-task-");
        executor.initialize();

        return executor;
    }
}
