package app.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.LoginManager;
import app.core.services.ClientService;
import app.core.utils.ClientDetails;
import app.core.utils.Jwt;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	private LoginManager loginManager;
	private Jwt jwtUtil;
	
	public LoginController(LoginManager loginManager, Jwt jwtUtil) {
		super();
		this.jwtUtil = jwtUtil;
		this.loginManager = loginManager;
	}
	
	// TODO ELDAR ask why CORS is triggering all the time even when the @crossOrigin annotation is on
	@CrossOrigin("http://localhost:4200")
	@PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String login(@RequestParam String email, @RequestParam String password) {
		ClientService clientService = loginManager.login(email,password);
		if(clientService != null) {
		ClientDetails clientDetails = new ClientDetails(clientService.getClientId(),email,clientService.getClientType());
		return jwtUtil.generateToken(clientDetails);
		}else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user");
		}
	}

}
