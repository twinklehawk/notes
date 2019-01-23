package net.plshark.notes.webservice;

import net.plshark.auth.jwt.HttpBearerBuilder;
import net.plshark.auth.jwt.JwtReactiveAuthenticationManager;
import net.plshark.auth.service.AuthService;
import net.plshark.auth.service.AuthServiceClient;
import net.plshark.auth.throttle.IpThrottlingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Notes web security configuration
 */
@EnableWebFluxSecurity
public class WebSecurityConfig {

    /**
     * Set up the security filter chain
     * @param http the spring http security configurer
     * @return the filter chain
     */
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http, JwtReactiveAuthenticationManager authenticationManager) {
        HttpBearerBuilder builder = new HttpBearerBuilder(authenticationManager);
        http
            .authorizeExchange()
                .anyExchange()
                    .hasRole("notes-user")
            .and()
                .authenticationManager(authenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .csrf().disable()
                .logout().disable()
                .addFilterAt(ipThrottlingFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
                .addFilterAt(builder.buildFilter(), SecurityWebFiltersOrder.HTTP_BASIC);
        return http.build();
    }

    @Bean
    public JwtReactiveAuthenticationManager authenticationManager(AuthService authService) {
        return new JwtReactiveAuthenticationManager(authService);
    }

    @Bean
    public AuthService authService(WebClient webClient, @Value("auth.host") String authHost) {
        return new AuthServiceClient(webClient, authHost);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    private IpThrottlingFilter ipThrottlingFilter() {
        return new IpThrottlingFilter();
    }
}
