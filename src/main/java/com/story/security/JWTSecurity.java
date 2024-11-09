package com.story.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class JWTSecurity {

	@Autowired
	private JWTFetchingData jwtFetchingData;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public UserDetailsService userDetailsService() {
		return jwtFetchingData;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		
		return security
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/view/**", "/CSS/**", "/JS/**", "/Image/**", "/logo/**","/Other_Image/**","/static/**","/robots.txt").permitAll()
                        .requestMatchers("/", "/hindi-story", "/hindi-story/", "/hindi-story/**", "/sitemap", "/hindi-story/sitemap.xml","/info/**").permitAll()
                        .requestMatchers("/dashboard", "/dashboard/", "/dashboard/**", "/indexHeader", "/next50Entries", "/deleteEntry", "/findToDelete", "/updateEntry", "/findData", "/insertData", "/btnNavigateAjax", "/loginData","/hindi-story/showDB", "/hindi-story/showStory", "/hindi-story/handleShowDB", "/selectStoryByCategory").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/register/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/dashboard", true)  // Redirect to /dashboard on successful login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")  // Redirect to the home page after logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                    )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // Create session only if required
                    .maximumSessions(1)  // Limit to one session per user
                    .maxSessionsPreventsLogin(false)  // Prevents new login if session limit is reached
                    .expiredUrl("/login?expired=true")  // Redirect to login page if session expires
                )
                .sessionManagement(session -> session
                    .sessionFixation().migrateSession()  // Migrate session on login to prevent session fixation
                )
                .build();
	}
	
	
}

//@Bean
//public UserDetailsService userDetailsService() {
//	UserDetails admin = User.builder()
//			.username("aarav")
//			.password(passwordEncoder().encode("admin"))
//			.roles("ADMIN")
//			.build();
//	UserDetails user1 = User.builder()
//			.username("ballu")
//			.password(passwordEncoder().encode("1234"))
//			.roles("USER")
//			.build();
//	return new InMemoryUserDetailsManager(admin,user1);
//}


