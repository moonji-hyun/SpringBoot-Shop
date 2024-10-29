package com.shop.shop.service;

import com.shop.shop.dto.ItemFormDto;
import com.shop.shop.dto.ItemImgDto;
import com.shop.shop.dto.ItemSearchDto;
import com.shop.shop.dto.MainItemDto;
import com.shop.shop.entity.Item;
import com.shop.shop.entity.ItemImg;
import com.shop.shop.repository.ItemImgRepository;
import com.shop.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;  // 아이템 서비스

    private final ItemImgService itemImgService; // 아이템 이미지 서비스

    private final ItemImgRepository itemImgRepository; // 이미지 db 연동

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록
        Item item = itemFormDto.createItem();   // 등록 폼으로 입력 받은 데이터를 이용해 객체 생성
        itemRepository.save(item);              // db에 저장

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0)
                itemImg.setRepimgYn("Y");        // 이미지가 첫번째 일 경우 대표이미지 Y 처리
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 상품 이미지 저장
        }

        return item.getId();                // 저장된 id를 리턴
    }


    @Transactional(readOnly = true) // 상품을 읽어오는 트랜젝션을 읽기 전용으로 설정하면 JPA가 더티체킹(변경감지)를 수행하지 않아 성능이 개선됨.
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        /*해당 상품의 이미지를 조회함. 등록순으로 가지고 오기 위해서 상품 이미지 아이디 오름차순으로 가지고 옴*/
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {   /*조회한 ItemImg엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가*/
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId)     /*상품의 아이티를 통해 상품 엔티티를 죄회. 존재하지 않을 때는 EntityNotFoundException을 발생 시킴*/
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }


    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())    /*상품 등록 화면으로부터 전달 받은 상품 아이디를 이용하여 상품 엔티티를 조회함*/
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);   /*상품 등록 화면으로부터 전달 받은 ItemFormDto를 통해 상품 엔티티를 업데이트*/

        List<Long> itemImgIds = itemFormDto.getItemImgIds(); /*상품 이미지 아이디 리스트를 조회*/

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));/*상품 이미지를 업데이트하기 위해서 updateItemImg() 메소드에 상품 이미지 아이디와, 상품 이미지 파일 정보를 파라미터로 전달*/
        }

        return item.getId();
    }

    @Transactional(readOnly = true) // readOnly = true : 데이터의 수정이 일어나지 않으므로 최적화를 위해 설정
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    } // 페이지 처리되는 아이템 처리용
    //getAdminItemPage() 메서드 : 상품 조회 조건과 페이지 정보를 파라미터로 받아서 상품 데이터를 조회


    @Transactional(readOnly = true) // 메인 페이지용 서비스  /*메인 페이지 보여줄 상품 데이터를 조회하는 메서드*/
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }



}
