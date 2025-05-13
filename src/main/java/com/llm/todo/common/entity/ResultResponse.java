package com.llm.todo.common.entity;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultResponse {

    @Builder.Default private Boolean success = Boolean.TRUE;
    @Builder.Default private String message = "";
    @Builder.Default private Map<String, Object> data = new HashMap<>();

    public Map<String, Object> put(@NonNull String key, @NonNull Object value) {
        this.data.put(key, value);
        return this.data;
    }

    public <T extends Object> T get(@NonNull String key) {
        return (T)this.data.get(key);
    }

}
