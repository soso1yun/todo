package com.llm.todo.domain.user;

import com.llm.todo.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String userName;

    @Column
    private String userId;

    @Column
    private String password;

    @Column
    private String role;

}
