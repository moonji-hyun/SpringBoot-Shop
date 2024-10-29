package com.shop.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@RequiredArgsConstructor //final 선언시 필요
@EnableMethodSecurity(prePostEnabled = true) // 시큐리티 6에서 변경 https://jake-seo-dev.tistory.com/82
// @EnableWebSecurity 시큐리티 5버전 까지만 활용
public class SecurityConfig {
    // 시큐리티 6 extends WebSecurityConfigurerAdapter 사용 안됨

    //  시큐리티 6 자동 주입 기법 대신 final로 @RequiredArgsConstructor 선언 함
    //  @Autowired
    //  MemberService memberService;

    //private final MemberService memberService; // 시큐리티 6 @RequiredArgsConstructor 사용

    @Bean   // 시큐리트 6에서 config(HttpSecurity http) 변경됨
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .formLogin(form -> {
                    form
                            .loginPage("/members/login")    // 로그인 페이지 url
                            .defaultSuccessUrl("/")         // 로그인 성공시 기본 경로
                            .usernameParameter("email")     // 로그인시 사용할 파라미터 이름으로 email을 지정
                            .failureUrl("/members/login/error");  // 로그인 실패시 갈 경로
                })
                .logout(logout -> {                         // 로그아웃용
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 처리용 경로
                            .logoutSuccessUrl("/");     // 로그아웃 성공시 갈 경로
                });
//                .loginPage("/members/login")
//                .defaultSuccessUrl("/")
//                .usernameParameter("email")
//                .failureUrl("/members/login/error")
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
//                .logoutSuccessUrl("/")

        // authorizeHttpRequests 스프링 스큐리티의 구성 메서드 내에서 사용되는 메서드, HTTP요청에 대한 인가 설정을 구성
        // 이 메서드를 사용하여 다양한 인가 규칙을 정의할 수 있음, 경로별 다른 권한 설정이 가능
        http.authorizeHttpRequests(authorizeHttpRequests -> {
            authorizeHttpRequests
                    .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                    // requestMatchers Http 요청매처를 적용
                    // .permitAll 모든 요청을 인가(인증된 사용자 권한에 상관 없음)
                    .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    // /admin/하위 메서드는 ADMIN 룰에 적용됨.
                    .anyRequest().authenticated();
        })  ;

        http.exceptionHandling(exceptionHandling -> {
            exceptionHandling
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint());/*인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러 등록*/
        })  ;

        return http.build();
    }

    @Bean   // 패스워드를 db에 저장할 때 암호화 처리함.
    public PasswordEncoder passwordEncoder(){
        /* 비밀번호를 데이터베이스에 그대로 저장했을 경우, 데이터베이스가 해킹당하면 고객의 회원정보가 그대로 노출됨
        이를 해결하기 위해 BCryptPasswordEncoder의 해시 함수를 이용하여 비밀번호를 암호화하여 저장
        BCryptPasswordEncoder를 빈으로 등록하여 사용함*/

        return new BCryptPasswordEncoder();
    }







}
