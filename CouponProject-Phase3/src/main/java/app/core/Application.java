package app.core;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

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
public class Application extends SpringBootServletInitializer{

	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

		
		
		// scheduler check to delete all expired coupons from DB all other scheduled runs are exactly at 00:00 daily.
		DailyRemoveCouponsTask scheduled = ctx.getBean(DailyRemoveCouponsTask.class);
		scheduled.runAtApplicationStart();
		
		
		//===========================================================//
		//========TESTS RUNNING FROM APP.CORE.TESTS PACKAGE==========//
		//===========================================================//
		
		Jwt jwtUtil = ctx.getBean(Jwt.class);
		ClientDetails clientD = new ClientDetails(0,"aaa",ClientType.ADMINISTRATOR);
		String genToken = jwtUtil.generateToken(clientD);
		System.out.println(genToken);
		
		// slight delay before context close, to allow all test to run correctly.
//		try {
//			Thread.sleep(1000L);
//		} catch (InterruptedException e) {
//		}
//		ctx.close();

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
