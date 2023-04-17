package personal.sns.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import personal.sns.configuration.filter.JwtTokenFilter;
import personal.sns.exception.CustomAuthenticationEntryPoint;
import personal.sns.service.MemberService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig{

    private final MemberService memberService;

    @Value("${jwt.secret-key}")
    private String key;
    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {

        return http
                .csrf().disable()
                .authorizeHttpRequests(authorize ->
                    authorize
                            .requestMatchers("/api/v1/user/**").permitAll()
                            .requestMatchers("/api/**").authenticated()
                            .anyRequest().permitAll()
                )
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtTokenFilter(key, memberService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
