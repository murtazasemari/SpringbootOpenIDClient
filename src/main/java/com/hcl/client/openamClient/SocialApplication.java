/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hcl.client.openamClient;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SocialApplication extends WebSecurityConfigurerAdapter {
	
	

	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
		Map<String, Object> userinfo = new HashMap<String, Object>();
		userinfo.put("fullname", principal.getAttribute("sub"));
		userinfo.put("firstname", principal.getAttribute("given_name"));
		userinfo.put("lastname", principal.getAttribute("family_name"));
		userinfo.put("email", principal.getAttribute("email"));
		userinfo.put("phone", principal.getAttribute("phone_number"));

		Map address = principal.getAttribute("address");
		userinfo.put("address", address.get("formatted"));
		
		return userinfo;
	}
	
	
	
	@GetMapping("/error")
	public String error(HttpServletRequest request) {
		String message = (String) request.getSession().getAttribute("error.message");
		request.getSession().removeAttribute("error.message");
		return message;
	}

	/*
	 * @GetMapping("/") public String home(Model model, @AuthenticationPrincipal
	 * OidcUser principal) { return "index_old"; }
	 */
	
	


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		try {
			
			
			http
			.authorizeRequests(a -> 
				
					a.antMatchers("/home_old","/error", "/webjars/**", "/js/**", "/css/**","/assets/**","/start/**","/old_start/**").permitAll()
						.anyRequest().authenticated()
			)
			
			.oauth2Login().defaultSuccessUrl("/home/home.html", true).failureHandler(authenticationFailureHandler())
			.and()
			.logout().deleteCookies("iPlanetDirectoryPro").invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutUrl("/logout")
			.logoutSuccessUrl("/start/login.html")
			;
			
			
			
		}catch(Exception e) {
			System.out.println("Error While Authentication:"+e);
		}
		
		// @formatter:on
	}
	
	@Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
	
	

	public static void main(String[] args) {
		SpringApplication.run(SocialApplication.class, args);
	}

	
	
}
