package org.strangeforest.tcb.stats.spring;

import java.util.*;

import org.springframework.web.servlet.*;
import org.springframework.web.servlet.i18n.*;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.web.servlet.error.*;
import org.springframework.context.annotation.*;
import org.strangeforest.tcb.stats.controller.*;

@Configuration @ConditionalOnWebApplication
public class TennisStatsWebConfig implements WebMvcConfigurer {

	@Autowired(required = false) DownForMaintenanceInterceptor downForMaintenanceInterceptor;

	@Override public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(new RequestURLLoggingHandlerInterceptor());
		if (downForMaintenanceInterceptor != null)
			registry.addInterceptor(downForMaintenanceInterceptor);
	}

	@Bean
	public ErrorAttributes errorAttributes() {
		return new TennisStatsErrorAttributes();
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
		cookieLocaleResolver.setDefaultLocale(Locale.CHINESE);
		return cookieLocaleResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("language");
		return localeChangeInterceptor;
	}
}
