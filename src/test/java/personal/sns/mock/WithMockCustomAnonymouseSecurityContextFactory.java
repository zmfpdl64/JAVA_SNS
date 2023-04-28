package personal.sns.mock;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import personal.sns.domain.Member;
import personal.sns.fixture.EntityFixture;

import java.lang.annotation.Annotation;
import java.util.List;

public class WithMockCustomAnonymouseSecurityContextFactory implements WithSecurityContextFactory<WithCustomAnonymouse> {
    @Override
    public SecurityContext createSecurityContext(WithCustomAnonymouse annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");
        AnonymousAuthenticationToken anonymouseToken = new AnonymousAuthenticationToken("key","anonymouse", authorities);
        context.setAuthentication(anonymouseToken);

        return context;
    }
}
