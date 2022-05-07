package app.core.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import app.core.LoginManager;

@RestController
public class LoginController {
	
	private LoginManager loginManager;
	
	public LoginController(LoginManager loginManager) {
		super();
		this.loginManager = loginManager;
	}
	
	@PutMapping("/login/{email}/{password}")
	public String login(@PathVariable String email, @PathVariable String password) {
		loginManager.login(email,password);
		return "success";
	}
	
}
