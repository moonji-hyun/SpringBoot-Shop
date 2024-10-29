package com.shop.shop.entity;

import com.shop.shop.constant.ItemSellStatus;
import com.shop.shop.dto.ItemFormDto;
import com.shop.shop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity  // item클래스를 엔티티로 선언, entity란 데이터베이스의 테이블에 대응하는 클래스
@Table(name="item") // 엔티티와 매핑할 테이블을 지정, item테이블과 매핑되도록 name을 item으로 지정
@Getter
@Setter
@ToString
public class Item extends BaseEntity{

    @Id // entity로 선언한 클래스는 반드시 기본키를 가져야 함, 기본키가 되는 멤버변수에 @id 어노에티션을 붙여줌
    @Column(name="item_id") // 테이블에 매핑될 컬럼의 이름을 @Comumn 어노테이션을 통해 설정, item 클래스의 id변수와 item 테이블의 item_id 컬럼이 매핑되도록 함.
    @GeneratedValue(strategy = GenerationType.AUTO)  // @GeneratedValue 어노테이션을 통해 기본키 생성 전략을 AUTO로 지정
    private Long id;  // 상품코드

    @Column(nullable = false, length = 50)  // 필드와 컬럼 매핑
    // @Column 어노테이션의 nullable 속성을 이용해서 항상 값이 있어야하는 필드는 not null 설정함.
    // String 필드는 default값으로 255가 설정돼있음. 각 String 필드마다 필요한 길이를 length 속성에 default 값을 세팅 함.
    private  String itemNm;     // 상품명

    @Column(name = "price", nullable = false)
    private  int price;     // 가격

    @Column(nullable = false)
    private  int stockNumber;   // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  // 상품 상세 설명

    @Enumerated(EnumType.STRING)    // // constant.ItemSellStatus에 적용된 enum을 처리함.(판매중, 판매 종료)
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태 (/constant/itemSellStatus)

//    private LocalDateTime regTime;  // 등록 시간
//    private LocalDateTime updateTime;   // 수정 시간

/*
    Hibernate:
    create table item (
        id bigint not null,
        item_detail tinytext not null,
        item_nm varchar(50) not null,
        item_sell_status enum ('SELL','SOLD_OUT'),
        price integer not null,
        reg_time datetime(6),
        stock_number integer not null,
        update_time datetime(6),
        primary key (id)
    ) engine=InnoDB
    Hibernate:
        create sequence item_seq start with 1 increment by 50 nocache
*/

    public void updateItem(ItemFormDto itemFormDto){  //
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){  // exception.OutOfStockException 클래스 활용
        int restStock = this.stockNumber - stockNumber;  // 재고 수량에서 주문 후 남은 재고 수량을 구함

        if(restStock<0){        // 재고가 주문 수량보다 작을 경우 재고 부족 예외 발생
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;  // 주문 후 남은 재고 수량을 상품의 현재 재고값으로 할당.
    }


    public void addStock(int stockNumber){ // 상품의 재고를 더해주기(증가) 위한 메서드
        this.stockNumber += stockNumber;
    }

    // @Lob BLOB, CLOB 타입 매핑
    //      BLOB 바이너리 데이터를 외부 DB에 저장 하기 위한 타입(이미지, 사운드, 비디오 등.)
    //      CLOB 사이즈가 큰 데이터를 외부 파일로 저장하기 위한 데이터 타입(문자형 대용량 파일을 저장)

    // @Column
    //      name 필드와 매핑할 컬럼의 이름
    //      unique(DDL) 유니크 제약조건
    //      insertable : insert 가능 여부
    //      updatable : update 가능 여부
    //      length : 문자 길이
    //      nullable(DDL) : null 허용 여부
    //      columnDefinition : 컬럼 정보 직접 기술 (columnDefinition = "varchar(5) default'10' not null")
    //      precision, scale(DDL) : precision 소수점을 포함한 전체 자리수, scale 소수점 자리수(Bouble, float 제외)

    // @GeneratedValue(strategy = GenerationType.???)
    //      auto :  JPS 구현체가 자동으로 생성 전략
    //      IDENTITY : 기본키 생성을 DB에 위임 (마리아DB, mySql -> AUTO_INCREMENT)
    //      SEQUENCE : 데이터베이스 시퀀시 객체를 이용 (@SequenceGenerator 를 사용하여 시퀀스 등록 필요)
    //      TABLE : 키 생성용 테이블 사용, @TableGenerator 필요

    // @CreationTimestamp insert 시 시간 자동 저장
    // @UpdateTimestamp update 시 시간 자동 저장
    // @Transient 해당 필드 데이터베이스 매핑 무시
    // @Temporal 날짜 타입 매핑
    // @CreateDate 엔티티가 생성되어 저장될 때 시간 자동 저장
    // @LastModifiedDate 조회한 엔티티의 값을 변경할 때 시간 자동 저장

}
