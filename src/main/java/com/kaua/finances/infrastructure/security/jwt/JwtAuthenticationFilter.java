package com.kaua.finances.infrastructure.security.jwt;

import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.application.usecases.account.output.AccountOutput;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountCacheGateway;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.authenticate.SecurityGateway;
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
import java.util.Objects;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityGateway jwtGateway;
    private final AccountGateway accountGateway;
    private final AccountCacheGateway accountCacheGateway;

    public JwtAuthenticationFilter(SecurityGateway jwtGateway, AccountGateway accountGateway, AccountCacheGateway accountCacheGateway) {
        this.jwtGateway = jwtGateway;
        this.accountGateway = accountGateway;
        this.accountCacheGateway = accountCacheGateway;
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

        final var accountId = jwtGateway.extractSubject(jwtToken);

        if (accountId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (this.accountCacheGateway.findById(accountId).isEmpty() && this.accountGateway.findById(accountId).isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            // TODO: mover talvez para um método especifico, privado nessa classe ou talvez usar um usecase pra isso
            final var aAccount = this.accountCacheGateway.findById(accountId)
                    .orElseGet(() -> {
                        final var accountFindDb = this.accountGateway.findById(accountId).get();

                        this.accountCacheGateway.create(accountFindDb);

                       return AccountOutput.from(accountFindDb);
                    });

            if (jwtGateway.isTokenValid(jwtToken, aAccount.id())) {
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
