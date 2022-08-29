package ru.vk.competition.minchecker.utils;

public class JsonBuilder {

    private static final String SINGLE_QUERY_RESULT_JSON = "{\"resultId\":%1$d,\"code\":%2$d}";
//    private static final String TABLE_QUERY_EXT_RESULT_JSON = "{\"resultId\":%1$d,\"code\":%2$d,\"tableQueries\":%3$s}";
    private static final String TABLE_QUERY_EXT_RESULT_JSON = "{\"resultId\":%1$d,\"code\":%2$d,\"tableQuery\":%3$s}";
    private static final String TABLE_QUERY_RESULT_JSON = "{\"resultId\":%1$d,\"code\":%2$d}";
    private static final String TABLE_RESULT_JSON = "{\"resultId\":%1$d,\"code\":%2$d}";
    private static final String TABLE_EXT_RESULT_JSON = "{\"resultId\":%1$d,\"code\":%2$d,\"table\":%3$s}";
    private static final String REPORT_RESULT_JSON = "{\"resultId\":%1$d,\"code\":%2$d}";
    private static final String GET_REPORT_RESULT_JSON = "{\"resultId\":%1$d,\"code\":%2$d,\"getReport\":%3$s}";

    public static String singleQueryResultJsonBuilder(Integer resultId, Integer code) {
        return String.format(SINGLE_QUERY_RESULT_JSON, resultId, code);
    }

    public static String tableQueryResultJsonBuilder(Integer resultId, Integer code, String tableQuery) {
        return String.format(TABLE_QUERY_EXT_RESULT_JSON, resultId, code, tableQuery);
    }

    public static String tableQueryResultJsonBuilder(Integer resultId, Integer code) {
        return String.format(TABLE_QUERY_RESULT_JSON, resultId, code);
    }

    public static String tableResultJsonBuilder(Integer resultId, Integer code) {
        return String.format(TABLE_RESULT_JSON, resultId, code);
    }

    public static String tableResultJsonBuilder(Integer resultId, Integer code, String table) {
        return String.format(TABLE_EXT_RESULT_JSON, resultId, code, table);
    }

    public static String reportResultJsonBuilder(Integer resultId, Integer code) {
        return String.format(REPORT_RESULT_JSON, resultId, code);
    }

    public static String getReportResultJsonBuilder(Integer resultId, Integer code, String getReport) {
        return String.format(GET_REPORT_RESULT_JSON, resultId, code, getReport);
    }
}
