package com.shop.shop.service;

import com.shop.shop.entity.Member;
import com.shop.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional  // 다중쿼리 처리용
/*비즈니스 로직을 담당하는 서비스 계층 클래스에 @Transactional 어노테이션을 선언. 로직을 처리하다가 에러가 발생하면, 변경된 데이터를 로직을 수행하기 이전 상태로 콜백 시킴*/
@RequiredArgsConstructor
/*빈을 주입하는 방법으로는 @Autowired 어노테이션을 이용하거나, 필드 주입(Setter 주입), 생성자 주입을 이용하는 방법이 있다.
@RequiredArgsConstructor 어노테이션은 final 이나 @NonNull이 붙은 필드에 생성자를 생성해줌.
빈에 생성자가 1개이고 생성자의 파라미터 타입이 빈으로 등록이 가능하다면 @Autowired 어노테이션 없이 의존성 주입 가능 */
public class MemberService implements UserDetailsService {  /*MemberService가 UserDetailsService를 구현함*/

    private final MemberRepository memberRepository;
    /*빈을 주입하는 방법으로는 @Autowired 어노테이션을 이용하거나, 필드 주입(Setter 주입), 생성자 주입을 이용하는 방법이 있다.
@RequiredArgsConstructor 어노테이션은 final 이나 @NonNull이 붙은 필드에 생성자를 생성해줌.
빈에 생성자가 1개이고 생성자의 파라미터 타입이 빈으로 등록이 가능하다면 @Autowired 어노테이션 없이 의존성 주입 가능 */

    public Member saveMember(Member member){    // 회원 가입시 이메일 검증 후 회원 저장
        validateDuplicateMember(member); // 28행 메서드 실행
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        /*이미 가입된 회원의 경우 IllegalStateException 예외 발생 시킴*/
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
            //IllegalStateException -> 사용자가 값을 제대로 입력했지만, 개발자 코드가 값을 처리할 준비가 안된 경우에 발생한다.
            //예를 들어, 로또 게임 진행 후 게임이 종료된 상태에서 사용자가 추가 진행을 위해 금액을 입력하는 경우.
            //이미 로또 게임 로직이 종료되었기 때문에 사용자의 입력에 대응할 수 없다.
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // 이메일 정보를 받아 처리 함
       /*UsernameNotFoundException 인터페이스의 loadUserByUsername() 메소드를 오버라이딩함. 로그인할 유저의 email을 파라미터로 전달 받음*/
        Member member = memberRepository.findByEmail(email);
        // 이메일을 받아 찾아오고 Member 객체로 담음

        if(member == null){  // member에 값이 비어 있으면 없는 회원으로 예외 발생
            throw new UsernameNotFoundException(email);
        }


        // 객체가 있으면 User 객체에 빌더 패턴으로 값을 담아 리턴한다.
        return User.builder()/*UserDetail을 구현하고 있는 User 객체를 반환해줌.
        User 객체를 생성하기 위해서 생성자로 회원의 이메일, 비밀번호, role을 파라미터로 넘겨 줌*/
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();

        //public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        //		this(username, password, true, true, true, true, authorities);
        //	          사용자명,  암호,    권한을 가지게 됨. }
    }
}
