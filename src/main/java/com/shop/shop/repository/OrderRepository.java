package com.shop.shop.repository;

import com.shop.shop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 주문 이력을 조회하는 쿼리

    @Query("select o from Order o where o.member.email = :email order by o.orderDate desc" )
    List<Order> findOrders(@Param("email") String email, Pageable pageable);/*현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞춰서 조회*/
    // 이메일 주소를 이용해서 주문날짜로 정렬하여 주문 정보를 가져고 옴  + 페이징 처리

    @Query("select count(o) from Order o where o.member.email = :email" )
    Long countOrder(@Param("email") String email);
    // 현재 로그인한 회원의 주문 개수가 몇개인지 조회(페이징 처리용)

}
