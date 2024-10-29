package com.shop.shop.entity;

import com.shop.shop.constant.Role;
import com.shop.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity  /*entity는 db담당이라서 바로 프론트로 출력 하지 않고 dto로 변환하여 프론트로 출력*/
@Table(name ="member")/*member라는 이름의 테이블 생성*/
@Getter @Setter
@ToString
public class Member extends BaseEntity{
/*회원 정보를 저장하는 Member entity. 관리할 회원 정보는 이름, 이메일, 비밀번호, 주소, 역할이다.*/

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true) /*회원은 이메일을 통해 유일하게 구분해야 하기 때문에, 동일한 값이 데이터베이스에 들어올 수 없도록 unique속성을 지정함*/
    private String email;    // 회원 검색 처리용

    private String password;

    private String address;

    @Enumerated(EnumType.STRING) /*자바의 enum타입을 엔티티 속성으로 지정할 수 있음.
    Enum을 사용할 때 기본적으로 순서가 저장되는데, enum의 순서가 바뀔 경우 문제가 발생할 수 있으므로 "EnumType.STRING 옵션을 사용해서
    String으로 저장하기를 권장.*/
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        /*Member entity를 생성하는 메소드. Member entity에 회원을 생성하는 메소드를 만들어서 관리를 한다면 코드가 변경되더라도 한 군데만 수정하면 된다.*/

        Member member = new Member();
        member.setName(memberFormDto.getName());/*입력받은 이름을 db에 저장*/
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        /* 입력받은 비밀번호를 BCryptPasswordEncoder Bean을 파라미터로 넘겨서 비밀번호 암호화*/
        member.setPassword(password); /* encoding된 비밀번호를 db에 저장*/
        //member.setRole(Role.USER);/* user권한 부여*/
        member.setRole(Role.ADMIN);/* ADMIN권한 부여*/
        return member;
    }   // 회원 생성용 메서드 (dto와 암호화를 받아 Member 객체 리턴)
}
