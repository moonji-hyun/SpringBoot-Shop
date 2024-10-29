package com.shop.shop.repository;

import com.shop.shop.dto.ItemSearchDto;
import com.shop.shop.dto.MainItemDto;
import com.shop.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    /*Querydsl을 Spring Data Jpa와 함께 사용하기 위해서는 사용자 정의 리포지토리를 정의 해야 한다. 총 3단계의 과정으로 구현함*/

    /* 1. 사용자 정의 인터페이스 작성*/
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    /*상품 조회 조건을 담고 있는 itemSearchDto 객체와 페이징 정보를 담고 있는 pageable 객체를 파라미터로 받는
     getAdminItemPage 메서드를 정의. 반환 데이터로 Page<Item> 객체를 반환함*/

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    // QueryDsl에서 제공하는 @QueryProjection 기능 사용
}
