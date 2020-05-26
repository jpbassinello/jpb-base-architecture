package br.com.jpb.config;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class EnhanceLogFilter implements Filter {

	private static final String REQUEST_URL = "requestURL";
	private static final String USERNAME = "username";
	private static final String METHOD = "method";

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		placeParameters(req);

		try {
			chain.doFilter(request, response);
		} finally {
			MDC.remove(REQUEST_URL);
			MDC.remove(METHOD);
			MDC.remove(USERNAME);
		}

	}

	private void placeParameters(HttpServletRequest req) {
		MDC.put(REQUEST_URL, getRequestURL(req));
		MDC.put(METHOD, req.getMethod());

		placeUsername(req);
	}

	private String getRequestURL(HttpServletRequest req) {
		String requestUri = (String) req.getAttribute("javax.servlet.forward.request_uri");
		String query;
		if (requestUri != null) {
			query = (String) req.getAttribute("javax.servlet.forward.query_string");
		} else {
			requestUri = req.getRequestURI();
			query = req.getQueryString();
		}

		if (query != null) {
			requestUri += "?" + query;
		}
		return requestUri;
	}

	private void placeUsername(HttpServletRequest req) {
		Principal principal = req.getUserPrincipal();
		if (principal != null) {
			String username = principal.getName();
			MDC.put(USERNAME, username);
		}
	}

	@Override
	public void destroy() {
	}
}
