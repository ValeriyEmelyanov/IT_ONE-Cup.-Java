package com.example.r2dbcpreform.ui.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableColumnResponse {

    private final String title;

    private final String type;
}
