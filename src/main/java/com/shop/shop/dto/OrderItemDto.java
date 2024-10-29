package com.shop.shop.dto;

import com.shop.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto { // 주문 상품 정보를 담을 클래스
    // 조회한 주문 데이터를 화면에 보낼 때 사용할 DTO (상품 배송, 반품, 교환등....)

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        // OrderItemDto 클래스의 생성자로 orderItem 객체와 이미지 경로를 파라미터로 받아서 멤버 변수 값을 세팅
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }  // 생성자

    private String itemNm; //상품명
    private int count; //주문 수량
    private int orderPrice; //주문 금액
    private String imgUrl; //상품 이미지 경로
}
