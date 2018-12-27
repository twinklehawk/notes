package net.plshark.notes.webservice;

import com.auth0.jwt.JWTVerifier;
import net.plshark.auth.jwt.HttpBearerBuilder;
import net.plshark.auth.jwt.JwtReactiveAuthenticationManager;
import net.plshark.auth.throttle.IpThrottlingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

/**
 * Notes web security configuration
 */
@EnableWebFluxSecurity
public class WebSecurityConfig {

    // TODO
    private JWTVerifier jwtVerifier;

    /**
     * Set up the security filter chain
     * @param http the spring http security configurer
     * @return the filter chain
     */
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        HttpBearerBuilder builder = new HttpBearerBuilder(authenticationManager());
        http
            .authorizeExchange()
                .anyExchange()
                    .hasRole("notes-user")
            // use basic authentication
            .and()
                .authenticationManager(authenticationManager())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .csrf().disable()
                .logout().disable()
                .addFilterAt(ipThrottlingFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
                .addFilterAt(builder.buildFilter(), SecurityWebFiltersOrder.HTTP_BASIC);
        return http.build();
    }

    @Bean
    public JwtReactiveAuthenticationManager authenticationManager() {
        return new JwtReactiveAuthenticationManager(jwtVerifier);
    }

    private IpThrottlingFilter ipThrottlingFilter() {
        return new IpThrottlingFilter();
    }
}
