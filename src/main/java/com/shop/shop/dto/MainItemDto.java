package com.shop.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainItemDto {
    // 메인 페이지 구현(상품 관리 메뉴와 유사)

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    @QueryProjection // @QueryProjection을 이용하면 Item 객체로 값을 받은 후 DTO 클래스로 변환하는 과정 없이 바로 DTO객체를 뽑아낼 수 있다
    /*생성자에 @QueryProjection 어노테이션을 선언하여 Querydsl로 결과 조회 시 MainitemDto객체로 바로 받아 오도록 활용*/
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl,Integer price){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        // repository.ItemRepositoryCustom 에서 연동

        //@QueryProtection 어노테이션을 DTO의 생성자에 선언해주고 빌드를 수행하면
        //QDTO 클래스가 생성되어 해당 QDTO를 이용하여 더 간편하게 사용할 수 있는 장점이 있다.
        //컴파일 시점에도 오류를 검출할 수 있는 이점이 있으나
        //전역 계층에 사용하는 DTO를 QueryDSL 에 의존시켜야 하는 단점이 존재한다.

    }

}
