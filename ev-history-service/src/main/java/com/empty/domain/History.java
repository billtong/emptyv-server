package com.empty.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "history")
public class History {
    @Id
    private String id;
    @Indexed
    private String userId;
    @Indexed
    private Date created = new Date();
    private OperationEnum operation;
    private Map<String, Object> object;

    public History(String userId, OperationEnum operation, Map object) {
        this.userId = userId;
        this.operation = operation;
        this.object = object;
    }
}
