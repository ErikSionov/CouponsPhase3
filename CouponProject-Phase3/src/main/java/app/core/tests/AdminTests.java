package app.core.tests;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import app.core.LoginManager;
import app.core.entities.Category;
import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.services.AdminService;
import app.core.services.ClientService;

//@Component
@Order(1)
public class AdminTests implements CommandLineRunner {

	@Autowired
	AdminService adminService;

	@Autowired
	LoginManager loginManager;
	
	@Override
	public void run(String... args) throws Exception {

		System.out.println("================================");
		System.out.println(">>>>>>>>> ADMIN TESTS <<<<<<<<<<");

		doLoginManagerTest();
		doAdminLoginTestSuccess();
		doAdminLoginTestFail();
		doAddCompany();
		doAddCompanyWithSameEmailOrName();
		doUpdateCompany();
		doUpdateCompanyWithDiffrentName();
		doDeleteCompany();
		doGetCompany();
		doAddCustomer();
		doAddCustomerWithSameEmail();
		doDeleteCustomer();
		doUpdateCustomer();
		doGetCustomer();
		doGetCustomerWithCoupons();

		System.out.println("================================");
		System.out.println("");
	}

	private void doLoginManagerTest() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== login manager test");
		
		ClientService clientService = loginManager.login("admin@admin.com", "admin");
		if(clientService instanceof AdminService) {
			System.out.println("> test result SUCCESS: loginManager.login() returned adminService");
		}else {
			System.out.println("> test result Failed: check if login information present in application.properties, or have been changed");
		}
	}

	private void doAdminLoginTestSuccess() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin login() test with correct credentials");

		boolean check = adminService.login("admin@admin.com", "admin");
		if (check == true) {
			System.out.println("> test result SUCCESS: login successful");
		} else {
			System.err.println("> test result FAILED: couldn't login, check if login information present in application.properties");
		}
	}

	private void doAdminLoginTestFail() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin login() test with incorrect credentials");

		boolean check = adminService.login("", "");
		if (check == true) {
			System.err.println("> test result FAILED: check login() logic...");
		} else {
			System.out.println("> test result SUCCESS: couldn't login as intended");
		}
	}

	private void doAddCompany() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin addCompany() test with new company");
		
		Company testCompany = new Company(0, "testCompany1", "test1@mail", "12345", null);
		List<Coupon> couponList = List.of(//
				new Coupon(0, testCompany, Category.ELECTRONICS, "100$ coupon", "when buying appliances", LocalDate.of(2022, 12, 12), LocalDate.of(2024, 12, 12), 100, 25.50, "image01"), //
				new Coupon(0, testCompany, Category.TRAVEL, "zoo coupon", "when traveling with Air-Canada", LocalDate.of(2022, 12, 12), LocalDate.of(2024, 12, 12), 100, 25.50, "image01") //
		);
		testCompany.setCoupons(couponList);
		try {
			Company checkPresence = adminService.checkIfCompanyExistsByNameOrEmail(testCompany.getName(), testCompany.getEmail());
			if( checkPresence != null) {
				adminService.deleteCompany(checkPresence.getId());
			}
			adminService.addCompany(testCompany);
			System.out.println("> test result SUCCESS: added company: " + testCompany);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't add company: " + e.getMessage());
		}
	}

	private void doAddCompanyWithSameEmailOrName() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin addCompany() test with same name or email");

		Company testCompany = adminService.getCompany(adminService.getAllCompanies().get(0).getId());
		try {
			adminService.addCompany(testCompany);
			System.err.println("> test result FAILED: added company: " + testCompany);
		} catch (Exception e) {
			System.out.println("> test result SUCCESS: failed as intended: " + e.getMessage());
		}
	}

	private void doUpdateCompany() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin updateCompany() test with first company in DB");
		try {
			Company testCompany = adminService.getAllCompanies().get(0);
			testCompany.setEmail("testChange@mail");
			testCompany.setPassword("testChangePassword");
			adminService.updateCompany(testCompany);
			System.out.println("> test result SUCCESS: updated company: " + testCompany);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't update company: " + e.getMessage());
		}
	}

	private void doUpdateCompanyWithDiffrentName() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin updateCompany() test with changed name and changed id");
		try {
			Company testCompany = adminService.getAllCompanies().get(0);
			testCompany.setName("testChangeName");
			adminService.updateCompany(testCompany);
			System.out.println("> test result FAILED: updated company with diffrent name: " + testCompany);
			testCompany.setId(1);
			adminService.updateCompany(testCompany);
			System.err.println("> test result FAILED: updated company with diffrent Id: " + testCompany);
		} catch (Exception e) {
			System.out.println("> test result SUCCESS: failed as intended: " + e.getMessage());
		}
	}

	private void doDeleteCompany() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin deleteCompany() test");
		Company testCompany = new Company(0, "testDeleteCompany", "testDelete@mail", "12345", null);
		List<Coupon> couponList = List.of(//
				new Coupon(0, testCompany, Category.ELECTRONICS, "100$ coupon", "when buying appliances", LocalDate.of(2022, 12, 12), LocalDate.of(2024, 12, 12), 100, 25.50, "image01"), //
				new Coupon(0, testCompany, Category.TRAVEL, "zoo coupon", "when traveling with Air-Canada", LocalDate.of(2022, 12, 12), LocalDate.of(2024, 12, 12), 100, 25.50, "image01") //
		);
		testCompany.setCoupons(couponList);
		try {
			adminService.addCompany(testCompany);
			adminService.deleteCompany(testCompany.getId());
			System.out.println("> test result SUCCESS: deleted company: " + testCompany);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't delete company: " + e.getMessage());
		}
	}

	private void doGetCompany() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin getCompany() test with first company in DB");
		try {
			Company testCompany = adminService.getAllCompanies().get(0);
			adminService.getCompany(testCompany.getId());
			System.out.println("> test result SUCCESS: got company: " + testCompany);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't retrive company: " + e.getMessage());
		}
	}

	private void doAddCustomer() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin addCustomer() test with new customer");
		Customer testCustomer3 = new Customer(0, "testCustomer3", "testCustomer3", "test3@mail", "1245", null);
		List<Coupon> companyCoupons3 = adminService.getAllCompanyCoupons(adminService.getAllCompanies().get(0).getId());
		List<Coupon> customerCoupons3 = companyCoupons3;
		testCustomer3.setCoupons(customerCoupons3);
		try {
			adminService.addCustomer(testCustomer3);
			System.out.println("> test result SUCCESS: added customer: " + testCustomer3);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't add customer: " + e.getMessage());
		}
	}

	private void doAddCustomerWithSameEmail() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin doAddCustomerWithSameEmail() test with same email as other customer in DB");
		Customer testCustomer3 = adminService.getCustomer(adminService.getAllCustomer().get(0).getId());
		try {
			adminService.addCustomer(testCustomer3);
			System.err.println("> test result FAILED: added customer: " + testCustomer3);
		} catch (Exception e) {
			System.out.println("> test result SUCCESS: failed as intended: " + e.getMessage());
		}
	}

	private void doDeleteCustomer() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin deleteCustomer() test");
		Customer testCustomer3 = new Customer(0, "testDeleteCustomer", "testDeleteCustomer", "testDelete@mail", "1245", null);
		List<Coupon> companyCoupons3 = adminService.getAllCompanyCoupons(adminService.getAllCompanies().get(0).getId());
		System.out.println(companyCoupons3);
		List<Coupon> customerCoupons3 = companyCoupons3;
		testCustomer3.setCoupons(customerCoupons3);
		try {
			adminService.addCustomer(testCustomer3);
			adminService.deleteCustomer(testCustomer3.getId());
			System.out.println("> test result SUCCESS: deleted customer: " + testCustomer3);
		} catch (Exception e) {
			System.err.println("> test result FAILED: failed to delete customer: " + e.getMessage());
		}
	}

	private void doUpdateCustomer() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin updateCustomer() test with first customer in DB");
		Customer testCustomer = adminService.getCustomer(adminService.getAllCustomer().get(0).getId());
		testCustomer.setFirstName("testChangeCustomer");
		testCustomer.setEmail("testEmailChange@mail");
		List<Coupon> couponList = adminService.getAllCompanyCoupons(adminService.getAllCompanies().get(0).getId());
		testCustomer.setCoupons(couponList);
		try {
			adminService.updateCustomer(testCustomer);
			System.out.println("> test result SUCCESS: updated customer: " + testCustomer);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't update customer: " + e.getMessage());
		}
	}

	private void doGetCustomer() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin getCustomer() test with first customer in DB");
		try {
			Customer testCustomer = adminService.getCustomer(adminService.getAllCustomer().get(0).getId());
			System.out.println("> test result SUCCESS: retrieved customer: " + testCustomer);
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't retrieved customer: " + e.getMessage());
		}
	}

	private void doGetCustomerWithCoupons() {
		System.out.println("");
		System.out.println("================================");
		System.out.println("=== admin getCustomerWithCoupons() test with first customer in DB");
		try {
			Customer testCustomer = adminService.getCustomerWithCoupons(adminService.getAllCustomer().get(0).getId());
			System.out.println("> test result SUCCESS: retrieved customer: " + testCustomer);
			System.out.println("> with COUPONS: " + testCustomer.getCoupons());
		} catch (Exception e) {
			System.err.println("> test result FAILED: couldn't retrieved customer: " + e.getMessage());
		}
	}

}
