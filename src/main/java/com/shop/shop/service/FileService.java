package com.shop.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID();                                      // 랜덤 파일명 생성
        // UUID(Universally Unique Identifier)는 서로 다른 개체들을 구별하기 위해서 이름을 부여할 때 사용.
        // 실제 사용 시 중복될 가능성이 거의 없기 때문에 파일의 이름으로 사용하면 파일명 중복 문제를 해결할 수 있다
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자
        String savedFileName = uuid.toString() + extension;                 //  uuid+원래파일명 결합
        /*UUID로 받은 값과 원래 파일의 이름의 확장자를 조합해서 저장될 파일 이름을 만듬*/
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;        // 경로 추가
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        /*FileOutputStream 클래스는 바이트 단위의 출력을 보내는 클래스. 생성자로 파일이 저장될 위치와 파일의 이름을 넘겨 파일에 쓸 파일 출력 스트림을 만듬*/
        fos.write(fileData); /*fileData를 파일 출력 스트림에 입력*/              // 파일 저장
        fos.close();                                                        // 닫고
        return savedFileName;  /*업로드된 파일의 이름을 반환*/                  // 파일명 리턴
    }

    public void deleteFile(String filePath) throws Exception{  // 파일 삭제 메서드
        File deleteFile = new File(filePath);/*파일이 저장된 경로를 이용하여 파일 객체를 생성*/
        if(deleteFile.exists()) {   /*해당 파일이 존재하면 파일을 삭제함*/
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}
