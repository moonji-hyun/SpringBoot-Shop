package com.shop.shop.entity;

import com.shop.shop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;


    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "gildong", roles = "USER")/*스프링 시큐리티에서 제공하는 어노테이션으로 @WithMockuSER에 지정한 사용자가 로그인한 상태라고 가정하고 테스트를 진행할 수 있음*/
    public void auditingTest(){
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("register time : " + member.getRegTime());
        System.out.println("update time : " + member.getUpdateTime());
        System.out.println("create member : " + member.getCreatedBy());
        System.out.println("modify member : " + member.getModifiedBy());
    }
    /*
        register time : 2024-10-23T22:16:19.166238
        update time : 2024-10-23T22:16:19.166238
        create member : gildong
        modify member : gildong
    */
}
