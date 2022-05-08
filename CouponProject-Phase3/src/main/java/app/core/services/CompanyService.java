package app.core.services;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.core.ClientType;
import app.core.entities.Category;
import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.exceptions.CompanyServiceException;
import app.core.exceptions.CouponSystemException;

@Service
@Transactional(rollbackFor = CouponSystemException.class)
@Scope("prototype")
public class CompanyService extends ClientService {

	private int companyId;
	
	@Override
	public boolean login(String email, String password) throws CompanyServiceException {
		try {
			Company company = companyRepository.findByEmailAndPassword(email, password);
			if (company != null) {
				companyId = company.getId();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new CompanyServiceException("company Login() ERROR: " + e.getMessage());
		}
	}

	public void addCoupon(Coupon coupon) throws CompanyServiceException {
		try {
			// set coupon's Company field to this CompanyService user companyId
			coupon.setCompany(companyRepository.getById(this.companyId));
			// check if companyId and title match
			if (couponRepository.existsByCompanyIdAndTitle(coupon.getCompany().getId(), coupon.getTitle())) {
				throw new CompanyServiceException("addCoupon ERROR: can't add coupon with same title to same company");
			} else {
				couponRepository.save(coupon);
			}
		} catch (CompanyServiceException e) {
			throw new CompanyServiceException("addCoupon ERROR: couldn't add coupon to DB. " + e.getMessage());
		}
	}

	public Coupon getCoupon(int couponId) throws CompanyServiceException {
		Optional<Coupon> opt = couponRepository.findById(couponId);
		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new CompanyServiceException("getCoupon ERROR: couldn't find coupon with id: " + couponId);
		}
	}

	public void updateCoupon(Coupon coupon) throws CompanyServiceException {
		try {
			// check if coupon with this id present in DB
			Coupon c = getCoupon(coupon.getId());
			if (c != null) {
				// check if couponId and companyId match
				if (coupon.getCompany().equals(c.getCompany())) {
					c.setTitle(coupon.getTitle());
					c.setDescription(coupon.getDescription());
					c.setCategory(coupon.getCategory());
					c.setStartDate(coupon.getStartDate());
					c.setEndDate(coupon.getEndDate());
					c.setAmount(coupon.getAmount());
					c.setPrice(coupon.getPrice());
					c.setImage(coupon.getImage());
					c.setCustomers(coupon.getCustomers());
				} else {
					throw new CompanyServiceException("can't update Company field in Coupon DB.");
				}
			} else {
				throw new CompanyServiceException("no coupon with id=" + coupon.getId() + " present in DB.");
			}
		} catch (Exception e) {
			throw new CompanyServiceException("updateCoupon ERROR: " + e.getMessage());
		}
	}

	public void deleteCoupon(int couponId) throws CompanyServiceException {
		Coupon coupon = getCoupon(couponId);
		if (coupon != null) {
			couponRepository.delete(coupon);
		} else {
			throw new CompanyServiceException("no coupon with id=" + couponId + " present in DB.");
		}
	}

	/**used to return the company information of current user's companyId.
	 * @return Company object from database.
	 * @throws CompanyServiceException if login wasn't successful or companyId not present in DB.
	 */
	public Company getCompanyDetails() throws CompanyServiceException {
		Optional<Company> opt = companyRepository.findById(this.companyId);
		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new CompanyServiceException("getCompanyDetails() ERROR: couldn't find company with id: " + this.companyId);
		}
	}
	
	/**return a list of coupon objects with companyId of current user's companyId, from a database.
	 * @return List of coupon or empty array if no coupons found from this company.
	 */
	public List<Coupon> getCompanyCoupons() {
		List<Coupon> list = couponRepository.findAllByCompany_id(this.companyId);
		return list;
	}

	/**return a list of coupon objects from a single category of current user companyId, from a database.
	 * @param category - enumerator used in DB and coupon entity
	 * @return List of coupon or empty array if no coupons found with same parameter.
	 */
	public List<Coupon> getCompanyCoupons(Category category) {
		List<Coupon> list = couponRepository.findAllByCategoryAndCompany_id(category, this.companyId);
		return list;
	}

	/**return a list of coupon objects up to a price of current user companyId, from a database.
	 * @param price - the max price to search for, exclusive.
	 * @return List of coupon or empty array if no coupons found with same parameter.
	 */
	public List<Coupon> getCompanyCoupons(double price) {
		List<Coupon> list = couponRepository.findAllByCompany_idAndPriceLessThan(this.companyId, price);
		return list;
	}

	public Coupon getCouponByTitle(String title) {
		Coupon coupon = couponRepository.getByTitle(title);
		return coupon;
	}

	@Override
	public ClientType getClientType() {
		return ClientType.COMPANY;
	}

	@Override
	public int getClientId() {
		return companyId;
	}

}
