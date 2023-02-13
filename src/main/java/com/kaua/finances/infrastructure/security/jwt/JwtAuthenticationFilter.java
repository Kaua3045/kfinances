package com.kaua.finances.infrastructure.security.jwt;

import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.authenticate.SecurityGateway;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityGateway jwtGateway;
    private final AccountGateway accountGateway;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthenticationFilter(SecurityGateway jwtGateway, AccountGateway accountGateway, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtGateway = jwtGateway;
        this.accountGateway = accountGateway;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        final var authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final var jwtToken = authHeader.substring(7);

        try {
            final var accountId = jwtGateway.extractSubject(jwtToken);

            if (accountId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                final var aAccount = accountGateway.findById(accountId);

                if (aAccount.isEmpty()) {
                    filterChain.doFilter(request, response);
                    return;
                }

                if (jwtGateway.isTokenValid(jwtToken, aAccount.get().getId())) {
                    final var accountAuthenticated = new UsernamePasswordAuthenticationToken(
                            aAccount,
                            null,
                            new ArrayList<>()
                    );

                    accountAuthenticated.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(accountAuthenticated);
                }
            }
        } catch (JwtException ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }

        filterChain.doFilter(request, response);
    }
}
