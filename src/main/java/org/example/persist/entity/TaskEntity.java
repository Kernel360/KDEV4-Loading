package org.example.persist.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.constants.TaskStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Builder
@DynamicInsert // Hibernate에 insert 쿼리를 동적으로 생성하도록 지시(null인 필드를 자동으로 제외시켜줌)
@DynamicUpdate // 기본적으로 Hibernate는 모든 필드를 포함한 UPDATE 쿼리를 생성하지만, @DynamicUpdate는 변경된 필드만 업데이트하는 쿼리를 생성함
@Entity(name = "TASK") // 이 클래스가 JPA의 Entity 임을 나타냄 (Entity = 데이터베이스의 테이블과 매핑되는 객체), name 속성으로 테이블의 이름 지정
@NoArgsConstructor // 매개변수가 없는 기본생성자를 자동으로 생성, JPA는 엔티티 객체를 생성할 때 기본생성자를 요구함
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자를 자동으로 생성
public class TaskEntity {

    @Id // 아래 필드를 엔티티의 식별자로 지정, JPA에서 엔티티는 기본 키를 요구하므로, 아래 필드를 기본 키 및 식별자로 사용함.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키의 값을 자동으로 생성하는 방법을 지정. GenerationType.IDENTITY 전략은 Auto_Increment 방식과 같음. 엔티티가 테이블에 삽입될 때 값을 자동으로 생성함.
    private long id;

    private String title;

    private String description;

    @Enumerated(value=EnumType.STRING) // Enum 타입의 필드를 데이터베이스에 저장할 때 어떻게 저장할지 결정. EnumType.STRING -> 문자열로 저장
    private TaskStatus status;

    private Date dueDate;

    @CreationTimestamp // 이 필드가 엔티티 객체가 생성될 때 자동으로 설정되도록 함. (현재 시간으로 설정됨)
    @Column(insertable = false, updatable = false) // @Column은 테이블의 컬럼과 엔티티의 필드를 매핑함. insertable과 updatalbe을 false로 설정한 이유는 해당 컬럼은 값을 삽입할 때 자동으로 값을 생성할 것이므로(@CreationTimestamp 때문) 직접 삽입할 수 없게 만든 것.
    private Timestamp createdAt;

    @UpdateTimestamp // 엔티티가 수정될 때 = 테이블에 UPDATE 쿼리문이 실행될 때 값을 자동으로 생성. 엔티티 객체는 자동 생성되는 값들은 null 값으로 가지고 있다가, 테이블과 상호작용 (Repository.save() 등) 할 때에 생성된 값이 엔티티 객체에 할당됨.
    @Column(insertable = false) // updatable = false ??
    private Timestamp updatedAt;
}
