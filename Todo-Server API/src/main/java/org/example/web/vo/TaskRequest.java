package org.example.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class TaskRequest {

    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // 직렬화 방식을 지정함. 첫 번째 인자는 JSON에 날짜를 문자열로 변환헤 저장할 것을 지정, 두 번째 인자는 직렬화될 JSON 문자열의 형식을 지정함.
    @JsonDeserialize(using = LocalDateDeserializer.class) // 역직렬화 방식을 지정함. 기본적으로 LocalDate 타입은 yyyy-MM-dd 형식의 문자열을 받는데, JSON의 날짜 포맷을 LocalDate 타입으로 받기 위해 사용한다.
    private LocalDate dueDate;
}
