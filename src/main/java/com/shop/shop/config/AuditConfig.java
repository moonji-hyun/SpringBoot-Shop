package com.shop.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing  /* JPA의 Auditing 기능을 활성화 함*/ // JPA 감시자 BaseEntity활용 용
public class AuditConfig {
    // Audit 기능을 사용하기 위한 클래스

    @Bean
    public AuditorAware<String> auditorProvider(){  // 등록자와 수정자를 처리해주는 AuditorAware을 빈으로 등록함.
        return new AuditorAwareImpl();
    }


}
