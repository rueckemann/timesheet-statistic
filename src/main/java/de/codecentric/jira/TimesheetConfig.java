package de.codecentric.jira;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan("de.codecentric")
@PropertySource("classpath:jira.properties")
public class TimesheetConfig {

    @Value("${jira.url}")
    private String jiraUrl;
    
    @Value("${jira.username}")
    private String jiraUsername;
    
    @Value("${jira.password}")
    private String jiraPassword;
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();      
    }
    
    @Bean
    public JiraClient getJiraClient() {
        JiraClient jiraClient = new JiraClient(jiraUrl, jiraUsername, jiraPassword);
        return jiraClient;
    }
    
}
