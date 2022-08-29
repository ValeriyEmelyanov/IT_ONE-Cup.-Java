package ru.vk.competition.minchecker.service;

import org.junit.jupiter.api.Test;
import ru.vk.competition.minchecker.utils.JsonBuilder;
import ru.vk.competition.minchecker.utils.Urls;

class StartServiceTest {
    private final String RESULT_ID = "resultId";
    private final String API_ROOT = "/api/";
    private final String PARAM_URL_TEMPL = "%s?%s=%d";

    @Test
    void seeSingleQueryUrls() {
        System.out.println(Urls.SINGLE_QUERY + Urls.ADD_NEW_QUERY_RESULT);
        final Integer resultId = 1;
        String url = String.format("%s?%s=%d", Urls.SINGLE_QUERY + Urls.ADD_NEW_QUERY, RESULT_ID, resultId);
        System.out.println(url);
        System.out.println(API_ROOT + url);

        System.out.println(Urls.SINGLE_QUERY + Urls.MODIFY_SINGL_QUERY_RESULT);
        url = String.format("%s?%s=%d", Urls.SINGLE_QUERY + Urls.MODIFY_SINGL_QUERY, RESULT_ID, resultId);
        System.out.println(url);
        System.out.println(API_ROOT + url);

        url = String.format(PARAM_URL_TEMPL,
                Urls.SINGLE_QUERY + Urls.DELETE_SINGL_QUERY,
                RESULT_ID, resultId).replace("{id}", "1");
        System.out.println(url);
    }

    @Test
    void seeJson() {
        String emtyString = "\"\"";
        String json = "{\"tableName\": \"Artists\",\"columnsAmount\": 3,\"columnInfos\": [" +
                "{\"title\": \"id\",\"type\": \"int4\"}," +
                "{\"title\": \"name\",\"type\": \"varchar\"}," +
                "{\"title\": \"age\",\"type\": \"int4\"}]," +
                "\"primaryKey\": \"id\"}";
        System.out.println(json);
    }

}