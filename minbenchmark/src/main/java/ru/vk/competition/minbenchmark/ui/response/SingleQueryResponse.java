package ru.vk.competition.minbenchmark.ui.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SingleQueryResponse {

    private final Integer queryId;

    private final String query;
}