package app.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.core.exceptions.CouponSystemException;
import app.core.services.AdminService;
import app.core.services.ClientService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;

/**
 * Login manager to log into system using credentials from DB.
 * @author Erik Sionov
 *
 */
@Component
public class LoginManager {

	@Autowired
	private ApplicationContext ctx;

	/** used to login into the system.
	 *  While <code>ClientType</code> used to indicate the DB login() method used from one of the client services.
	 * 
	 * @param email
	 *            user email
	 * @param password
	 *            user password
	 * @param clientType
	 *            enum based on client types
	 * @return AdminService, CompanyService or CustomerService, based on ClientType enum used.
	 * @throws CouponSystemException
	 *             if login would fail for some reason
	 */
	public ClientService login(String email, String password) throws CouponSystemException {

		try {
			
			
			AdminService adminService = ctx.getBean(AdminService.class);
			if (adminService.login(email, password)) {
				return adminService;
			}
			adminService = null;
			
			CompanyService companyService = ctx.getBean(CompanyService.class);
			if (companyService.login(email, password)) {
				return companyService;
			}
			companyService = null;
			
			CustomerService customerService = ctx.getBean(CustomerService.class);
			if (customerService.login(email, password)) {
				return customerService;
			}
			customerService = null;
			return null;
			
		} catch (CouponSystemException e) {
			System.out.println("login() in loginManager failed");
			return null;
		}

	}

}
