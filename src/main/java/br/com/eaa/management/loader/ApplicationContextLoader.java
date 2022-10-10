package br.com.eaa.management.loader;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextLoader implements ApplicationContextAware {

    @Autowired
    private static ApplicationContext applicationContexts;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContexts = applicationContext;
    }

    public static  ApplicationContext getApplicationContext() {
        return applicationContexts;
    }
}
