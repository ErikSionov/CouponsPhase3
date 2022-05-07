package app.core.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "coupons")
@Entity
public class Customer implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	@ManyToMany
	@JoinTable(
			name = "customers_vs_coupons",
			joinColumns = @JoinColumn(name = "customer_id"), 
			inverseJoinColumns = @JoinColumn(name = "coupon_id") 
			)
	@JsonIgnore
	private List<Coupon> coupons;


	public void addCoupon(Coupon coupon) {
		if(this.coupons == null) {
			this.coupons = new ArrayList<>();
		}
		this.coupons.add(coupon);
	}
	
}
