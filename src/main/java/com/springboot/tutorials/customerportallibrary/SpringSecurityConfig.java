package com.springboot.tutorials.customerportallibrary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@SecurityScheme(name="swaggerSecurity", scheme="basic", type=SecuritySchemeType.HTTP, in=SecuritySchemeIn.HEADER)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		String passwordencodeUser = passwordEncoder().encode("userpassword");
		String passwordencodeAdmin = passwordEncoder().encode("adminpassword");
	    auth.inMemoryAuthentication()
	        .withUser("user")
	        .password(passwordencodeUser)
	        .roles("USER")
	        .and()
	        .withUser("admin")
	        .password(passwordencodeAdmin)
	        .roles("USER", "ADMIN");
	  }
	
	@Override
	  protected void configure(HttpSecurity http) throws Exception { 
	    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
        .antMatchers("/customers/**","user-openapi/**").hasAnyRole("USER","ADMIN")
        .antMatchers("/**","admin-openapi/**").hasRole("ADMIN")
        .anyRequest().authenticated()
        .and()
        .httpBasic();
	  }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
