package org.springframework.samples.petris.configuration;

import static org.springframework.security.config.Customizer.withDefaults;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.samples.petris.configuration.jwt.AuthEntryPointJwt;
import org.springframework.samples.petris.configuration.jwt.AuthTokenFilter;
import org.springframework.samples.petris.configuration.services.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	DataSource dataSource;

	private static final String ADMIN = "ADMIN";
	private static final String PLAYER = "PLAYER";
	
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		http.cors(withDefaults())		
			.csrf(AbstractHttpConfigurer::disable)		
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))			
			.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()))
			.exceptionHandling((exepciontHandling) -> exepciontHandling.authenticationEntryPoint(unauthorizedHandler))			
			.authorizeHttpRequests(authorizeRequests ->	authorizeRequests
			.requestMatchers("/resources/**", "/webjars/**", "/static/**", "/swagger-resources/**").permitAll()		
			.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()	
			.requestMatchers("/", "/oups","/api/v1/auth/**","/v3/api-docs/**","/swagger-ui.html","/swagger-ui/**").permitAll()												
			.requestMatchers("/api/v1/developers").permitAll()														
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/users/**")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/petris/comment/**")).hasAuthority(PLAYER)
			.requestMatchers(HttpMethod.GET, "/petris/achievements").hasAnyAuthority(PLAYER, ADMIN)
			.requestMatchers(HttpMethod.POST, "/petris/achievements").hasAuthority(ADMIN)
			.requestMatchers(HttpMethod.PUT, "/petris/achievements/**").hasAuthority(ADMIN)
			.requestMatchers(HttpMethod.DELETE, "/petris/achievements/**").hasAuthority(ADMIN)
			.requestMatchers(HttpMethod.GET,"/petris/matches/**").hasAnyAuthority(PLAYER, ADMIN)
			.requestMatchers(HttpMethod.POST,"/petris/matches/**").hasAuthority(PLAYER)
			.requestMatchers(HttpMethod.PUT,"/petris/matches/**").hasAuthority(PLAYER)
			.requestMatchers(AntPathRequestMatcher.antMatcher("/petris/matchInvitation/**")).hasAuthority(PLAYER)
			.requestMatchers(HttpMethod.GET,"/petris/players/**").permitAll()
			.requestMatchers(HttpMethod.POST,"/petris/players/**").hasAuthority(PLAYER)
			.requestMatchers(HttpMethod.PUT,"/petris/players/**").hasAuthority(PLAYER)
			.requestMatchers(HttpMethod.DELETE,"/petris/players/**").hasAuthority(ADMIN)
			.requestMatchers(AntPathRequestMatcher.antMatcher("/petris/users/**")).permitAll()
			// .requestMatchers(AntPathRequestMatcher.antMatcher("/petris/**")).permitAll()
			.anyRequest().authenticated())					
			.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);		
		return http.build();
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}	


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
}