package com.example.mungumall.config;

import com.example.mungumall.handler.LoginFailureHandler;
import com.example.mungumall.handler.LoginSuccessHandler;
import com.example.mungumall.member.model.service.MemberService;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@EnableWebSecurity
public class SpringSecurityConfiguration {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    public SpringSecurityConfiguration(MemberService memberService, PasswordEncoder passwordEncoder, LoginSuccessHandler loginSuccessHandler, LoginFailureHandler loginFailureHandler) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
    }

    @Bean
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers("/css/**", "/js/**", "/image/**");
    }

    @Bean
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        /* 요청 권한 체크 */
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/cart/order/**")).authenticated()
                        .requestMatchers(mvcMatcherBuilder.pattern("/cs/inquiry/**")).authenticated()
                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/order/**")).hasRole("MEMBER")
                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "/order/**")).hasRole("ADMIN")
                        .requestMatchers(mvcMatcherBuilder.pattern("/admin/member/**")).hasRole("ADMIN")
                        .requestMatchers(mvcMatcherBuilder.pattern("/admin/product/**")).hasRole("ADMIN")
                        .requestMatchers(mvcMatcherBuilder.pattern("/admin/**")).hasRole("ADMIN")
                        .requestMatchers(mvcMatcherBuilder.pattern("/member/mypage")).hasAnyRole("MEMBER", "ADMIN")
                        .anyRequest().permitAll())
                /* 로그인 */
                .formLogin(login -> login
                        .loginPage("/member/signin")											//로그인 페이지
                        //.defaultSuccessUrl("/")
                        .successForwardUrl("/")													//성공 시 랜딩 페이지
                        .failureHandler(loginFailureHandler)									//실패 시 핸들러, AuthenticationFailureHandler를 implements한 해당 클래스에서 @Component 선언
                        .successHandler(loginSuccessHandler)									//성공 시 핸들러, AuthenticationSuccessHandler를 implements한 해당 클래스에서 @Component 선언
                        .usernameParameter("username")
                        .passwordParameter("password"))
                /* 로그아웃 */
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/signout"))		//로그아웃 주소
                        .deleteCookies("JSESSIONID")											//JSESSIONID 쿠키 삭제
                        .invalidateHttpSession(true)											//세션 만료
                        .logoutSuccessUrl("/"))
                /* 인증/인가 */
                .exceptionHandling((exceptionConfig) ->                                         //예외 처리
                        exceptionConfig.accessDeniedPage("/common/denied"))      //권한 없는 등 인가되지 않을 시 이동 페이지
                /* 에디터 적용 중 X-Frame-Options로 인한 오류 처리
                 * Refused to display in a frame because it set 'X-Frame-Options' to 'deny'. */
                .headers((headerConfig) ->
                        headerConfig.frameOptions(frameOptionsConfig ->
                                        frameOptionsConfig.sameOrigin()))
                /* OAuth2 로그인 */
                .oauth2Login(oauth2 -> oauth2
                .failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler));

        return http.build();
    }
}
