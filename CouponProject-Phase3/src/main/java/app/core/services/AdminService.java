package app.core.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.core.ClientType;
import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.exceptions.AdminServiceException;
import app.core.exceptions.CouponSystemException;

@Service
@Transactional(rollbackFor = CouponSystemException.class)
@Scope("prototype")
public class AdminService extends ClientService {

	@Value("${admin.email:admin@admin.com}")
	private String emailProp;
	@Value("${admin.pass:admin}")
	private String passProp;

	@Override
	public boolean login(String email, String password) throws AdminServiceException {
		try {
			if (email.equals(emailProp) && password.equals(passProp)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new AdminServiceException("admin Login() ERROR: " + e.getMessage());
		}
	}

	/**
	 * Adds a new company to the DB. the company mustn't have a similar name and
	 * email to existing company in the DB.
	 * 
	 * @param company company to be added.
	 * @throws AdminServiceException in case of error.
	 */
	public int addCompany(Company company) throws AdminServiceException {
		if (companyRepository.existsById(company.getId())) {
			throw new AdminServiceException("Company with same id is present");
		}
		try {
			List<Company> companies = companyRepository.findByNameOrEmail(company.getName(), company.getEmail());
			if (companies.isEmpty()) {
				companyRepository.save(company);
				System.out.println("company added with id: " + company.getId());
				return company.getId();
			} else {
				throw new AdminServiceException("Company with same name or email present in DB.");
			}
		} catch (Exception e) {
			throw new AdminServiceException("addCompany ERROR: " + e.getMessage());
		}
	}

	/**
	 * Updates already present company in DB, can't update Id or Name of the company
	 * 
	 * @param company company entity to that needs updating
	 * @throws AdminServiceException in case of error.
	 */
	public void updateCompany(Company company) throws AdminServiceException {
		try {
			Company c = getCompany(company.getId());
			if (c != null) {
				// check if company's name not changed
				if (company.getName().equals(c.getName())) {
					c.setEmail(company.getEmail());
					c.setPassword(company.getPassword());
					c.setCoupons(company.getCoupons());
				} else {
					throw new AdminServiceException("can't change company name.");
				}
			} else {
				throw new AdminServiceException("no company with id=" + company.getId() + " present in DB.");
			}
		} catch (Exception e) {
			throw new AdminServiceException("updateCompany ERROR: " + e.getMessage());
		}
	}

	/**
	 * Delete a company by it's companyId, while doing so ALSO will delete all
	 * company's coupons and their purchases.
	 * 
	 * @param companyId Company's id to delete
	 * @throws AdminServiceException if any error occurs.
	 */
	public void deleteCompany(int companyId) throws AdminServiceException {
		Company company = companyRepository.getById(companyId);
		if (company != null) {
			companyRepository.delete(company);
		} else {
			throw new AdminServiceException("no company with id=" + companyId + " present in DB.");
		}
	}

	/**
	 * Retrieves a list of all companies in the DB
	 * 
	 * @return list of Company() objects
	 */
	public List<Company> getAllCompanies() {
		return companyRepository.findAll();
	}

	/**
	 * Retrieves one company by it's Id from DB
	 * 
	 * @param companyId
	 * @return one Company() object
	 * @throws AdminServiceException
	 */
	public Company getCompany(int companyId) throws AdminServiceException {
		Optional<Company> opt = companyRepository.findById(companyId);
		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new AdminServiceException("getCompany ERROR: couldn't find company with id: " + companyId);
		}
	}

	public List<Company> getCompanyByNameOrEmail(String nameOrEmail) throws AdminServiceException {
		List<Company> list = companyRepository.findByNameContainingOrEmailContaining(nameOrEmail, nameOrEmail);
		if (!list.isEmpty()) {
			return list;
		} else {
			return list;
		}
	}

	/**
	 * Adds a new customer to the DB, mustn't have the same email as an existing
	 * customer
	 * 
	 * @param customer Customer() object to be added to the DB
	 * @throws AdminServiceException
	 */
	public int addCustomer(Customer customer) throws AdminServiceException {
		if (customerRepository.existsById(customer.getId())) {
			throw new AdminServiceException("customer with same id already exists");
		}
		try {
			List<Customer> list = customerRepository.findByFirstNameOrEmail(null, customer.getEmail());
			if (list.isEmpty()) {
				Customer cust = customerRepository.save(customer);
				System.out.println("customer added with id: " + customer.getId());
				return cust.getId();
			} else {
				throw new AdminServiceException("can't add customer with same email.");
			}
		} catch (Exception e) {
			throw new AdminServiceException("addCustomer ERROR: " + e.getMessage());
		}
	}

	/**
	 * Update a customer in DB, can't update it's Id.
	 * 
	 * @param customer customer() object to update.
	 * @throws AdminServiceException
	 */
	public void updateCustomer(Customer customer) throws AdminServiceException {
		try {
			Optional<Customer> opt = customerRepository.findById(customer.getId());
			if (opt.isPresent()) {
				opt.get().setFirstName(customer.getFirstName());
				opt.get().setLastName(customer.getLastName());
				opt.get().setEmail(customer.getEmail());
				opt.get().setPassword(customer.getPassword());
				opt.get().setCoupons(customer.getCoupons());
			} else {
				throw new AdminServiceException("no customer with id=" + customer.getId() + " present in DB.");
			}
		} catch (Exception e) {
			throw new AdminServiceException("updateCustomer ERROR: " + e.getMessage());
		}
	}

	/**
	 * Delete a customer from DB using it's customerId, also deletes all coupon
	 * purchases by the customer.
	 * 
	 * @param customerId customer's Id to delete
	 * @throws AdminServiceException
	 */
	public void deleteCustomer(int customerId) throws AdminServiceException {
		Customer customer = getCustomer(customerId);
		if (customer != null) {
			customerRepository.delete(customer);
		} else {
			throw new AdminServiceException("no customer with id=" + customerId + " present in DB.");
		}
	}

	/**
	 * Retrieves one customer() object WITHOUT! it's coupons from DB by its
	 * customerId,
	 * 
	 * @param customerId
	 * @return Customer() object from DB without it's coupons list.
	 * @throws AdminServiceException
	 */
	public Customer getCustomer(int customerId) throws AdminServiceException {
		Optional<Customer> opt = customerRepository.findById(customerId);
		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new AdminServiceException("getCustomer ERROR: couldn't find customer with id: " + customerId);
		}
	}

	/**
	 * Retrieves one customer() object from DB by its customerId with it's coupons.
	 * 
	 * @param customerId
	 * @return Customer() object from DB with coupon list.
	 * @throws AdminServiceException
	 */
	public Customer getCustomerWithCoupons(int customerId) throws AdminServiceException {
		Customer customer = customerRepository.findByIdAndFetchCoupons(customerId);
		if (customer != null) {
			return customer;
		} else {
			throw new AdminServiceException("getCustomer ERROR: couldn't find customer with id: " + customerId);
		}
	}

	/**
	 * Retrieves customer list based on search string
	 * 
	 * @param customerId
	 * @return Customer() object from DB with coupon list.
	 * @throws AdminServiceException
	 */
	public List<Customer> getCustomerSearch(String search) throws AdminServiceException {
		List<Customer> customers = new ArrayList<Customer>();
		try {
			//TODO ELDAR how to get suggested numbers instead of one, can't seem to get all which contains some form of number
			int searchId = Integer.parseInt(search);
			Optional<Customer> opt= customerRepository.findById(searchId);
			if(!opt.isEmpty()) {
				customers.add(opt.get());
			}
		} catch (NumberFormatException e1) {
			System.out.println("tried to parse a number but failed");
		}
		try {
			List<Customer> temp = customerRepository.findAllByFirstNameContainingOrEmailContainingOrLastNameContaining(search, search, search);
			customers.addAll(temp);
			return customers;
		} catch (Exception e) {
			throw new AdminServiceException("AdminService Error: failed to search for: " + search);
		}
	}

	/**
	 * Retrieves a list of customer() objects from DB
	 * 
	 * @return list of Customer() objects
	 */
	public List<Customer> getAllCustomer() {
		return customerRepository.findAll();
	}

	public void deleteAllOutdatedCoupons(LocalDate date) {
		couponRepository.deleteAllByEndDateLessThan(date);
	}

	public List<Coupon> getAllCompanyCoupons(int companyId) {
		List<Coupon> coupons = couponRepository.findAllByCompany_id(companyId);
		return coupons;
	};

	public List<Coupon> getAllCustomerCoupons(Customer customer) {
		List<Coupon> coupons = couponRepository.findAllByCustomers(customer);
		return coupons;
	}

	public Customer getCustomerByEmail(String email) {
		List<Customer> customers = customerRepository.findByFirstNameOrEmail(null, email);
		if (!customers.isEmpty()) {
			return customers.get(0);
		} else {
			return null;
		}
	}

	public Company checkIfCompanyExistsByNameOrEmail(String name, String email) {
		List<Company> companies = companyRepository.findByNameOrEmail(name, email);
		if (!companies.isEmpty()) {
			return companies.get(0);
		} else {
			return null;
		}
	}

	@Override
	public ClientType getClientType() {
		return ClientType.ADMINISTRATOR;
	}

	@Override
	public int getClientId() {
		return 0;
	}

	public List<Coupon> getAllCoupons() {
		List<Coupon> coupons = couponRepository.findAll();
		return coupons;
	}
}
