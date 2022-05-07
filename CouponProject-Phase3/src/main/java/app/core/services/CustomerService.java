package app.core.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.core.entities.Category;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;
import app.core.exceptions.CustomerServiceException;

@Service
@Transactional(rollbackFor = CouponSystemException.class)
@Scope("prototype")
public class CustomerService extends ClientService {

	private int customerId;

	@Override
	public boolean login(String email, String password) throws CustomerServiceException {
		try {
			Customer customer = customerRepository.findByEmailAndPassword(email, password);
			if (customer != null) {
				customerId = customer.getId();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new CustomerServiceException("customer Login() ERROR: " + e.getMessage());
		}
	}

	public void purchaseCoupon(Coupon coupon) throws CustomerServiceException {
		try {
			if (coupon.getAmount() <= 0) {
				throw new CustomerServiceException("coupon amount is 0");
			} else if (coupon.getEndDate().isBefore(LocalDate.now())) {
				throw new CustomerServiceException("coupon outdated");
			}
			// check if coupon already present in customer
			boolean present = customerRepository.existsByIdAndCoupons(customerId, coupon);
			if (present) {
				throw new CustomerServiceException("customer purchased the coupon already");
			} else {
				Customer c = customerRepository.getById(customerId);
				coupon.setAmount(coupon.getAmount() - 1);
				couponRepository.save(coupon);
				c.addCoupon(coupon);
			}
		} catch (Exception e) {
			throw new CustomerServiceException("purchaseCoupon() ERROR: " + e.getMessage());
		}
	}

	public List<Coupon> getCustomerCoupons(){
		try {
			List<Coupon> coupons = couponRepository.getByCustomers(customerRepository.getById(customerId));
			return coupons;
		} catch (Exception e) {
			System.err.println("getCustomerCoupons() ERROR: " + e.getMessage());
		}
		throw new CustomerServiceException("getCustomerCoupons() ERROR: couldn't retrive coupon list from DB.");
	}
	
	public List<Coupon> getCustomerCoupons(Category category){
		try {
			List<Coupon> coupons = couponRepository.getByCustomersAndCategory(customerRepository.getById(customerId), category);
			return coupons;
		} catch (Exception e) {
			System.err.println("getCustomerCoupons() ERROR: " + e.getMessage());
		}
		throw new CustomerServiceException("getCustomerCoupons() ERROR: couldn't retrive coupon list from DB.");
	}
	
	public List<Coupon> getCustomerCoupons(double price){
		try {
			List<Coupon> coupons = couponRepository.getByCustomersAndPriceLessThan(customerRepository.getById(customerId), price);
			return coupons;
		} catch (Exception e) {
			System.err.println("getCustomerCoupons() ERROR: " + e.getMessage());
		}
		throw new CustomerServiceException("getCustomerCoupons() ERROR: couldn't retrive coupon list from DB.");
	}
	
	public Customer getCustomerDetails() throws CustomerServiceException{
		Optional<Customer> opt = customerRepository.findById(this.customerId);
		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new CustomerServiceException("getCustomerDetails() ERROR: couldn't retrieve customer: " + this.customerId);
		}
	}
	
	public Customer getCustomerDetailsWithCoupons() throws CustomerServiceException{
		Customer customer = customerRepository.findByIdAndFetchCoupons(this.customerId);
		if (customer != null) {
			return customer;
		} else {
			throw new CustomerServiceException("getCustomerDetails() ERROR: couldn't retrieve customer: " + this.customerId);
		}
	}

	public boolean removeCoupon(Coupon coupon) throws CustomerServiceException{
		try {
			Customer currentCustomer = customerRepository.getById(this.customerId);
			if(currentCustomer != null && currentCustomer.getCoupons() != null) {
				if(currentCustomer.getCoupons().remove(coupon)) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		} catch (Exception e) {
			throw new CustomerServiceException("removeCoupon() ERROR: couldn't find customer with id: " + customerId);
		}
	}
}
