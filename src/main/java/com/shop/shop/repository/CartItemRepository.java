package com.shop.shop.repository;

import com.shop.shop.dto.CartDetailDto;
import com.shop.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // 장바구니에 들어갈 상품을 저장하거나 조회하기 위한 인터페이스

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
    /*카트 아이디와 상품 아이디를 이용해서 상품이 장바구니에 들어있는지 조회*/


        @Query("select new com.shop.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
                /*CartDetailDto의 생성자를 이용하여 DTO를 반환할 때는 "new com.shop.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl)" 처럼
                new 키워드와 해당 DTO의 패키지, 클래스명을 적어줌. 또한ㄴ 생성자의 파라미터 순서는 DTO클래스에 명시한 순으로 넣어줘야 함*/
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +    /*장바구니에 담겨있는 상품의 대표 이미지만 가지고 오도록 조건문을 작성*/
            "and im.repimgYn = 'Y' " +          /*장바구니에 담겨있는 상품의 대표 이미지만 가지고 오도록 조건문을 작성*/
            "order by ci.regTime desc"
            )
        List<CartDetailDto> findCartDetailDtoList(Long cartId);
            // 장바구니 페이지에 전달할 리스트를 쿼리문으로 작성 343

    /*JPQL문 : 연관 관계 매핑을 진연 로딩으로 설정할 경우 엔티티에 매핑된 다른 엔티티를 조회할 때 추가적으로 쿼리문이 실행됨
    * 따라서 성능 최적화가 필요할 경우 DTO의 생성자를 이용하여 반환 값으로 DTO 객체를 생성할 수 있다.*/

}
