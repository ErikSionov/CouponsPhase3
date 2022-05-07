package app.core.tasks;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.core.services.AdminService;

/**scheduled task of deleting expired coupons to be run on application start and every 24 hours
 * 
 * @author Erik Sionov
 *
 */
@Component
public class DailyRemoveCouponsTask {
	
	@Autowired
	AdminService adminService;
	
	@Scheduled(cron = "0 0 0 * * *")
	public void run() {
		deleteExpiredCoupons();
	}
	
	public void runAtApplicationStart() {
		deleteExpiredCoupons();
	}
	
	private void deleteExpiredCoupons() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println(">> begin scheduled coupon deletion >");
		System.out.println(">>>>>>> next one in: 00:00 >>>>>>>>>");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		adminService.deleteAllOutdatedCoupons(LocalDate.now());
	}
	
}
