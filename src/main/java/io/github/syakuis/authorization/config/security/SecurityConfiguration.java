package io.github.syakuis.authorization.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(EncodeId.BCRYPT.name(), new BCryptPasswordEncoder());
        encoders.put(EncodeId.PBKDF2.name(), new Pbkdf2PasswordEncoder());
        encoders.put(EncodeId.SCRYPT.name(), new SCryptPasswordEncoder());

        return new DelegatingPasswordEncoder(EncodeId.BCRYPT.name(), encoders);
    }

    /**
     * protected void configure(AuthenticationManagerBuilder auth)
     *         throws Exception {
     *         auth.inMemoryAuthentication()
     *             .withUser("test").password(passwordEncoder().encode("1234"))
     *             .roles("USER")
     *             .and()
     *             .withUser("admin").password(passwordEncoder().encode("!@#$"))
     *             .roles("ADMIN", "USER");
     *
     *     }
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService aUserDetailsService() {
        UserDetails user = User.builder()
            .username("test").password(passwordEncoder().encode("1234"))
            .roles("USER")
            .build();
        UserDetails admin = User.builder()
            .username("admin").password(passwordEncoder().encode("!@#$"))
            .roles("ADMIN", "USER")
            .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(aUserDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and()
            .authorizeRequests()
            .anyRequest().fullyAuthenticated()
            .and()
            .formLogin(form -> form.loginPage("/login").permitAll())
            .httpBasic(withDefaults())
        ;
    }
}