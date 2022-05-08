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

import app.core.ClientType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
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
	
	private String createToken(Map<String, Object> claims, String subject) {
		Instant now = Instant.now();
		Instant experationDate = now.plus(unitsNumber, ChronoUnit.valueOf(chronoUnits));

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date.from(now))
				.setExpiration(Date.from(experationDate)).signWith(key).compact();
	}
	
	public ClientDetails extractClient(String token) {
		Claims claims = extractAllClaims(token);
		int clientId = claims.get("clientId", Integer.class);
		String email = claims.getSubject();
		ClientType clientType = ClientType.valueOf(claims.get("clientType", String.class));
		return new ClientDetails(clientId, email, clientType);
	}
	
	public Claims extractAllClaims(String token) {
		JwtParser jwtParser;
		jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
		return jwtParser.parseClaimsJws(token).getBody();
	}
	
	public String extractEmail(String token) {
		return extractAllClaims(token).getSubject();
	}
	
	public int extractId(String token) {
		return extractAllClaims(token).get("clientId", Integer.class);
	}
	
	public String extractClientType(String token) {
		return extractAllClaims(token).get("clientType", String.class);
	}
	
}