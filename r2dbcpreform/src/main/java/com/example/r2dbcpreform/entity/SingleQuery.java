package com.example.r2dbcpreform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table(name = "single_query")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"id"})
public class SingleQuery implements Persistable<Integer> {

    @Id
    @Column("query_id")
    private Integer queryId;

    @Column("query")
    private String query;

    @Transient
    @JsonIgnore
    private boolean newThis;

    @Override
    public Integer getId() {
        return queryId;
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return newThis || queryId == null;
    }

    public SingleQuery setAsNew() {
        newThis = true;
        return this;
    }
}