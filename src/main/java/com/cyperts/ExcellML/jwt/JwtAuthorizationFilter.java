package com.cyperts.ExcellML.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.cyperts.ExcellML.UserAndRole.Role;
import com.cyperts.ExcellML.UserAndRole.User;
import com.cyperts.ExcellML.UserAndRole.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, ObjectMapper mapper, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, Object> errorDetails = new HashMap<>();

        try {
            String accessToken = jwtUtil.resolveToken(request);
            if (accessToken != null) {
                Claims claims = jwtUtil.resolveClaims(request);

                if (claims != null && jwtUtil.validateClaims(claims)) {
                    String email = claims.getSubject();
                    // Fetch the user from the database to get the roles
                    User user = userRepository.findByEmail(email);

                    if (user != null) {
//                        Role roles = user.getRoles();
//
//                        // Prefix roles with "ROLE_" if necessary
//                        SimpleGrantedAuthority authorities = new SimpleGrantedAuthority("ROLE_" + roles.getName());
//
//                        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        // Handle the case where the user is not found in the database
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        errorDetails.put("message", "User not found");
                        mapper.writeValue(response.getWriter(), errorDetails);
                        return;
                    }
                }
            }

        } catch (Exception e) {
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details", e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(response.getWriter(), errorDetails);

            return;
        }

        filterChain.doFilter(request, response);
    }

}
