package app.core.filters;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class DelayFilter implements Filter {

	private long milis;

	public DelayFilter(long milis) {
		super();
		this.milis = milis;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			System.out.println("DELAY FILTER");
			TimeUnit.MILLISECONDS.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		chain.doFilter(request, response);
	}


}
