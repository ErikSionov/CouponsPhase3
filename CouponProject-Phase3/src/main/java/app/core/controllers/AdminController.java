package app.core.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.exceptions.AdminServiceException;
import app.core.services.AdminService;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/admin")
public class AdminController {

	private AdminService adminService;

	public AdminController(AdminService adminService) {
		super();
		this.adminService = adminService;
	}

	@GetMapping("/company/{companyId}")
	public Company getCompany(@PathVariable int companyId, @RequestHeader String token) {
		try {
			return adminService.getCompany(companyId);
		} catch (AdminServiceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PostMapping("/company/add")
	public int addCompany(@RequestBody Company company, @RequestHeader String token) {
		try {
			return adminService.addCompany(company);
		} catch (AdminServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@DeleteMapping("/company/delete/{companyId}")
	public void deleteCompany(@PathVariable int companyId, @RequestHeader String token) {
		try {
			adminService.deleteCompany(companyId);
		} catch (AdminServiceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PutMapping("/company/update")
	public void updateCompany(@RequestBody Company company, @RequestHeader String token) {
		try {
			adminService.updateCompany(company);
		} catch (AdminServiceException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@GetMapping("/company/all")
	public List<Company> getAllCompanies(@RequestHeader String token) {
		return adminService.getAllCompanies();
	}
	
	@GetMapping("/company/search/{company}")
	public List<Company> searchAllCompanies(@PathVariable String company, @RequestHeader String token) {
		try {
			return adminService.getCompanyByNameOrEmail(company);
		} catch (AdminServiceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@GetMapping("/company/search/")
	public List<Company> searchAllCompanies(@RequestHeader String token) {
		return adminService.getAllCompanies();
	}
	
	@PostMapping("/customer/add")
	public int addCustomer(@RequestBody Customer customer, @RequestHeader String token) {
		try {
			return adminService.addCustomer(customer);
		} catch (AdminServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/customer/{customerId}")
	public Customer getCustomer(@PathVariable int customerId, @RequestHeader String token) {
		try {
			return adminService.getCustomer(customerId);
		} catch (AdminServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
 
	@GetMapping("/customer/allcoupons/{customerId}")
	public List<Coupon> getCustomerCoupons(@PathVariable int customerId, @RequestHeader String token) {
		try {
			return adminService.getAllCustomerCoupons(adminService.getCustomer(customerId));
		} catch (AdminServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/customer/allcoupons")
	public List<Coupon> getAllCoupons(@RequestHeader String token) {
		try {
			return adminService.getAllCoupons();
		} catch (AdminServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PutMapping("/customer/update")
	public void updateCustomer(@RequestBody Customer customer, @RequestHeader String token) {
		try {
			adminService.updateCustomer(customer);
		} catch (AdminServiceException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@DeleteMapping("/customer/delete/{customerId}")
	public void deleteCustomer(@PathVariable int customerId, @RequestHeader String token) {
		try {
			adminService.deleteCustomer(customerId);
		} catch (AdminServiceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@GetMapping("/customer/all")
	public List<Customer> getAllCustomers(@RequestHeader String token) {
		try {
			return adminService.getAllCustomer();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/customer/search/{search}")
	public List<Customer> searchforCustomers(@PathVariable String search, @RequestHeader String token){
		return this.adminService.getCustomerSearch(search);
	}
	
	@GetMapping("/customer/search/")
	public List<Customer> searchforCustomersAll(@RequestHeader String token){
		return this.adminService.getAllCustomer();
	}
	
}
