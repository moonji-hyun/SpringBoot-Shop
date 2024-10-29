package com.shop.shop.controller;

import com.shop.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller             // 콘트롤러 역할
@RequestMapping(value = "/thymeleaf") // http://localhost/thymeleaf, 클라이언트의 요청에 대해서 어떤 컨트롤러가 처리할지 매핑하는 어노테이션.
// url에 "/thymeleaf"경로로 오는 요청을 ThymeleafExController가 처리하도록 함.
public class ThymeleafExController {

    @GetMapping(value = "/ex01")    // http://localhost/thymeleaf/ex01 호출 시
    public String thymeleafExample01(Model model){  // Model 객체를 이용해서 데이터를 전이
        model.addAttribute("data", "타임리프 예제 입니다."); // model 객체를 이용해 뷰에 전달한 데이터를 key, value 구조로 넣어줌 (Model객체에 값 추가 k:data , v: 타임리프)
        return "thymeleafEx/thymeleafEx01"; // templates 폴더를 기준으로 뷰의 위치와 이름(thymeleafEx01.html)을 반환 // 리턴 thymeleafEx/thymeleafEx01.html
    }

    @GetMapping(value = "/ex02")    // http://localhost/thymeleaf/ex02 호출 시
    public String thymeleafExample02(Model model){   // Model 객체를 이용해서 데이터를 전이
        ItemDto itemDto = new ItemDto(); // ItemDto 객체 생성
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto); // Model영역에  k: itemDto, v : itemDto객체 전이
        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping(value = "/ex03")
     public String thymeleafExample03(Model model){  // http://localhost/thymeleaf/ex03 호출 시

        List<ItemDto> itemDtoList = new ArrayList<>(); // 리스트 객체 생성

        for(int i=1;i<=10;i++){ // 반복문을 통해 화면에서 출력할 10개의 itemDto 객체를 만들어서 itemDtoList에 넣어줌

            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setItemNm("테스트 상품"+i);
            itemDto.setPrice(1000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList", itemDtoList); // 화면에서 출력할 itemDtoList를 model에 담아서 View에 전달함. // Model영역에 k : itemDtoList, v : itemDto 리스트 객체 전이
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping(value = "/ex04")
    public String thymeleafExample04(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i=1;i<=10;i++){

            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping(value = "/ex05")
    public String thymeleafExample05(){
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping(value = "/ex06")  // ex06?param1=kkk&param2=eee
    public String thymeleafExample06(String param1, String param2, Model model){    // 전달했던 매개 변수와 같은 이름의 String 변수 param1, param2를 파라미터로 설정하면 자동으로 데이터가 바인딩 된다. 매개 변수를 model에 담아서 View로 전달함.
        model.addAttribute("param1", param1);  // url로 받는 param1 을 Model 영역에 저장
        model.addAttribute("param2", param2);  // url로 받는 param2 을 Model 영역에 저장
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping(value = "/ex07")
    public String thymeleafExample07(){
        return "thymeleafEx/thymeleafEx07";
    }/* thymeleafEx07.html 파일에는 따로 header 영역과 footer 영역을 지정하지 않았지만 작성한 내용이 layout1.html 파일에 포함돼 출력된다.*/

}
