package com.shop.shop.repository;

import com.shop.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    /*Member entity를 데이터베이스에 저장*/

    Member findByEmail(String email);   // 이메일을 받아서 회원 정보를 가져옴.
    /*회원 가입 시 중복된 회원이 있는지 검사하기 위해서 이메일로 회원을 검사할 수 있도록 쿼리 메소드를 작성*/

}
