package com.eduManage.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/api/auth/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

        	String token = header
        	        .replace("Bearer", "")
        	        .replace("\"", "")
        	        .trim();

//
            try {
                String email = jwtUtil.extractUsername(token);
                String role  = jwtUtil.extractRole(token); // ADMIN / INSTRUCTOR

                if (email != null &&
                    role != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority("ROLE_"+role);
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    List.of(authority)
                            );

                    auth.setDetails(request); // ✅ ADD THIS LINE

                    

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("AUTH = " +
                    	    SecurityContextHolder.getContext()
                    	        .getAuthentication()
                    	        .getAuthorities());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }
}
