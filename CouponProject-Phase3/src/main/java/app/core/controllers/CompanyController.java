package app.core.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.core.entities.Company;
import app.core.services.CompanyService;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
	
	private CompanyService companyService;
	
	public CompanyController(CompanyService companyService) {
		super();
		this.companyService = companyService;
	}

	
	@GetMapping
	public Company getCompanyInfo() {
		
		return companyService.getCompanyDetails();
	}
	
}
