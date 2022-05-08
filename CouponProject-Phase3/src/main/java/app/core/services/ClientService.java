package app.core.services;

import org.springframework.beans.factory.annotation.Autowired;

import app.core.ClientType;
import app.core.exceptions.CouponSystemException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

public abstract class ClientService {
	
	@Autowired
	protected CompanyRepository companyRepository;
	@Autowired
	protected CouponRepository couponRepository;
	@Autowired
	protected CustomerRepository customerRepository;
	
	/**	used to log into the system. Admin credentials are preset in the properties file. other credentials are IO based.
	 * @param email user email
	 * @param password user password
	 * @return True if credentials correct, False if credentials of this type haven't been found.
	 * @throws CouponSystemException in case of any error.
	 */
	public abstract boolean login(String email, String password) throws CouponSystemException;
	
	
	/** used in API to retrieve the clientType if necessary.
	 * @return ClientType enum value of the used service
	 */
	public abstract ClientType getClientType();
	
	/** used in API to retrieve the id number if necessary, for login purposes.
	 * @return the Id number of the user from DB
	 */
	public abstract int getClientId();
}
