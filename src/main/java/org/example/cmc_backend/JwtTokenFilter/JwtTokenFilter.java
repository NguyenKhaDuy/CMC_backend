package org.example.cmc_backend.JwtTokenFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.cmc_backend.Entity.UserEntity;
import org.example.cmc_backend.Utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtTokenUtils jwtTokenUtils;
    @Autowired
    UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (isBypassToken(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);

        System.out.println("TOKEN: " + token);

        if (token == null || token.isBlank()) {
            System.out.println("Token null hoặc rỗng");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(jwtTokenUtils.getSignInKey())
                    .parseClaimsJws(token)
                    .getBody();

            final String email = claims.getSubject();

            if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                Authentication authentication =
                        SecurityContextHolder.getContext().getAuthentication();

                System.out.println(authentication);

                UserEntity userEntity = (UserEntity) userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userEntity, null, userEntity.getAuthorities()
                        );

                auth.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }

        } catch (Exception e) {

            System.out.println("JWT FILTER ERROR: " + e.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request) {

//        String path = request.getRequestURI();
//
//        if (path.startsWith("/swagger-ui")
//                || path.startsWith("/v3/api-docs")
//                || path.startsWith("/swagger-resources")
//                || path.startsWith("/webjars")) {
//            return true;
//        }
//
//        if (path.startsWith("/favicon.ico")
//                || path.startsWith("/css/")
//                || path.startsWith("/js/")
//                || path.startsWith("/images/")) {
//            return true;
//        }
//
//        if (path.startsWith("/login") || path.startsWith("/register")) {
//            return true;
//        }
//
//        final List<String> publicApis = Arrays.asList(
//                "/api/forgotpassword",
//                "/api/login",
//                "/api/logout",
//                "/api/resend-otp",
//                "/api/register",
//                "/api/verify-otp",
//                "/api/actor/",
//                "/api/voucher/",
//                "/api/schedule/",
//                "/api/movie",
//                "/api/category",
//                "/api/branch",
//                "/api/seat",
//                "/swagger-ui/**",
//                "/v3/api-docs/**",
//                "/api/send-otp/forgotPassword",
//                "/api/forgot/password",
//                "/api/resend-otp"
//        );
//
//        for (String api : publicApis) {
//            if (path.startsWith(api)) {
//                return true;
//            }
//        }

        return true;
    }
}
