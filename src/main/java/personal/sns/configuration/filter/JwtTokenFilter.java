package personal.sns.configuration.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import personal.sns.domain.Member;
import personal.sns.service.MemberService;
import personal.sns.util.JwtTokenUtils;

import java.io.IOException;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private MemberService memberService;
    private String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request 에서 헤더 꺼내오기
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 토큰 존재, or "Bearer " 로 시작해야 함
        if (header == null || !header.startsWith("Bearer ")){
            log.error("헤더가 Bearer로 시작하지 않거나 비어있습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        
        // 토큰이 만료되었는지 확인
        try {
            String token = header.split(" ")[1].trim();
            if (JwtTokenUtils.isExpired(token, key)){
                filterChain.doFilter(request, response);
                log.error("토큰의 유효기간이 지났습니다");
                return ;
            }

            // 헤더에서 Claims 에서 usernmae 꺼내기
            String userName = JwtTokenUtils.getUserName(token, key);
            Member member = memberService.loadMemberByMemberName(userName);

            // 유저 이름이 기존 유저에 존재한다면 response에 권한을 담아준다.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
            // 인증 토큰에 request를 담는다.
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //SecurityContextHolder에 인증정보를 담는다.
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (RuntimeException e) {
            log.error("유효성을 검사하던 중 오류가 발생했습니다 : {}", e.getMessage());
            filterChain.doFilter(request,response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
