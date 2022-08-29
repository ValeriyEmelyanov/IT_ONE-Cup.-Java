package ru.vk.competition.minchecker.dto.results;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleQueryResult {

    public final Integer resultId;
    public final Integer code;

    public SingleQueryResult(
            @JsonProperty("resultId") Integer resultId,
            @JsonProperty("code") Integer code
    ) {
        this.resultId = resultId;
        this.code = code;
    }
}
