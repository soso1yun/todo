package com.llm.todo.domain.todo.dto;

import com.llm.todo.common.entity.BaseEntity;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoSearchRequest extends BaseEntity {

    private String searchText;

    @Pattern(regexp = "^$|^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",message = "날짜는 YYYY-MM-DD 형태로 넣어주세요.")
    private String startDueDate;

    @Pattern(regexp = "^$|^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",message = "날짜는 YYYY-MM-DD 형태로 넣어주세요.")
    private String endDueDate;

    @Pattern(regexp = "^$|^(대기|긴급|진행중|완료)$", message = "상태는 '대기', '긴급', '진행중', '완료' 중 하나여야 합니다.")
    private String status;

}

