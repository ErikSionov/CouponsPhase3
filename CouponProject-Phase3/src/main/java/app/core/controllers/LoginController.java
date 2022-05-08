package app.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

import app.core.ClientType;
import app.core.LoginManager;
import app.core.services.AdminService;
import app.core.services.ClientService;
import app.core.utils.ClientDetails;
import app.core.utils.Jwt;

@RestController
public class LoginController {
	
	private LoginManager loginManager;
	private Jwt jwtUtil;
	
	public LoginController(LoginManager loginManager, Jwt jwtUtil) {
		super();
		this.jwtUtil = jwtUtil;
		this.loginManager = loginManager;
	}
	
	// TODO ELDAR ask how to send the email/password not through path variable
	@PutMapping("/login/{email}/{password}")
	public String login(@PathVariable String email, @PathVariable String password) {
		ClientService clientService = loginManager.login(email,password);
		if(clientService != null) {
		ClientDetails clientDetails = new ClientDetails(clientService.getClientId(),email,clientService.getClientType());
		return jwtUtil.generateToken(clientDetails);
		}else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user");
		}
	}
	
}
