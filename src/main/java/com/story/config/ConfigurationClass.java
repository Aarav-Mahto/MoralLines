package com.story.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.story.helper.Helper;
import com.story.helper.HelperImplementationClass;

@Configuration
public class ConfigurationClass {
	
	@Bean
	public Helper getHelper() {
		return new HelperImplementationClass();
	}
	@Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
	
}
