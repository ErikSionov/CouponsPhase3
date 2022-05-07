package app.core.tests;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import app.core.entities.Category;
import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.services.AdminService;

//@Component
@Order(0)
public class PopulateDbTests implements CommandLineRunner {

	@Autowired
	AdminService adminService;

	@Override
	public void run(String... args) throws Exception {

		System.out.println("================================");
		System.out.println(">>>> POPULATING DB BEGAN <<<<");

		doAddCompanies();
		doAddCustomers();

		System.out.println("================================");
		System.out.println(">>>> POPULATING DB COMPLETED <<<<");
		System.out.println("");
	}
	
	private void doAddCompanies() {
		
		Company testCompany = new Company(0, "Apple", "apple@mail", "12345", null);
		List<Coupon> couponList = List.of(//
				new Coupon(0, testCompany, Category.ELECTRONICS, "free Iphone case", "when buying new models", LocalDate.of(2022, 12, 12), LocalDate.of(2024, 12, 12), 100, 25.50, "image01"), //
				new Coupon(0, testCompany, Category.TRAVEL, "free coffee", "when visiting Apple Stores", LocalDate.of(2022, 12, 12), LocalDate.of(2024, 12, 12), 100, 25.50, "image01") //
		);
		testCompany.setCoupons(couponList);
		
		Company testCompany2 = new Company(0, "Microsoft", "microsoft@mail", "12345", null);
		List<Coupon> couponList2 = List.of(//
				new Coupon(0, testCompany, Category.ELECTRONICS, "20$ coupon", "when buying office365 one year license", LocalDate.of(2022, 12, 12), LocalDate.of(2024, 12, 12), 100, 25.50, "image01"), //
				new Coupon(0, testCompany, Category.FOOD, "microsoft pankakes", "try our new product", LocalDate.of(2022, 12, 12), LocalDate.of(2024, 12, 12), 100, 25.50, "image01") //
		);
		testCompany2.setCoupons(couponList2);
		
		Company testCompany3 = new Company(0, "Telma", "telma@mail", "12345", null);
		List<Coupon> couponList3 = List.of(//
				new Coupon(0, testCompany, Category.FOOD, "3+1 yogurt", "one free when buying 3 telma products", LocalDate.of(2022, 12, 12), LocalDate.of(2024, 12, 12), 100, 25.50, "image01"), //
				new Coupon(0, testCompany, Category.FOOD, "30% off products", "when buying over 20$", LocalDate.of(2022, 12, 12), LocalDate.of(2024, 12, 12), 100, 25.50, "image01") //
		);
		testCompany3.setCoupons(couponList3);
		
		List<Company> companies = List.of(testCompany,testCompany2,testCompany3);
		
		for (Company company : companies) {
			try {
			Company check = adminService.checkIfCompanyExistsByNameOrEmail(company.getName(), company.getEmail()); 
				if(check != null){
					System.out.println("> company already present in db with name: " +company.getName());
					continue;
				}else {
					adminService.addCompany(company);
				}
			} catch (Exception e) {
				System.out.println("> populate DB FAILED: couldn't add company: " + company.getName() + " : " + e.getMessage());
			}
		}
		
	}
	

	private void doAddCustomers() {
		
		Customer testCustomer = new Customer(0, "Yossi", "Doron", "yossi@mail", "1245", null);
		List<Coupon> companyCoupons = adminService.getAllCompanyCoupons(adminService.getAllCompanies().get(0).getId());
		List<Coupon> customerCoupons = companyCoupons;
		testCustomer.setCoupons(customerCoupons);
		
		Customer testCustomer2 = new Customer(0, "Dani", "Aflalo", "dan@mail", "1245", null);
		List<Coupon> companyCoupons2 = adminService.getAllCompanyCoupons(adminService.getAllCompanies().get(1).getId());
		List<Coupon> customerCoupons2 = companyCoupons2;
		testCustomer2.setCoupons(customerCoupons2);
		
		Customer testCustomer3 = new Customer(0, "Nir", "Bloom", "nir@mail", "1245", null);
		List<Coupon> companyCoupons3 = adminService.getAllCompanyCoupons(adminService.getAllCompanies().get(1).getId());
		List<Coupon> customerCoupons3 = companyCoupons3;
		testCustomer3.setCoupons(customerCoupons3);
		
		List<Customer> customers = List.of(testCustomer,testCustomer2,testCustomer3);
		
		for (Customer customer : customers) {
			try {
			Customer check = adminService.getCustomerByEmail(customer.getEmail());
				if(check != null){
					System.out.println("> customer already present in db with name: " +customer.getFirstName());
					continue;
				}else {
					adminService.addCustomer(customer);
				}
			} catch (Exception e) {
				System.out.println("> populate DB FAILED: couldn't add customer: " + customer.getFirstName() + " : " + e.getMessage());
			}
		}
	}
}
