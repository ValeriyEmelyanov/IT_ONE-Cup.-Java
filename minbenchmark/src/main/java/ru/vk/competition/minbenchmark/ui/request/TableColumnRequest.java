package ru.vk.competition.minbenchmark.ui.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TableColumnRequest {

    @NotBlank
    private final String title;

    @NotBlank
    private final String type;

}
