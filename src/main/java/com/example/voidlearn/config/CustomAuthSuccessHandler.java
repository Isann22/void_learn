package com.example.voidlearn.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl = determineTargetUrl(authentication, request);

        request.getSession().setAttribute("title", getTitleForRole(authentication));
        request.getSession().setAttribute("view", getViewForRole(authentication));
        request.getSession().setAttribute("viewHTML", getViewHtmlForRole(authentication));

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(Authentication authentication, HttpServletRequest request) {
        if (authentication.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ADMIN"))) {
            return "/admin/dashboard";
        } else if (authentication.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("USER"))) {
            return "/user/dashboard";
        } else {
            return "/";
        }
    }

    private String getTitleForRole(Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ADMIN"))) {
            return "Admin Dashboard";
        } else {
            return "User Dashboard";
        }
    }

    private String getViewForRole(Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ADMIN"))) {
            return "admin/dashboard";
        } else {
            return "user/dashboard";
        }
    }

    private String getViewHtmlForRole(Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ADMIN"))) {
            return "admin-dashboard";
        } else {
            return "user-dashboard";
        }
    }
}
