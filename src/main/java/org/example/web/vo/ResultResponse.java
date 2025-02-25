package org.example.web.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter // 요청의 성공 여부를 전송하는 클래스이기 때문에 값이 변경될 필요가 없다. -> Setter는 설정하지 않음.
@ToString
@AllArgsConstructor
public class ResultResponse {
    private boolean isSuccess;
}
