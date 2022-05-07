package app.core.tests;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.services.AdminService;
import app.core.services.CustomerService;

//@Component
@Order(3)
public class CustomerTests implements CommandLineRunner {

	@Autowired
	CustomerService customerService;

	@Autowired
	AdminService adminService;

	@Override
	public void run(String... args) throws Exception {

		System.out.println("================================");
		System.out.println(">>>>>>>>> CUSTOMER TESTS <<<<<<<<<<");

		doCustomerLoginTestFail();
		doCustomerLoginTestSuccess();
		doPurchaseCoupon();
		doPurchaseCouponAlreadyPresentInCustomer();
		doGetCustomerCoupons();
		doRemoveCoupon();
		doGetCustomerDetails();
		
		System.out.println("================================");
		System.out.println("");
	}

	private void doCustomerLoginTestSuccess() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== customer login test with correct credentials");

		Customer testCustomer = new Customer(0, "testCustomerService", "testCustomerService", "testCustomerService@mail", "12345", null);
		try {
			adminService.addCustomer(testCustomer);
		} catch (Exception e) {
			testCustomer = adminService.getCustomerByEmail(testCustomer.getEmail());
		}
		boolean check = customerService.login(testCustomer.getEmail(), testCustomer.getPassword());
		if (check == true) {
			System.out.println("> test result SUCCESS: login successful with customer: " + testCustomer);
		} else {
			System.err.println("> test result FAILED: couldn't login, check if any customers present in DB.");
		}
	}

	private void doCustomerLoginTestFail() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== customer login test with incorrect credentials");

		boolean check = customerService.login("", "");
		if (check == true) {
			System.err.println("> test result FAILED: check login() logic...");
		} else {
			System.out.println("> test result SUCCESS: couldn't login as intended");
		}
	}

	private void doPurchaseCoupon() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== customer purchaseCoupon() test with a coupon from DB");
		List<Coupon> testCoupons = adminService.getAllCompanyCoupons(adminService.getAllCompanies().get(0).getId());
		try {
			customerService.purchaseCoupon(testCoupons.get(0));
			Customer testCustomer = customerService.getCustomerDetailsWithCoupons();
			System.out.println("> test result SUCCESS: customerId=" + testCustomer.getId() + " purchased coupon as intended: " + testCoupons.get(0));
			System.out.println("> check presence of coupon in customer: " + testCustomer.getCoupons());
			if (customerService.removeCoupon(testCoupons.get(0))) {
				System.out.println("> removed tested coupon from DB to allow the test to pass next run");
			}
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't purchase coupon: " + e.getMessage());
		}
	}

	private void doPurchaseCouponAlreadyPresentInCustomer() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== customer purchaseCoupon() test with a coupon already present in customers_vs_coupons table");
		List<Coupon> couponsFromDb = adminService.getAllCompanyCoupons(adminService.getAllCompanies().get(0).getId());
		try {
			customerService.purchaseCoupon(couponsFromDb.get(0));
			Customer testCustomer = customerService.getCustomerDetailsWithCoupons();
			List<Coupon> testCoupons = testCustomer.getCoupons();
			customerService.purchaseCoupon(testCoupons.get(0));
			System.err.println("> test result FAILED: customerId=" + testCustomer.getId() + " purchased another copy of coupon: " + testCoupons.get(0));
			System.err.println("> check presence of coupon in customer: " + testCustomer.getCoupons());
			if (customerService.removeCoupon(testCoupons.get(0))) {
				System.err.println("> removed tested coupon from DB to allow the test to pass next run");
			}
		} catch (Exception e) {
			System.out.println("> test result SUCCESS: failed as intended: " + e.getMessage());
		}
	}

	private void doGetCustomerCoupons() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== customer getCustomerCoupons() test with customer coupons from DB");
		try {
			List<Coupon> testList = customerService.getCustomerCoupons();
			System.out.println("> test result SUCCESS: retrieved a list of coupons:");
			System.out.println("> " + testList);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't retrieve the list of coupons: " + e.getMessage());
		}
	}
	
	private void doRemoveCoupon() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== customer removeCoupon() test with customer coupon from db");
		try {
			List<Coupon> testList = customerService.getCustomerCoupons();
			customerService.removeCoupon(testList.get(0));
			System.out.println("> test result SUCCESS: deleted the first coupon in customers coupon list from DB");
			System.out.println("> " + testList);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't delete the coupon from DB: " + e.getMessage());
		}
	}
	
	private void doGetCustomerDetails() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== customer getCustomerDetails() test with customer coupon from db");
		try {
			Customer testCustomer = customerService.getCustomerDetails();
			System.out.println("> test result SUCCESS: retrieved details of customer: " + testCustomer);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't retrieve the details of customer from DB: " + e.getMessage());
		}
	}
	
}
