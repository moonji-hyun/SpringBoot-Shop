package com.shop.shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartItemDto {
    // 장바구니 담기 구현용 : 상품 상세 페이지에서 장바구니에 담을 상품의 아이디와 수량을 전달 받을 클래스

    @NotNull(message = "상품 아이디는 필수 입력 값 입니다.")
    private Long itemId;

    @Min(value = 1, message = "최소 1개 이상 담아주세요") /*장바구니에 담을 상품의 최소 수량은 1개 이상으로 제한*/
    private int count;
}
