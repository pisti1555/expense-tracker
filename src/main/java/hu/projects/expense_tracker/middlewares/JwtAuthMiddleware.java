package hu.projects.expense_tracker.middlewares;

import hu.projects.expense_tracker.common.exceptions.UnauthorizedException;
import hu.projects.expense_tracker.services.auth_token_service.AuthTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
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

@Component
@Slf4j
public class JwtAuthMiddleware extends OncePerRequestFilter {
    private final AuthTokenService authTokenService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthMiddleware(AuthTokenService authTokenService, UserDetailsService userDetailsService) {
        this.authTokenService = authTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No Bearer token found, skipping filter.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String username = authTokenService.getUsernameFromToken(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username == null) {
                logger.warn("Extracted username from JWT is null. Throwing UnauthorizedException.");
                throwUnauthorized();
            }

            if (authentication == null) {
                logger.debug("Authentication is null, creating a new one.");
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (!authTokenService.validateToken(jwt, userDetails)) {
                    logger.warn("ValidateToken() in JwtAuthMiddleware returned false. Throwing UnauthorizedException.");
                    throwUnauthorized();
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("Authentication successfully created.");
            }

            filterChain.doFilter(request, response);
        } catch(UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected exception occurred in JwtAuthMiddleware.", e);
            throwUnauthorized();
        }
    }

    private void throwUnauthorized() {
        throw new UnauthorizedException("Invalid JWT token.");
    }
}
