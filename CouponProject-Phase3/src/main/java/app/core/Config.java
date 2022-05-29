package app.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.core.filters.DelayFilter;
import app.core.filters.LoginFilter;
import app.core.utils.Jwt;

@Configuration
public class Config {

	@Bean
	public FilterRegistrationBean<LoginFilter> RegistrationBean(Jwt jwtUtil){
		FilterRegistrationBean<LoginFilter> regBean = new FilterRegistrationBean<>();
		regBean.setFilter(new LoginFilter(jwtUtil));
		regBean.addUrlPatterns("/api/*");
		regBean.setOrder(1);
		return regBean;
	}

	@Bean
	public FilterRegistrationBean<DelayFilter> delayFilter(@Value("${my.filters.delay.milis: 1000}") long delayMilis) {
		FilterRegistrationBean<DelayFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new DelayFilter(delayMilis));
		registrationBean.setOrder(1);
		return registrationBean;
	}
}
