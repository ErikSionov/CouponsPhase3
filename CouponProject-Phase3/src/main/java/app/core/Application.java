package app.core;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import app.core.filters.LoginFilter;
import app.core.tasks.DailyRemoveCouponsTask;
import app.core.utils.ClientDetails;
import app.core.utils.Jwt;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** Coupon System application phase 2, Spring boot based.
 * @author Erik Sionov
 *
 */
@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
		
		// scheduler check to delete all expired coupons from DB all other scheduled runs are exactly at 00:00 daily.
		DailyRemoveCouponsTask scheduled = ctx.getBean(DailyRemoveCouponsTask.class);
		scheduled.runAtApplicationStart();
		
		
		//===========================================================//
		//========TESTS RUNNING FROM APP.CORE.TESTS PACKAGE==========//
		//===========================================================//
		
		Jwt jwtUtil = ctx.getBean(Jwt.class);
		ClientDetails clientD = new ClientDetails(2,"aaa",ClientType.ADMINISTRATOR);
		String genToken = jwtUtil.generateToken(clientD);
		System.out.println(genToken);
		
		// slight delay before context close, to allow all test to run correctly.
//		try {
//			Thread.sleep(1000L);
//		} catch (InterruptedException e) {
//		}
//		ctx.close();

	}
	// TODO ELDAR where is a best place to place beans such as those?
	@Bean
	public FilterRegistrationBean<LoginFilter> RegistrationBean(Jwt jwtUtil){
		FilterRegistrationBean<LoginFilter> regBean = new FilterRegistrationBean<>();
		regBean.setFilter(new LoginFilter(jwtUtil));
		regBean.addUrlPatterns("/api/*");
		regBean.setOrder(1);
		return regBean;
	}
	
	
	/**Utility method to format out-print of List objects.
	 * @param list
	 */
	public static void printList(List<?> list) {
		System.out.println("=======================");
		list.forEach(System.out::println);
		System.out.println("=======================");
	}

}
