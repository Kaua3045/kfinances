package com.kaua.finances.infrastructure.security.jwt;

import com.kaua.finances.domain.account.AccountGateway;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtGateway jwtGateway;
    private final AccountGateway accountGateway;

    public JwtAuthenticationFilter(JwtGateway jwtGateway, AccountGateway accountGateway) {
        this.jwtGateway = jwtGateway;
        this.accountGateway = accountGateway;
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
        final var accountId = jwtGateway.extractId(jwtToken);

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

        filterChain.doFilter(request, response);
    }
}
