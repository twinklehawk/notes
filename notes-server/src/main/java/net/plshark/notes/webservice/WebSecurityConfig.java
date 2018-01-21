package net.plshark.notes.webservice;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import net.plshark.auth.throttle.LoginAttemptService;
import net.plshark.auth.throttle.LoginAttemptThrottlingFilter;
import net.plshark.auth.throttle.LoginThrottlingConfig;
import net.plshark.auth.throttle.impl.BasicAuthenticationUsernameExtractor;

/**
 * Notes web security configuration
 */
@Configuration
@Import(LoginThrottlingConfig.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private UserDetailsService userDetailsService;
    @Inject
    private LoginAttemptService loginAttemptService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/users/**", "/roles/**")
                    .hasRole("notes-admin")
                .anyRequest()
                    .hasRole("notes-user")
            // use basic authentication
            .and().httpBasic()
            .and().addFilterBefore(loginAttemptThrottlingFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * @return the encoder to use to encode passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private LoginAttemptThrottlingFilter loginAttemptThrottlingFilter() {
        return new LoginAttemptThrottlingFilter(loginAttemptService, new BasicAuthenticationUsernameExtractor());
    }
}
