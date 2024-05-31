
package com.arg.swu.configs;

import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import lombok.RequiredArgsConstructor;

/**
*
* @author sittichaim
* 
*/
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtRequestFilter jwtRequestFilter;

	@Value("${security.ldap.url}")
	private String ldapUrl;
	
	@Value("${security.ldap.base}")
	private String ldapBase;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

    	httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/**"));
    	
        // don't authenticate this particular request
    	httpSecurity.authorizeHttpRequests(auth ->
                		auth.requestMatchers("/authenticate/**", "/error").permitAll()
                		.requestMatchers(
                				"/v3/**",
                				"/demo/**",
                				"/sec/**",
                				"/swagger-ui.html",
                				"/swagger-ui/**",
                				"/api-docs/**",
                				"/download/**",
                				"favicon.ico",
                				"/publicfile/**").permitAll()
                		.requestMatchers(
                				"/login/**"
								,"/actuator/**").permitAll()
                		.requestMatchers(
                				"/payment-gateway/**",
                				"/operationapi/payment/notify",
                				"/operationapi/payment/notifyqr").permitAll()
                		.requestMatchers(
                				"/mainpage/**",
                				"/course-management/**",
                				"/dropdown/**",
                				"/register/**",
                				"/student-portal/**",
                				"/homepage/**",
                				"/all-courses/**",
                				"/course-detail/**",
                				"/user-management/**").permitAll()
                        // all other requests need to be authenticated
                        .anyRequest()
                        .authenticated()
                        )
                // make sure we use stateless session; session won't be used to
                // store user's state.
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
        httpSecurity.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.applyPermitDefaultValues();
            configuration.setAllowedOrigins(Arrays.asList("*"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("Authorization", "lang", "Cache-Control", "Content-Type"));
            configuration.setExposedHeaders(Arrays.asList("Authorization", "lang"));
            return configuration;
        }));
        
        return httpSecurity.build();
    }

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	/** LDAP */
	@Bean
	@Qualifier("ldapTemplateDefault")
	public LdapTemplate ldapTemplateDefault() throws Exception {
		
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(ldapUrl);
		contextSource.setBase(ldapBase);
		contextSource.setAnonymousReadOnly(true);
		contextSource.afterPropertiesSet();
		
		return new LdapTemplate(contextSource);
	}
	
}
