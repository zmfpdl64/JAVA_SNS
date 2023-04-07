package personal.sns.configuration;

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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class AuthenticationConfig{

    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {

        return http
                .csrf().disable()
                .authorizeHttpRequests(authorize ->
                    authorize
                            .requestMatchers("/api/user/*").permitAll()
                            .anyRequest().authenticated()
                ).build();
    }

}
