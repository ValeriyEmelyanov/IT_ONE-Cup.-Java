package ru.vk.competition.minbenchmark.ui.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SingleQueryRequest {

    @NotNull
    private final Integer queryId;

    @NotBlank
    private final String query;

}
