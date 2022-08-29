package ru.vk.competition.minbenchmark.ui.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class SingleQueryRequest {

    @NotNull
    private final Integer queryId;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z][*\\s\\w()-+/,;]+")
    private final String query;

}
