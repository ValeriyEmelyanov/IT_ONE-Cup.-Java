package ru.vk.competition.minbenchmark.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "app_table_query")
@NoArgsConstructor
@AllArgsConstructor
public class TableQuery {

    @Id
    @Column(name = "queryId")
    private Integer queryId;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "query")
    private String query;

}
