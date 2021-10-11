package io.github.syakuis.todo.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "todo", indexes = {
    @Index(name = "IDX_todo_completed", columnList = "completed")
})
public class TodoEntity implements Todo {
    @Id
    @GeneratedValue
    private Long id;

    // todo 작성자 정보

    @Column(nullable = false)
    private String objective;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean completed;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, length = 6)
    private LocalDateTime registeredOn;

    @Column(insertable = false, length = 6)
    private LocalDateTime completedOn;

    @Builder
    public TodoEntity(String objective, boolean completed, LocalDateTime registeredOn, LocalDateTime completedOn) {
        this.objective = objective;
        this.completed = completed;
        this.registeredOn = registeredOn;
        this.completedOn = completedOn;
    }
}
