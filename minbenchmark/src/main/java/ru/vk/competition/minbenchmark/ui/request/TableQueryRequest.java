package ru.vk.competition.minbenchmark.ui.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class TableQueryRequest {

    @NotNull
    private Integer queryId;

    @NotBlank
//    @Size(max = 50)
    private String tableName;

    @NotBlank
//    @Size(max = 120)
//    @Pattern(regexp = "[a-zA-Z][*\\s\\w()-+/,;]+")
    private String query;
}
