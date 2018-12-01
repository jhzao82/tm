package org.strangeforest.tcb.stats;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cache.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.boot.web.servlet.*;
import org.springframework.web.filter.*;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class TennisStatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TennisStatsApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean urlFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new UrlFilter());
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}

	@Bean
	public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}
}
