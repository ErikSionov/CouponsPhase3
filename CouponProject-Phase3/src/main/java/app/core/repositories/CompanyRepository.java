package app.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.core.entities.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer>{

	List<Company> findByNameOrEmail(String name, String email);
	
	List<Company> findByNameContainingOrEmailContaining(String name, String Email);
	
	Company findByEmailAndPassword(String email, String password);
	
}
