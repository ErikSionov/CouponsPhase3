package app.core.tests;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import app.core.entities.Category;
import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.services.AdminService;
import app.core.services.CompanyService;

//@Component
@Order(2)
public class CompanyTests implements CommandLineRunner {

	@Autowired
	CompanyService companyService;

	@Autowired
	AdminService adminService;

	@Override
	public void run(String... args) throws Exception {

		System.out.println("================================");
		System.out.println(">>>>>>>>> COMPANY TESTS <<<<<<<<<<");

		doCompanyLoginTestFail();
		doCompanyLoginTestSuccess();
		doAddCoupon();
		doGetCoupon();
		doUpdateCoupon();
		doUpdateCouponWithDiffrentCompany();
		doDeleteCoupon();
		doGetCompanyDetails();

		System.out.println("================================");
		System.out.println("");
	}

	private void doCompanyLoginTestSuccess() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== company login test with correct credentials");

		Company testCompany = new Company(0, "couponTestingCompany", "coupons@mail", "coupons", null);
		try {
			adminService.addCompany(testCompany);
		} catch (Exception e) {
			testCompany = adminService.checkIfCompanyExistsByNameOrEmail(testCompany.getName(), testCompany.getEmail());
		}
		boolean check = companyService.login(testCompany.getEmail(), testCompany.getPassword());
		if (check == true) {
			System.out.println("> test result SUCCESS: login successful with company: " + testCompany);
		} else {
			System.err.println("> test result FAILED: couldn't login, check if any companies present in DB.");
		}
	}

	private void doCompanyLoginTestFail() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== company login test with incorrect credentials");

		boolean check = companyService.login("", "");
		if (check == true) {
			System.err.println("> test result FAILED: check login() logic...");
		} else {
			System.out.println("> test result SUCCESS: couldn't login as intended");
		}
	}

	private void doAddCoupon() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== company addCoupon() test with new coupon");
		Coupon testCoupon = new Coupon(0, null, Category.ELECTRONICS, "test addCoupon()", "testing addCoupon()", LocalDate.of(2022, 5, 5), LocalDate.of(2024, 1, 1), 100, 15.5, "testImage01.jpg");
		Coupon testCoupon2 = new Coupon(0, null, Category.ELECTRONICS, "test addCoupon() 2", "testing addCoupon() 2", LocalDate.of(2022, 5, 5), LocalDate.of(2024, 1, 1), 100, 15.5, "testImage01.jpg");
		Coupon testCoupon3 = new Coupon(0, null, Category.ELECTRONICS, "test addCoupon() 3", "testing addCoupon() 3", LocalDate.of(2022, 5, 5), LocalDate.of(2024, 1, 1), 100, 15.5, "testImage01.jpg");
		List<Coupon> list = List.of(testCoupon, testCoupon2, testCoupon3);
		for (Coupon coupon : list) {
			try {
				companyService.addCoupon(coupon);
				System.out.println("> test result SUCCESS: added coupon to company: " + coupon);
			} catch (Exception e) {
				System.err.println("> test result FAILED: couldn't add coupon: " + e.getMessage());
			}
		}
	}

	private void doGetCoupon() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== company getCoupon() test with first coupon of company from DB");
		try {
			int couponId = companyService.getCompanyCoupons().get(0).getId();
			Coupon testCoupon = companyService.getCoupon(couponId);
			System.out.println("> test result SUCCESS: retrieved coupon from company: " + testCoupon);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't get coupon from company: " + e.getMessage());
		}
	}

	private void doUpdateCoupon() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== company updateCoupon() test with first coupon of company from DB");
		try {
			int couponId = companyService.getCompanyCoupons().get(0).getId();
			Coupon testCoupon = companyService.getCoupon(couponId);
			testCoupon.setCategory(Category.HOME);
			testCoupon.setDescription("test description update");
			testCoupon.setTitle("check title update");
			companyService.updateCoupon(testCoupon);
			System.out.println("> test result SUCCESS: updated company coupon: " + testCoupon);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't update coupon from company: " + e.getMessage());
		}
	}

	private void doUpdateCouponWithDiffrentCompany() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== company updateCouponWithDiffrentCompany() test with first coupon of company from DB");
		try {
			int couponId = companyService.getCompanyCoupons().get(0).getId();
			Coupon testCoupon = companyService.getCoupon(couponId);
			testCoupon.setCategory(Category.HOME);
			testCoupon.setDescription("test description update");
			testCoupon.setTitle("check title update");
			Company testCompany = new Company(0, "testCouponCompany", "testcoupon@mail", "12345", null);
			testCoupon.setCompany(testCompany);
			companyService.updateCoupon(testCoupon);
			System.err.println("> test result FAILED: updated coupon with diffrent company entity: " + testCoupon);
		} catch (Exception e) {
			System.out.println("> test result SUCCESS: failed as intended: " + e.getMessage());
		}
	}

	private void doDeleteCoupon() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== company deleteCoupon() test with new coupon");
		Coupon testCoupon = new Coupon(0, null, Category.ELECTRONICS, "test deleteCoupon()", "testing deletecoupon()", LocalDate.of(2022, 5, 5), LocalDate.of(2024, 1, 1), 100, 15.5, "testDeleteImage01.jpg");
		try {
			companyService.addCoupon(testCoupon);
			companyService.deleteCoupon(testCoupon.getId());
			System.out.println("> test result SUCCESS: updated company coupon: " + testCoupon);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't update coupon from company: " + e.getMessage());
		}
	}

	private void doGetCompanyDetails() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== company getCompanyDetails() test with current logged in company");
		try {
			Company testCompany = companyService.getCompanyDetails();
			System.out.println("> test result SUCCESS: got company details: " + testCompany);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't retrive company details: " + e.getMessage());
		}
	}

}