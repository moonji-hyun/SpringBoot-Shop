package com.shop.shop.entity;

import com.shop.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")/*정렬할 때 사용하는 "order" 키워드가 있기 때문에 Order 엔티티에 매핑되는 테이블로 "orders"를 지정함*/
@Getter @Setter
public class Order extends BaseEntity{
    // 회원이 주문 한 경우에 처리되는 엔티티

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  /*한 명의 회원은 여러 번 주문을 할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑을 함.*/ // 주문한 사용자 상태 객체

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    //    private LocalDateTime regTime;
    //    private LocalDateTime updateTime;



    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)  //fetch = FetchType.LAZY 지연로딩(호출할때 쿼리문 생성)
    /*주문 상품 엔티티와 일대다 매핑을 함. 외래키(order_id)가 order_item 테이블에 있으므로 연관 관계의 주인은 OrderItem 엔티티 이다
    Order 엔티티가 주인이 아니므로 "mappedBy" 속성으로 연관 관계의 주인을 설정함. 속성의 값으로 "order"를 적어준 이유는 OrderItem에 있는 Order에 의해 관리되다는 의미로 해석하면됨
    즉, 연관 관계의 주인의 필드인 order를 mappedBy의 값으로 세팅하면됨*/
    private List<OrderItem> orderItems = new ArrayList<>();/*하나의 주문이 여러 개의 주문 상품을 갖으므로 list 자료형을 사용해서 매핑을 함*/
    // 하나의 주문이 여러 개의 주문 상품을 갖으므로 List로 처리
    // 영속성 전이 cascade = CascadeType.ALL  -> cascade 폭포처럼 흐르다(삭제시 처리기법)
    // 부모는 one, 자식은 Many ( 게시글, 댓글 ) ( 게시글, 첨부파일 )
    // CascadeType.ALL : 모든 Cascade를 적용한다. --> 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이
    //CascadeType.PERSIST : 엔티티를 영속화할 때, 연관된 하위 엔티티도 함께 유지한다.
    //CascadeType.MERGE : 엔티티 상태를 병합(Merge)할 때, 연관된 하위 엔티티도 모두 병합한다.
    //CascadeType.REMOVE : 엔티티를 제거할 때, 연관된 하위 엔티티도 모두 제거한다.
    //CascadeType.DETACH : 영속성 컨텍스트에서 엔티티 제거
    //부모 엔티티를 detach() 수행하면, 연관 하위 엔티티도 detach()상태가 되어 변경 사항을 반영하지 않는다.
    //CascadeType.REFRESH : 상위 엔티티를 새로고침(Refresh)할 때, 연관된 하위 엔티티도 모두 새로고침한다.

    //CascadeType.REMOVE와 orphanRemoval = true의 차이점
    //CascadeType.REMOVE는 부모 엔티티가 삭제되면 자식 엔티티도 삭제된다. 즉, 부모가 자식의 삭제 생명 주기를 관리한다.
    //만약 CascadeType.REFRESH도 함께 사용하면, 부모가 자식의 전체 생명 주기를 관리하게 된다.
    //그러나 이 옵션의 경우, 부모 엔티티가 자식 엔티티와의 관계를 제거해도 자식 엔티티는 삭제되지 않고 그대로 남아있다.

    //orphanRemoval = true 또한 부모 엔티티가 삭제되면 자식 엔티티도 삭제 된다.
    //그런데 CasecadeType.Remove와는 달리, 부모 엔티티가 자식 엔티티와의 관계를 제거하면 자식은 고아로 취급되어 그대로 사라진다.
    //두 옵션은 부모 엔티티를 삭제할 때는 동일하게 동작하지만, 부모 엔티티에서 자식 엔티티와의 관계를 제거할 때 차이점을 가진다.

    //주의점
    //두 케이스 모두 자식 엔티티에 딱 하나의 부모 엔티티가 연관되어 있는 경우에만 사용해야 한다.
    //예를 들어 Member(자식)을 Team(부모)도 알고 Parent(부모)도 알고 있다면, CascadeType.REMOVE 또는 orphanRemoval = true의 사용을 조심할 필요가 있다.
    //자식 엔티티를 삭제할 상황이 아닌데도 어느 한쪽의 부모 엔티티를 삭제했거나 부모 엔티티로부터 제거됐다고 자식이 삭제되는 불상사가 일어날 수 있기 때문이다.
    //그러므로 @OneToMany에서 활용할 때 주의를 기울이고, @ManyToMany에서는 활용을 지양하는 것이 좋다.

    // orphanRemoval = true 고아 객체
    // 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 가리킵니다.
    // 부모가 제거될때, 부모와 연관되어있는 모든 자식 엔티티들은 고아객체가 됩니다.
    // 부모 엔티티와 자식 엔티티 사이의 연관관계를 삭제할때, 해당 자식 엔티티는 고아객체가 됩니다.

    public void addOrderItem(OrderItem orderItem) {// orderItems에는 주문 상품 정보들을 담아줌. orderItem객체를 order 객체의 orderItems에 추가함
        orderItems.add(orderItem);
        orderItem.setOrder(this);   /*Order 엔티티와 OrderItem엔티티가 양방향 참조 관계이므로, orderItem 객체에도 order객체를 세팅*/
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) { // OrderItem엔티티가 양방향 참조 관계임
        Order order = new Order();
        order.setMember(member);    // 상품을 주문한 회원의 정보를 셋팅

        for(OrderItem orderItem : orderItemList) {  // 상품 페이지에서는 1개의 상품을 주문 하지만, 장바구니 페이지에는 한번에 여러개의 상품을 주문할 수 있다.
            /*따라서 여러 개의 주문 상품을 담을 수 있도록 리스트형태로 파라미터 값을 받으며 주문 객체 orderItem 객체를 추가함 */
            order.addOrderItem(orderItem);  // 리스트로 받음 -> 주문 객체에 orderItem 객체 추가
        }

        order.setOrderStatus(OrderStatus.ORDER);    // 주문 상태를 ORDER로 셋팅
        order.setOrderDate(LocalDateTime.now());    // 현재 주문 시간으로 셋팅
        return order;
    }

    public int getTotalPrice() {    // 총 주문 금액을 구하는 메서드
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }


    public void cancelOrder() {  // 주문 취소 시 주문 수량을 상품의 재고에 더해주는 로직과 주문 상태를 취소 상태로 바꿔주는 메서드
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}
