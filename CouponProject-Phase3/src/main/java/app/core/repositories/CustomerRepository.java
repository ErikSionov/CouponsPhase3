package app.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.core.entities.Coupon;
import app.core.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	List<Customer> findByFirstNameOrEmail(String name, String email);

	Customer findByEmailAndPassword(String email, String password);
	
	List<Customer> findAllByFirstNameContainingOrEmailContainingOrLastNameContaining(String firstName, String lastName, String email);
	
	boolean existsByIdAndCoupons(int customerId, Coupon coupon);
	
	 @Query("SELECT c FROM Customer c JOIN FETCH c.coupons WHERE c.id = (:id)")
	 Customer findByIdAndFetchCoupons(@Param("id") int id);
}
