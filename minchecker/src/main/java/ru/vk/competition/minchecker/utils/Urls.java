package ru.vk.competition.minchecker.utils;

public interface Urls {

    String SINGLE_QUERY = "single-query/";
    String ADD_NEW_QUERY_RESULT = "add-new-query-result";
    String ADD_NEW_QUERY = "add-new-query";
    String MODIFY_SINGL_QUERY_RESULT = "add-modify-result";
    String MODIFY_SINGL_QUERY = "modify-single-query";
    String DELETE_SINGL_QUERY_RESULT = "add-delete-result";
    String DELETE_SINGL_QUERY = "delete-single-query-by-id/{id}";
    String EXECUTE_SINGL_QUERY_RESULT = "add-execute-result";
    String EXECUTE_SINGL_QUERY = "execute-single-query-by-id/{id}";
    String GET_SINGL_QUERY_BY_ID_RESULT = "add-get-single-query-by-id-result";
    String GET_SINGL_QUERY_BY_ID = "get-single-query-by-id/{id}";
    String GET_ALL_SINGL_QUERIES = "get-all-single-queries";

    String TABLE_QUERY = "table-query/";
    String ADD_NEW_QUERY_TO_TABLE_RESULT = "add-new-query-to-table-result";
    String ADD_NEW_QUERY_TO_TABLE = "add-new-query-to-table";
    String GET_ALL_QUERIES_BY_TABLE_NAME_RESULT = "get-all-queries-by-table-name-result";
    String GET_ALL_QUERIES_BY_TABLE_NAME = "get-all-queries-by-table-name/{name}";
    String GET_TABLE_QUERY_BY_ID_RESULT = "get-table-query-by-id-result";
    String GET_TABLE_QUERY_BY_ID = "get-table-query-by-id/{id}";
    String EXECUTE_TABLE_QUERY_BY_ID_RESULT = "execute-table-query-by-id-result";
    String EXECUTE_TABLE_QUERY_BY_ID = "execute-table-query-by-id/{id}";
    String DELETE_TABLE_QUERY_BY_ID_RESULT = "delete-table-query-by-id-result";
    String DELETE_TABLE_QUERY_BY_ID = "delete-table-query-by-id/{id}";
    String MODIFY_TABLE_QUERY_BY_ID_RESULT = "modify-query-in-table-result";
    String MODIFY_TABLE_QUERY_BY_ID = "modify-query-in-table";

    String TABLE = "table/";
    String CREATE_TABLE_RESULT = "add-create-table-result";
    String CREATE_TABLE = "create-table";
    String GET_TABLE_BY_NAME_RESULT = "add-get-table-by-name-result";
    String GET_TABLE_BY_NAME = "get-table-by-name/{name}";
    String DROP_TABLE_RESULT = "add-drop-table-result";
    String DROP_TABLE = "drop-table/{name}";

    String REPORT = "report/";
    String CREATE_REPORT_RESULT = "add-create-report-result";
    String CREATE_REPORT = "create-report";
    String GET_REPORT_BY_ID_RESULT = "add-get-report-by-id-result";
    String GET_REPORT_BY_ID = "get-report-by-id/{id}";

}
