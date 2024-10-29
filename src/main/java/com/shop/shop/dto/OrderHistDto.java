package com.shop.shop.dto;

import com.shop.shop.constant.OrderStatus;
import com.shop.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderHistDto { // 주문 정보를 담을 클래스
    // 주문 히스토리

    public OrderHistDto(Order order){
        /*OrderHistDto 클래스의 생성자로 order 객체를 파라미터로 받아서 멤버 변수 값을 세팅. 주문 날짜의 경우 화면에 "yyyy-MM-dd HH:mm"형태로 전달하기 위해서 포맷을 수정함*/
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }  // 생성자

    private Long orderId; //주문아이디
    private String orderDate; //주문날짜
    private OrderStatus orderStatus; //주문 상태

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    //주문 상품리스트
    public void addOrderItemDto(OrderItemDto orderItemDto){ /*orderItemDto 객체를 주문 상품 리스트에 추가하는 메서드*/
        orderItemDtoList.add(orderItemDto);
    }
}
