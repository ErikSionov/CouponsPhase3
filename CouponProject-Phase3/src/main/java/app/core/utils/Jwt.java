package app.core.utils;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Jwt {
	
	private String signatureAlgo = SignatureAlgorithm.HS256.getJcaName();
	@Value("${jwt.util.secret.key}")
	private String encodedSecretKey;
	private Key key;
	@Value("${jwt.util.chrono.unit.number}")
	private int unitsNumber;
	@Value("${jwt.util.chrono.unit}")
	private String chronoUnits;
	
	@PostConstruct
	public void init() {
		// create encrypted key
		this.key = new SecretKeySpec(Base64.getDecoder().decode(encodedSecretKey), signatureAlgo);
	}
	
	public String generateToken(ClientDetails clientDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("clientType", clientDetails.getClientType());
		claims.put("clientId", clientDetails.getId());
		return createToken(claims, clientDetails.getEmail());
	}
	
	
	private String createToken(Map<String, Object> claims, String subject)
	{
		Instant now = Instant.now();
		Instant experationDate = now.plus(unitsNumber, ChronoUnit.valueOf(chronoUnits));
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(experationDate))
				.signWith(key)
				.compact();
	}
}