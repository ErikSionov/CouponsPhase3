package app.core.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.core.entities.Category;
import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Customer;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	boolean existsByCompanyIdAndTitle(int companyId, String title);

	List<Coupon> findAllByCompany_id(int companyId);

	List<Coupon> findAllByCategoryAndCompany_id(Category category, int companyId);

	List<Coupon> findAllByCompany_idAndPriceLessThan(int companyId, double price);

	List<Coupon> getByCustomers(Customer customer);

	List<Coupon> getByCustomersAndCategory(Customer byId, Category category);

	List<Coupon> getByCustomersAndPriceLessThan(Customer byId, double price);

	List<Coupon> getByEndDateLessThan(LocalDate date);
	
	void deleteAllByEndDateLessThan(LocalDate date);
	
	void deleteAllByCompany(Company company);

	List<Coupon> findAllByCustomers(Customer customer);

	Coupon getByTitle(String title);
	
}
