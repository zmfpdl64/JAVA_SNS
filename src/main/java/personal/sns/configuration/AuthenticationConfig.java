package personal.sns.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import personal.sns.configuration.filter.JwtTokenFilter;
import personal.sns.exception.CustomAuthenticationEntryPoint;
import personal.sns.service.MemberService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig{

    @Value("${jwt.secret-key}")
    private String key;
    private final MemberService memberService;

    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {

        return http
                .csrf().disable()
                .authorizeHttpRequests(authorize ->
                    authorize
                            .requestMatchers("/api/v1/user/**").permitAll()
                            .anyRequest().authenticated()
                )
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtTokenFilter(memberService, key), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
