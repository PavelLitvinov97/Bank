package com.Faust.Bank.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService customUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/v1/auth/signin").permitAll()
				// all other requests need to be authenticated
				.anyRequest().authenticated()
				.antMatchers(HttpMethod.GET, "/v1/treasury/issues").hasAnyRole("EMPEROR", "TREASURER")
				.antMatchers(HttpMethod.GET, "/v1/treasury/issues/**").hasAnyRole("EMPEROR", "TREASURER")
				.antMatchers(HttpMethod.DELETE, "/v1/treasury/issues/**").hasAnyRole("EMPEROR", "TREASURER")
				.antMatchers(HttpMethod.POST, "/v1/treasury/issues").hasRole("TREASURER")
				.antMatchers(HttpMethod.PUT, "/v1/treasury/issues/**").hasAnyRole("EMPEROR", "TREASURER")

				.antMatchers(HttpMethod.GET, "/v1/treasury/supplies").hasAnyRole("PROVIDER", "TREASURER")
				.antMatchers(HttpMethod.GET, "/v1/treasury/supplies/**").hasAnyRole("PROVIDER", "TREASURER")
				.antMatchers(HttpMethod.DELETE, "/v1/treasury/supplies/**").hasAnyRole("PROVIDER", "TREASURER")
				.antMatchers(HttpMethod.POST, "/v1/treasury/supplies").hasRole("TREASURER")
				.antMatchers(HttpMethod.PUT, "/v1/treasury/supplies/**").hasAnyRole("PROVIDER", "TREASURER")

				.antMatchers(HttpMethod.GET, "/v1/treasury/investments").hasRole("USER")
				.antMatchers(HttpMethod.GET, "/v1/treasury/investments/**").hasRole("USER")
				.antMatchers(HttpMethod.POST, "/v1/treasury/investments").hasRole("USER").and()
				// make sure we use stateless session; session won't be used to
				// store user's state.
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
