package personal.sns.mock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import personal.sns.domain.Member;
import personal.sns.fixture.EntityFixture;
import personal.sns.util.JwtTokenUtils;

public class WithMockCustomSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomMember> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomMember annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Member member = Member.fromEntity(EntityFixture.of());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
        context.setAuthentication(usernamePasswordAuthenticationToken);

        return context;
    }
}
