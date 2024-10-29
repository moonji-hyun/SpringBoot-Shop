package com.shop.shop.repository;

import com.shop.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    // JpaRepository<entity타입 클래스, 기본키pk타입>
    // extends JpaRepository<Item, Long> jpaRepository를 상속받아 Jpa 기능을 구현
    // QuerydslPredicateExecutor<Item> : 이 조건이 맞다고 판단하는 근거를 함수로 제공(페이징 처리용)
    // long count(Predicate) : 조건에 맞는 데이터의 총 개수 반환
    // boolean exists(Predicate) : 조건에 맞는 데이터 존재 여부 반환
    // Iterable findAll(Predicate) : 조건에 맞는 모든 데이터 반환
    // T findOne(Predicate) : 조건에 맞는 데이터 1개 반환
    // , ItemRepositoryCustom : 이미지 처리(파일처리), ItemRepository에서 Querysdsl로 구현한 상품 관리 페이지 목록을 불러오는 getAdminitemPage() 메소드 사용 가능

    List<Item> findByItemNm(String itemNm);
    // 쿼리메서드 (find + (엔티티 이름) + By + 변수이름) : itemNm을 이용해 아이템을 리스트와 찾아와
    // itemNm(상품명)으로 데이터를 조회하기 위해서 By 뒤에 필드명인 ItemNm을 메소드의 이름에 붙여줍니다.
    // 엔티티명은 생략이 가능하므로 findItemByItemNm 대신 findByItemNm으로 메소명을 만듬
    // 매개 변수로는 검색할 때 사용할 상품명 변수를 넘겨줍니다.

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    // 상품을 상품명과 상품 상세 설명을 OR 조건을 이용하여 조회하는 쿼리 메소드.
    // 쿼리메서드 : itemNm 또는 itemDetail을 이용해 아이템을 리스트와 찾아와

    List<Item> findByPriceLessThan(Integer price);
    // 파라미터로 넘어온 price 변수보다 값이 작은 상품 데이터를 조회하는 쿼리 메소드.
    // 쿼리메서드 : price 값보다 작거나 같은 아이템을 리스트와 찾아와

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
    // 'OrderBy + 속성명 + Desc 키워드'를 이용해 내림차순으로 정렬
    // 쿼리메서드 : price 값보다 작거나 같은 아이템을 내림차순으로 정렬하고 아이템을 리스트와 찾아와

    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
        // @Query 어노테이션 안에 JPQL로 작성한 쿼리문을 넣어준다. from 뒤에는 엔티티 클래스로 작성한 Item을 지정해주었고, Item으로부터 데이터를 select하겠다는 것을 의미함.
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
    // 파라미터에 @Param 어노테이션을 이용하여 파라미터로 넘어온 값을 JPQL에 들어갈 변수로 지정해줄 수 있다. 현재는 itemDetail 변수를 "like % %"사이에 ".itemDetail"로 값이 들어가도록 작성.
    // 엔티티 : 파라미터로 itemDetail값을 맞아 like로 찾아오고 가격순으로 내림차순하여 검색해와

    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    // value 안에 네이티브 쿼리문을 작성하고 "nativeQuery = true"를 지정한다. // nativeQuery = true -> JPA가 아니라 엔티티를 사용하지 않음 -> 순수한 쿼리문
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}
