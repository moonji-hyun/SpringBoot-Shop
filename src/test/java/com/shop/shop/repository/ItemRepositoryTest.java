package com.shop.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.shop.constant.ItemSellStatus;
import com.shop.shop.entity.Item;
import com.shop.shop.entity.QItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest     //통합 테스트를 위해 스프링 부트에서 제공하는 어노테이션. 실제 애플리케이션을 구동할 때처럼 모든 Bean을 loC 컨테이너에 등록함.
@TestPropertySource(locations = "classpath:application-test.properties") // 테스트 코드 실행 시 application.properties에 설정해둔 값보다
// application-test.properties에 같은 설정이 있다면 더 높은 우선순위 부여.
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;  // ItemRepository를 사용하기 위해서 @Autowired 어노테이션을 이요하여 Bean을 주입함

    @PersistenceContext
    EntityManager em; // 영속성 컨텍스를 사용하기 위해 @PersistenceContext 어노테이션을 이용해 EntityManager 빈을 주입함.

    @Test   // 테스트할 메서드 위에 선언하여 해당 메서드를 테스트 대상으로 지정
    @DisplayName("상품 저장 테스트")   // Junit5에 추가된 어노테이션으로 테스트 코드 실행 시 @Displayname에 지정한 테스트명이 노출됨.
    public void createItemTest(){   // 아이템 생성 테스트
        Item item = new Item();     // 아이템 엔티티를 생성
        item.setItemNm("테스트 상품");   // 아이템 이름 생성
        item.setPrice(10000);     // 아이템 가격 생성
        item.setItemDetail("테스트 상품 상세 설명"); // 아이템 상세 설명 생성
        item.setStockNumber(100);    // 아이템 개수
        item.setRegTime(LocalDateTime.now());   // 아이템 등록일
        item.setUpdateTime(LocalDateTime.now());    // 아이템 생성일
        Item savedItem = itemRepository.save(item); // Jpa를 이용해 insert 처리
        System.out.println(savedItem.toString());   // 출력
    }
    /*
    Hibernate:
    drop table if exists item cascade
    Hibernate:
        drop sequence if exists item_seq
    Hibernate:
        create sequence item_seq start with 1 increment by 50
    Hibernate:
        create table item (
            price integer not null,
            stock_number integer not null,
            id bigint not null,
            reg_time timestamp(6),
            update_time timestamp(6),
            item_nm varchar(50) not null,
            item_sell_status varchar(255) check (item_sell_status in ('SELL','SOLD_OUT')),
            item_detail clob not null,
            primary key (id)
        )

     Hibernate:
    select
        next value for item_seq

    Hibernate:
        insert
        into
            item
            (item_detail, item_nm, item_sell_status, price, reg_time, stock_number, update_time, id)
        values
            (?, ?, ?, ?, ?, ?, ?, ?)
    */


    public void createItemList(){    // 아이템 객체를 리스트로 생성 (10개)
        // 테스트 코드 실행 시 데이터베이스에 상품 데이터가 없으므로 테스트 데이터 생성을 위해서
        // 10개의 상품을 저장하는 메소드를 작성하여 findByItemNmTest()에서 실행해줌.
        for(int i=1; i<=10; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100); item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
        this.createItemList();                                              // 71행에 있는 메서드로 아이템 리스트 생성
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");    // ItemNm으로 찾아온 객체를 list로 저장
        // 테스트 코드 실행 시 데이터베이스에 상품 데이터가 없으므로 테스트 데이터 생성을 위해서 10개의 상품을 저장하는 메소드를 작성하여 findByItemNmTest()에서 실행해줌.

        for (Item item : itemList){     // 출력 테스트
            System.out.println(item.toString());  // 조회 결과 얻은 item 객체들을 출력함.
        }
    }

/*
Hibernate:
    select
        i1_0.id,
        i1_0.item_detail,
        i1_0.item_nm,
        i1_0.item_sell_status,
        i1_0.price,
        i1_0.reg_time,
        i1_0.stock_number,
        i1_0.update_time
    from
        item i1_0
    where
        i1_0.item_nm=?
tem(id=1, itemNm=테스트 상품1, price=10001, stockNumber=100, itemDetail=테스트 상품 상세 설명1, itemSellStatus=SELL, regTime=2024-10-20T22:40:12.967623, updateTime=2024-10-20T22:40:12.967623)
*/

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest(){
        this.createItemList();      // 기존에 만들었던 테스트 상품을 만드는 메소드를 실행하여 조회할 대상을 만들어줌 / 71행에 있는 메서드로 아이템 리스트 생성
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        // 상품명이 "테스트 상품1" 또는 상품 상세 설명이 "테스트 상품 상세 설명5"이면 해당 상품을 itemList에 할당함.
        // 테스트 코드를 실행하면 조건대로 2개의 상품이 출력되는 것을 볼 수 있음.
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }
    /*
    Item(id=1, itemNm=테스트 상품1, price=10001, stockNumber=100, itemDetail=테스트 상품 상세 설명1, itemSellStatus=SELL, regTime=2024-10-21T10:01:06.554913, updateTime=2024-10-21T10:01:06.554913)
    Item(id=5, itemNm=테스트 상품5, price=10005, stockNumber=100, itemDetail=테스트 상품 상세 설명5, itemSellStatus=SELL, regTime=2024-10-21T10:01:06.719930, updateTime=2024-10-21T10:01:06.719930)
    */

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest(){
        this.createItemList();   // 기존에 만들었던 테스트 상품을 만드는 메소드를 실행하여 조회할 대상을 만들어줌 / 71행에 있는 메서드로 아이템 리스트 생성
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        // 현재 데이터베이스에 저장된 가격은 10001~10005이다. 테스트 코드 실행 시 10개의 상품을 저장하는 로그가 콘솔에 나타나고 맨 마지막에 가격이 10005보다 작은 4개의 상품을 출력해줌
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }
/*
    Item(id=1, itemNm=테스트 상품1, price=10001, stockNumber=100, itemDetail=테스트 상품 상세 설명1, itemSellStatus=SELL, regTime=2024-10-21T10:20:31.641324, updateTime=2024-10-21T10:20:31.641324)
    Item(id=2, itemNm=테스트 상품2, price=10002, stockNumber=100, itemDetail=테스트 상품 상세 설명2, itemSellStatus=SELL, regTime=2024-10-21T10:20:31.747052, updateTime=2024-10-21T10:20:31.747052)
    Item(id=3, itemNm=테스트 상품3, price=10003, stockNumber=100, itemDetail=테스트 상품 상세 설명3, itemSellStatus=SELL, regTime=2024-10-21T10:20:31.752947, updateTime=2024-10-21T10:20:31.752947)
    Item(id=4, itemNm=테스트 상품4, price=10004, stockNumber=100, itemDetail=테스트 상품 상세 설명4, itemSellStatus=SELL, regTime=2024-10-21T10:20:31.756982, updateTime=2024-10-21T10:20:31.756982)
*/

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc(){
        this.createItemList();   // 기존에 만들었던 테스트 상품을 만드는 메소드를 실행하여 조회할 대상을 만들어줌 / 71행에 있는 메서드로 아이템 리스트 생성
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }
/*
Item(id=4, itemNm=테스트 상품4, price=10004, stockNumber=100, itemDetail=테스트 상품 상세 설명4, itemSellStatus=SELL, regTime=2024-10-21T10:44:26.208098, updateTime=2024-10-21T10:44:26.208098)
Item(id=3, itemNm=테스트 상품3, price=10003, stockNumber=100, itemDetail=테스트 상품 상세 설명3, itemSellStatus=SELL, regTime=2024-10-21T10:44:26.205057, updateTime=2024-10-21T10:44:26.206207)
Item(id=2, itemNm=테스트 상품2, price=10002, stockNumber=100, itemDetail=테스트 상품 상세 설명2, itemSellStatus=SELL, regTime=2024-10-21T10:44:26.201936, updateTime=2024-10-21T10:44:26.201936)
Item(id=1, itemNm=테스트 상품1, price=10001, stockNumber=100, itemDetail=테스트 상품 상세 설명1, itemSellStatus=SELL, regTime=2024-10-21T10:44:26.095177, updateTime=2024-10-21T10:44:26.095177)
*/

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();   // 기존에 만들었던 테스트 상품을 만드는 메소드를 실행하여 조회할 대상을 만들어줌 / 71행에 있는 메서드로 아이템 리스트 생성
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNative(){
        this.createItemList();   // 기존에 만들었던 테스트 상품을 만드는 메소드를 실행하여 조회할 대상을 만들어줌 / 71행에 있는 메서드로 아이템 리스트 생성
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }
/*  findByItemDetailTest() 와 findByItemDetailByNative() 는 같은 결과가 출력된다.
Item(id=10, itemNm=테스트 상품10, price=10010, stockNumber=100, itemDetail=테스트 상품 상세 설명10, itemSellStatus=SELL, regTime=2024-10-21T12:19:42.419278, updateTime=2024-10-21T12:19:42.419278)
Item(id=9, itemNm=테스트 상품9, price=10009, stockNumber=100, itemDetail=테스트 상품 상세 설명9, itemSellStatus=SELL, regTime=2024-10-21T12:19:42.415289, updateTime=2024-10-21T12:19:42.415289)
Item(id=8, itemNm=테스트 상품8, price=10008, stockNumber=100, itemDetail=테스트 상품 상세 설명8, itemSellStatus=SELL, regTime=2024-10-21T12:19:42.410300, updateTime=2024-10-21T12:19:42.410300)
Item(id=7, itemNm=테스트 상품7, price=10007, stockNumber=100, itemDetail=테스트 상품 상세 설명7, itemSellStatus=SELL, regTime=2024-10-21T12:19:42.394924, updateTime=2024-10-21T12:19:42.394924)
Item(id=6, itemNm=테스트 상품6, price=10006, stockNumber=100, itemDetail=테스트 상품 상세 설명6, itemSellStatus=SELL, regTime=2024-10-21T12:19:42.391942, updateTime=2024-10-21T12:19:42.391942)
Item(id=5, itemNm=테스트 상품5, price=10005, stockNumber=100, itemDetail=테스트 상품 상세 설명5, itemSellStatus=SELL, regTime=2024-10-21T12:19:42.372424, updateTime=2024-10-21T12:19:42.372424)
Item(id=4, itemNm=테스트 상품4, price=10004, stockNumber=100, itemDetail=테스트 상품 상세 설명4, itemSellStatus=SELL, regTime=2024-10-21T12:19:42.369398, updateTime=2024-10-21T12:19:42.369398)
Item(id=3, itemNm=테스트 상품3, price=10003, stockNumber=100, itemDetail=테스트 상품 상세 설명3, itemSellStatus=SELL, regTime=2024-10-21T12:19:42.364411, updateTime=2024-10-21T12:19:42.364411)
Item(id=2, itemNm=테스트 상품2, price=10002, stockNumber=100, itemDetail=테스트 상품 상세 설명2, itemSellStatus=SELL, regTime=2024-10-21T12:19:42.360431, updateTime=2024-10-21T12:19:42.360431)
Item(id=1, itemNm=테스트 상품1, price=10001, stockNumber=100, itemDetail=테스트 상품 상세 설명1, itemSellStatus=SELL, regTime=2024-10-21T12:19:42.237100, updateTime=2024-10-21T12:19:42.237100)
*/

    @Test
    @DisplayName("Querydsl1 조회테스트1")
    public void queryDslTest(){
        this.createItemList();   // 기존에 만들었던 테스트 상품을 만드는 메소드를 실행하여 조회할 대상을 만들어줌 / 71행에 있는 메서드로 아이템 리스트 생성
        JPAQueryFactory queryFactory = new JPAQueryFactory(em); // JPAQueryFactory를 이용하여 쿼리를 동적으로 생성. 생성자의 파라미터로는 EntityManager 객체를 넣어 줌.
         QItem qItem = QItem.item;  // Querydsl을 통해 쿼리를 생성하기 위해 플러그인을 통해 자동으로 생성된 QItem 객체를 이용함.
        JPQLQuery<Item> query = queryFactory.selectFrom(qItem)                         // select * from Item
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))                   // where itemSellStatus=SELL And
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))    // itemDetail = *테스트 상품 상세 설명*
                .orderBy(qItem.price.desc());                                         // orderBy price 내림차순
                                                                                      // 자바 소스코드지만 sql문과 유사하게 소스 작성 가능
        List<Item> itemList = query.fetch();    // 위에 만든 쿼리를 실행하여 list로 받음
        // JPAQuery 메소드중 하나인 fetch를 이용해서 쿼리 결과를 리스트로 반환함. fetch() 메소드 실행 시점에 쿼리문이 실행됨.
        // JPAQuery에서 데이터 결과를 반환하는 메소드
        // List<T> fetch() : 조회 결과 리스트 반환
        // T fetchOne : 조회 대상이 1건인 경우 제네릭으로 지정한 타입 반환
        // T fetchFirst() : 조회 대상 중 1건만 반환
        // Long fetchCount() : 조회 대상 개수 반환
        // QueryResult<T> fetchResult() : 조회한 리스트와 전체 개수를 포함한 QueryResult 반환

        for (Item item : itemList){                                                  // 위에서 만든 리스트 출력
            System.out.println(item.toString());
        }
    }
/*
Hibernate:
    select
        i1_0.id,
        i1_0.item_detail,
        i1_0.item_nm,
        i1_0.item_sell_status,
        i1_0.price,
        i1_0.reg_time,
        i1_0.stock_number,
        i1_0.update_time
    from
        item i1_0
    where
        i1_0.item_sell_status=?
        and i1_0.item_detail like ? escape '!'
    order by
        i1_0.price desc
Item(id=10, itemNm=테스트 상품10, price=10010, stockNumber=100, itemDetail=테스트 상품 상세 설명10, itemSellStatus=SELL, regTime=2024-10-21T14:24:32.342554, updateTime=2024-10-21T14:24:32.342554)
Item(id=9, itemNm=테스트 상품9, price=10009, stockNumber=100, itemDetail=테스트 상품 상세 설명9, itemSellStatus=SELL, regTime=2024-10-21T14:24:32.338572, updateTime=2024-10-21T14:24:32.338572)
Item(id=8, itemNm=테스트 상품8, price=10008, stockNumber=100, itemDetail=테스트 상품 상세 설명8, itemSellStatus=SELL, regTime=2024-10-21T14:24:32.335591, updateTime=2024-10-21T14:24:32.335591)
Item(id=7, itemNm=테스트 상품7, price=10007, stockNumber=100, itemDetail=테스트 상품 상세 설명7, itemSellStatus=SELL, regTime=2024-10-21T14:24:32.332578, updateTime=2024-10-21T14:24:32.332578)
Item(id=6, itemNm=테스트 상품6, price=10006, stockNumber=100, itemDetail=테스트 상품 상세 설명6, itemSellStatus=SELL, regTime=2024-10-21T14:24:32.324601, updateTime=2024-10-21T14:24:32.324601)
Item(id=5, itemNm=테스트 상품5, price=10005, stockNumber=100, itemDetail=테스트 상품 상세 설명5, itemSellStatus=SELL, regTime=2024-10-21T14:24:32.319652, updateTime=2024-10-21T14:24:32.319652)
Item(id=4, itemNm=테스트 상품4, price=10004, stockNumber=100, itemDetail=테스트 상품 상세 설명4, itemSellStatus=SELL, regTime=2024-10-21T14:24:32.316590, updateTime=2024-10-21T14:24:32.316590)
Item(id=3, itemNm=테스트 상품3, price=10003, stockNumber=100, itemDetail=테스트 상품 상세 설명3, itemSellStatus=SELL, regTime=2024-10-21T14:24:32.314647, updateTime=2024-10-21T14:24:32.314647)
Item(id=2, itemNm=테스트 상품2, price=10002, stockNumber=100, itemDetail=테스트 상품 상세 설명2, itemSellStatus=SELL, regTime=2024-10-21T14:24:32.309646, updateTime=2024-10-21T14:24:32.309646)
Item(id=1, itemNm=테스트 상품1, price=10001, stockNumber=100, itemDetail=테스트 상품 상세 설명1, itemSellStatus=SELL, regTime=2024-10-21T14:24:32.163704, updateTime=2024-10-21T14:24:32.163704)

*/

    public void createItemList2(){ // 상품 데이터를 만드는 새로운 메소드를 하나 만들것임. 1~5상품은 상품의 판매상태를 SELL(판매중)으로 지정, 6~10상품은 판매상태를 SOLD_OUT(품절)으로 세팅해 생성할것임
        for (int i=1; i<=5; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
        for (int i=6; i<=10; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트2")
    public void queryDslTest2(){
        this.createItemList2();  // 260행 객체 생성

        BooleanBuilder booleanBuilder = new BooleanBuilder();   //BooleanBuilder는 쿼리에 들어갈 조건을 만들어주는 빌더. predicate를 구현하고 있으며 메소드 체인 형식으로 사용할 수 있다.
        QItem item = QItem.item;                // 쿼리dsl로 객체 item 생성
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%")); // 필요한 상품을 조회하는데 필요한 "and"조건을 추가함. 아래 소스에서 상품의 판매상태가 SELL일 때만 booleanBuilder에 판매상태 조건을 동적으로 추가함
        booleanBuilder.and(item.price.gt(price));
        System.out.println(ItemSellStatus.SELL);
        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5);  // 데이터를 페이징해 조회하도록 PageRequest.of()메소드를 이용해 Pageable 객체를 생성함. 첫 번째 인자는 죄회할 페이지의 번호, 두 번째 인자는 한 페이지당 조회할 데이터의 개수
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable); // QuerydslPredicateExecutor 인터페이스에서 정의한 findAll()메서드를 이용해 조건에 맞는 데이터를 Page객체로 받아옴
        System.out.println("total elements : " + itemPagingResult.getTotalElements());  //Predicate를 이용해서 검색된 객체 수 알아옴

        List<Item> resultItemList = itemPagingResult.getContent();
        for (Item resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }
    }
    /*
    Hibernate:
    select
        i1_0.id,
        i1_0.item_detail,
        i1_0.item_nm,
        i1_0.item_sell_status,
        i1_0.price,
        i1_0.reg_time,
        i1_0.stock_number,
        i1_0.update_time
    from
        item i1_0
    where
        i1_0.item_detail like ? escape '!'
        and i1_0.price>?
        and i1_0.item_sell_status=?
    offset
        ? rows
    fetch
        first ? rows only
total elements : 2
Item(id=4, itemNm=테스트 상품4, price=10004, stockNumber=100, itemDetail=테스트 상품 상세 설명4, itemSellStatus=SELL, regTime=2024-10-21T15:44:58.805402, updateTime=2024-10-21T15:44:58.805402)
Item(id=5, itemNm=테스트 상품5, price=10005, stockNumber=100, itemDetail=테스트 상품 상세 설명5, itemSellStatus=SELL, regTime=2024-10-21T15:44:58.809406, updateTime=2024-10-21T15:44:58.809406)

    */
}