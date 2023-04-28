package personal.sns.mock;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomSecurityContextFactory.class)
public @interface WithMockCustomMember {
    String username = "username";
    String password = "password";
}
