package ru.vk.competition.minchecker.utils;

public class JsonBuilder {

    private static final String ADD_SINGLE_QUERY_RESULT_JSON = "{\"resultId\":%1$d,\"code\":%2$d}";

    public static String addSingleQueryResultJsonBuilder(Integer resultId, Integer code) {
        return String.format(ADD_SINGLE_QUERY_RESULT_JSON, resultId, code);
    }
}
