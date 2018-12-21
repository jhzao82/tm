package org.strangeforest.tcb.stats.controller;

import javax.servlet.http.*;

import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.stereotype.*;
import org.springframework.web.servlet.handler.*;

@Component @ConditionalOnProperty("tennis-stats.down-for-maintenance")
public class DownForMaintenanceInterceptor extends HandlerInterceptorAdapter {

	private static final String MAINTENANCE_PATH = "/maintenance";

	@Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String servletPath = request.getServletPath();
		if (!(servletPath.equals(MAINTENANCE_PATH) || servletPath.startsWith("/images"))) {
			response.sendRedirect(request.getContextPath() + MAINTENANCE_PATH);
			return false;
		}
		else
			return true;
	}
}
