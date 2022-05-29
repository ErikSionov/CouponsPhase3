package app.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import app.core.utils.ClientDetails;
import app.core.utils.Jwt;

public class LoginFilter implements Filter {

	private Jwt jwtUtil;

	public LoginFilter(Jwt jwtUtil) {
		super();
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String token = httpRequest.getHeader("token");
		System.out.println("LOGIN FILTER: token retrieved from header: " + token);
		
		if (token == null && httpRequest.getMethod().equals("OPTIONS")) {
			System.out.println("this is preflight request: " + httpRequest.getMethod());
			chain.doFilter(request, response);
			return;
		}
		try {
			// check if token extract-able
			ClientDetails clientDetails = jwtUtil.extractClient(token);
			
			System.out.println("LOGIN FILTER: filter extracted token: " + clientDetails);
			chain.doFilter(request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			httpResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "LOGIN FILTER ERROR:" + e.getMessage());
		}

	}

}
