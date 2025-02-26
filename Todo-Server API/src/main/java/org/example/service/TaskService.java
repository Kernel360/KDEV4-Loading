package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.example.constants.TaskStatus;
import org.example.model.Task;
import org.example.persist.entity.TaskEntity;
import org.example.persist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j // 로그 처리를 쉽게 하기 위한 롬복 어노테이션. log라는 이름의 logger 객체를 자동으로 생성. log.info(), log.debug() 등의 메서드를 사용해 로깅을 할 수 있음.
@Service // 해당 클래스가 서비스 계층의 컴포넌트임을 스프링에게 알려주고, 자동으로 스프링 컨테이너에 빈으로 등록됨.(의존성 주입, @Autoried 사용 가능)
@RequiredArgsConstructor // final 필드와 @NonNull 필드에 대한 생성자를 자동으로 생성함.
public class TaskService {

    private final TaskRepository taskRepository; // @RequiredArgsConstructor가 자동으로 생성자를 생성해 @Autowired 및 생성자 주입 방식을 사용하지 않아도 됨.

    /**
     * TaskRequest의 인자를 받아 Entity 생성 후 테이블에 삽입함.
     * @return 삽입한 DTO 객체를 반환함.
     */
    public Task add(String title, String description, LocalDate dueDate) {
        var entity = TaskEntity.builder()
                .title(title)
                .description(description)
                .dueDate(Date.valueOf(dueDate))
                .status(TaskStatus.TODO)
                .build();

        var saved = taskRepository.save(entity); // 저장된 Entity의 자동 생성 필드를 채운 후 반환함 (기본 키, created_at 등의 값이 생성되어 필드에 저장된 채로 반환됨)
        return entityToObject(saved);
    }

    public List<Task> getAll() {
         return this.taskRepository.findAll().stream()
                 .map(this::entityToObject) // .map(entity -> entityToObject(entity)), 각 Entity 객체에 대해 entityToObject 메서드를 적용함.
                 .collect(Collectors.toList()); // Collectors.toList() : 스트림의 요소들을 List로 담아줌.
    }

    public List<Task> getByDueDate(String dueDate) {
        return this.taskRepository.findAllByDueDate(Date.valueOf(dueDate)).stream()
                .map(this::entityToObject)
                .collect(Collectors.toList());
    }

    public List<Task> getByStatus(TaskStatus status) {
        return this.taskRepository.findAllByStatus(status).stream()
                .map(this::entityToObject)
                .collect(Collectors.toList());
    }

    public Task getOne(Long id) {
        return this.entityToObject(this.getById(id));
    }

    public Task update(Long id, String title, String description, LocalDate dueDate) {
        var entity = this.getById(id);

        entity.setTitle(Strings.isEmpty(title) ?
                entity.getTitle() : title);
        entity.setDescription(Strings.isEmpty(description) ?
                entity.getDescription() : description);
        entity.setDueDate(Objects.isNull(dueDate) ? // Objects.isNull()은 Object == null을 처리하는 것과 내부적으로는 동일하지만 가독성이 좋아진다.
                entity.getDueDate() : Date.valueOf(dueDate));

        var updated = this.taskRepository.save(entity);

        return this.entityToObject(updated);
    }

    public Task updateStatus(Long id, TaskStatus status) {
        var entity = this.getById(id);

        if(status != null) entity.setStatus(status); // Enum도 null이 될 수 있나? -> Enum도 결국 클래스이기 때문에, 참조하는 값이 없으면 null이 될 수 있음.(초기화하지 않거나 null로 명시했을 경우)
        else throw new IllegalArgumentException("status is null");

        var updated = this.taskRepository.save(entity);
        return this.entityToObject(updated);
    }

    public boolean delete(Long id) {
        try {
            var entity = this.getById(id);
            this.taskRepository.deleteById(id); // 반환 타입 vold, 값을 반환하지 않음.
            return true;
        } catch(Exception e) {
            log.error("an error occurred while deleting id [{}] : [{}]", id, e.getMessage());
            return false;
        }
    }

    private TaskEntity getById(long id) {
        return this.taskRepository.findById(id) // 기본 키에 많이 사용되는 방식. 만약 해당 id를 가진 데이터가 없다면 Optional.empty() 라는 Optional 객체를 반환함.
                .orElseThrow(() -> // 만약 Optional.empty() 객체를 Entity 객체에 대입하려 할 경우 컴파일 오류가 발생함. 따라서 적절한 처리를 해주어야 한다.
                        new IllegalArgumentException(String.format("not exists task id [%d]", id))); // Optional.empty() 객체는 orElse() 등의 메서드로 처리할 수 있으나, get() 메서드를 사용하면 컴파일 오류가 발생하게 됨.
    }

    private Task entityToObject(TaskEntity e) {
        return Task.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .status(e.getStatus())
                .dueDate(e.getDueDate().toString())
                .createdAt(e.getCreatedAt().toLocalDateTime())
                .updatedAt(e.getUpdatedAt().toLocalDateTime())
                .build();
    }
}