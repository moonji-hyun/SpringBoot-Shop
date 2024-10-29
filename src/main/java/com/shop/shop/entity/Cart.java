package com.shop.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter @Setter
@ToString
public class Cart extends BaseEntity{

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)   /* @OneToOne 어노테이션을 이용해 회원 엔티티와 일대일로 매핑*/   // 1:1 매핑 (회원과 카트)
    @JoinColumn(name = "member_id")/* @JoinColumn 어노테이션을 이용해 매핑할 외래키를 지정. name속성에는 매핑할 외래키의 이름을 설정.
     @JoinColumn의 name을 명시하지 않으면 JPA가 알아서 ID를 찾지만 컬럼명이 원하는 대로 생성되지 않을 수 있기 때문에 직접 지정.*/
    // 조인은 회원의 id와(매핑할 외래키 foreign key(member_id) references member )
    private Member member;
     /*
         ibernate:
         create table cart (
            cart_id bigint not null,
            member_id bigint unique,
            primary key (cart_id)
          )
          Hibernate:
         alter table if exists cart
           add constraint FKix170nytunweovf2v9137mx2o
           foreign key (member_id)
           references member
     */

    public static Cart createCart(Member member){
        /* 회원 한 명당 1개의 장바구니를 갖으므로 처음 장바구니에 상품을 담을 때는 해당 회원의 장바구니를 생성해줘야함
         회원 엔티티를 파라미터로 받아서 장바구니 엔티티를 생성하는 로직*/
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }

}
