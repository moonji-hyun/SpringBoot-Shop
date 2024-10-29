package com.shop.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity{
    // 고객이 관심 있거나 나중에 사려는 상품들을 담아줌
    // 하나의 장바구니에는 여러개의 상품이 들어갈 수 있다.
    // 같은 상품을 여러개 주문할 수도 있다.(몇개를 담아 줄 것인지도 설정 필요)

    @Id
    @GeneratedValue
    @Column(name = "cart_item")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 하나의 장바구니에는 여러 개의 상품을 담알 수 있다.
    @JoinColumn(name = "cart_id")     // foreign key (cart_id) references cart(card_id)
    private Cart cart;
    /*하나의 장바구니에는 여러 개의 상품을 담을 수 있으므로 @ManyToOne 어노테이션을 이용하여 다대일 관계로 매핑함*/

    @ManyToOne(fetch = FetchType.LAZY)  // 하나의 상품은 여러 장바구니에 장바구니 상품으로 담김.
    @JoinColumn(name = "item_id")       // 장바구니에 담을 상품의 정보를 알아야 함
    private Item item;                  // foreign key (item_id) references item(item_id)
    /*장바구니에 담을 상품의 정보를 알아야 하므로 상품 엔티티를 매핑해줌. 하나의 상품은 여러 장바구니의 장바구니 상품으로 담길 수 있으므로 @ManyToOne 어노테이션을 이용하여 다대일 관계로 매핑함*/

    private int count;  /*같은 상품을 장바구니에 몇 개 담을지 지정함*/
/*
    Hibernate:
    create table cart_item (
        cart_item bigint not null,
        count integer not null,
        cart_id bigint,
        item_id bigint,
        primary key (cart_item)
    ) engine=InnoDB
    Hibernate:
    alter table if exists cart_item
       add constraint FK1uobyhgl1wvgt1jpccia8xxs3
       foreign key (cart_id)
       references cart (cart_id)
    Hibernate:
    alter table if exists cart_item
       add constraint FKdljf497fwm1f8eb1h8t6n50u9
       foreign key (item_id)
       references item (item_id)
*/

    public static CartItem createCartItem(Cart cart, Item item, int count) {
        /*장바구니에 담을 상품 엔티티를 생성하는 메소드와 장바구니에 담을 수량을 증가시켜 주는 메소드 */
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    public void addCount(int count){
        /*장바구니에 기존에 담겨 있는 상품인데, 해당 상품을 추가로 장바구니에 담을 때 기존 수량에 현재 담을 수량을 더해줄 때 사용할 메소드*/
        this.count += count;
    }

    public void updateCount(int count){
        /*장바구니에서 상품의 수량을 변경할 경우 실시간으로 해당 회원의 장바구니 상품의 수량을 변경하는 메서드*/
        this.count = count;
    }

}
