package ru.vk.competition.minchecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import ru.vk.competition.minchecker.utils.JsonBuilder;
import ru.vk.competition.minchecker.utils.RequestBuilder;
import ru.vk.competition.minchecker.utils.Urls;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class StartService {

    OkHttpClient client = new OkHttpClient();

    private final String RESULT_ID = "resultId";
    private final String PARAM_URL_TEMPL = "%s?%s=%d";
    private final String GIST = "-- %s -> ";
    private final String COUNTER = "(%d): ";
    private final String RESULT_CODE_BODY = " -> %d, %s";
    private final String CHECK_CODE_BODY = " -> %d, %s -- END (%d)\n";

    private Integer counter = 1;

    public void onStartMission() {

        boolean checkSingleQuery = true;
        boolean checkTableQuery = true;
        boolean checkTable = true;
        boolean checkReport = true;
        boolean checkEmptyJson = true;
        boolean checkInvalidBody = true;

        // Table 30
        checkCreateTable();

        // Single Query 20
        if (checkSingleQuery) {
            checkAddNewQuery();
            checkAddNewQuerySqlGreater120();
            checkAddNewQueryInvalidId();
            checkAddNewQueryInvalidSql();
            checkAddEmptyQuery();
            checkAddNewQueryEmptyBody();
            checkModifySingleQuery();
            checkModifySingleQueryNotExistsId();
            checkModifySingleQueryInvalidId();
            checkExecuteSingleQuerySelect();
            checkExecuteSingleQuerySelect();
            checkAddNewQueryInsert();
            checkExecuteSingleQueryInsert();
            checkAddNewQueryDropTable();
            checkExecuteSingleQueryDropTable();
            checkExecuteSingleQueryNotExistsId();
            checkExecuteSingleQueryInvalidId();
            checkExecuteSingleQueryInvalidSql();
            checkGetSingleQueryById();
            checkGetSingleQueryByIdNotExistsId();
            checkGetSingleQueryByIdInvalidId();
            checkDeleteSingleQueryNotExistsId();
            checkDeleteSingleQueryInvalidId();

            checkModifySingleQueryEmptyBody();

            //checkGetAllSingleQueries(); // -
        }

        // Table Query 20
        if (checkTableQuery) {
            checkAddNewQueryToTable();
            checkAddNewQueryToTableInvalidSql();
            checkAddNewQueryToTableNotExistsTable();
            checkAddNewQueryToTableInvalidTableName();
            checkAddNewQueryToTableEmptyBody();
            checkGetAllQueriesByTableName();
            checkGetAllQueriesByTableNameNotExistsTable();
            checkGetTableQueryById();
            checkGetTableQueryByIdNotExistsId();
            checkGetTableQueryByIdInvalidId();
            checkExecuteTableQueryById();
            checkExecuteTableInvalidSql();
            checkExecuteTableQueryByIdNotExistsId();
            checkModifyTableQueryById();
            checkModifyTableQueryByIdNotExistsId();
            checkModifyTableQueryByIdInvalidId();
            checkModifyTableQueryByIdNotExistsTable();
            checkDeleteTableQueryByIdNotExistsId();
            checkDeleteTableQueryByIdInvalidId();
            checkDeleteTableQueryByIdNotExistsTable();

            checkExecuteTableQueryByIdInvalidId();
            checkModifyTableQueryByIdEmptyBody();
        }

        // Table 30
        if (checkTable) {
            checkCreateTableAlreadyExists();
            checkCreateTableInvalidTableName();
            checkCreateTableKeyWordTableName(); // 0
            checkCreateTableInvalidColumnType();
            checkCreateTableEmptyColumnType();
            checkCreateTableWrongColumnsNumber();
            checkCreateTableKeyWordColumnName();
            checkCreateTableEmptyColumns();
            checkCreateTableWrongPrimayKey();
            checkCreateTableEmptyPrimayKey();
            checkCreateTableEmptyColumnName(); // ?
            checkCreateTableInvalidColumnName();
            checkCreateTableEmptyBody();
            checkGetTableByName();
            checkGetTableByNameNotExistsTable();
            checkDropTableNotExistsTable();
            checkDropTableInvalidTableName();
            checkCreateTableDoubleColumns();
        }

        // Report 30
        if (checkReport) {
            checkCreateReport();
            checkCreateReportNotExistsTable();
            checkCreateReportWrongTableNumber();
            checkCreateReportEmptyTables();
            checkCreateReportEmptyColumns();
            checkCreateReportDoubleId();
            checkCreateReportInvalidColumnName();
            checkCreateReportInvalidColumnType();
            checkCreateReportEmptyBody();
            checkGetReport();
            checkGetReportNotExistsId();
            checkGetReportInvalidId();
        }

        if (!checkSingleQuery && (checkEmptyJson || checkInvalidBody)) {
            checkAddNewQuery();
        }
        if (!checkTableQuery && (checkEmptyJson || checkInvalidBody)) {
            checkAddNewQueryToTable();
        }
        if (checkEmptyJson) {
            checkAddNewQueryEmptyJson(); // **

            checkModifySingleQueryEmptyJson();

            checkAddNewQueryToTableEmptyJson(); // **
            checkCreateTableEmptyJson();
            checkModifyTableQueryByIdEmptyJson();

            checkCreateReportEmptyJson();
        }

        if (checkInvalidBody) {
            checkAddNewQueryInvalidJson();
            checkAddNewQueryInvalidBody(); // **
            checkModifySingleQueryInvalidBody();

            checkAddNewQueryToTableInvalidBody();
            checkModifyTableQueryByIdInvalidBody();

            checkCreateTableInvalidBody();
            checkCreateTableInvalidJson(); // *

            checkCreateReportInvalidBody(); // *
            checkCreateReportInvalidJson(); // *
        }

        if (checkSingleQuery) {
            checkDeleteSingleQuery();
        }
        if (checkTableQuery) {
            checkDeleteTableQueryById();
        }
        checkDropTable();
    }

    private void postResult(String jsonResult, String url) throws IOException {
        RequestBody resultRequestBody = RequestBody.create(jsonResult, MediaType.parse("application/json"));
        Request resultRequest = RequestBuilder.postRequestBuilder(url, resultRequestBody);
        System.out.print(resultRequest.url());
        Response response = client.newCall(resultRequest).execute();
        System.out.printf(RESULT_CODE_BODY, response.code(), bodyToString(response));
    }

    private String bodyToString(Response response) throws IOException {
        if (response.body() == null) return "";
        String body = response.body().string();
        if (body.length() <= 200) return body;
        return body.substring(0, 199);
    }

    // Single Query

    private void postSingleQueryResult(Integer resultId, Integer code, String url) throws IOException {
        String jsonResult = JsonBuilder.singleQueryResultJsonBuilder(resultId, code);
        postResult(jsonResult, url);
    }

    private void checkAddNewQuery() {
        System.out.printf(GIST, "+");
        String json = "{\"queryId\":1,\"query\":\"select * from Customer\"}";
        checkAddNewQueryWithParams(201, json);
    }

    private void checkAddNewQueryInsert() {
        System.out.printf(GIST, "+");
        String json = "{\"queryId\":2,\"query\":" +
                "\"INSERT INTO Customer" +
                "(CustomerId, FirstName, LastName, Company)" +
                "VALUES (1, 'Bob', 'Bob', 'ABC')\"}";
        checkAddNewQueryWithParams(201, json);
    }

    private void checkAddNewQueryDropTable() {
        System.out.printf(GIST, "+");
        String json = "{\"queryId\":3,\"query\":" +
                "\"DROP TABLE INFORMATION_SCHEMA.ROLES\"}";
        checkAddNewQueryWithParams(201, json);
    }

    private void checkAddNewQuerySqlGreater120() {
        System.out.printf(GIST, "sql > 120");
        String json = "{\"queryId\":10,\"query\":" +
                "\"select T.CustomerId as Id, T.FirstName, T.LastName, T.Company, T.Address, T.City, T.Country, " +
                "T.PostalCode, T.Phone, T.Fax, T.Email, T.SupportRepId from Customer as T\"}";
        checkAddNewQueryWithParams(400, json);
    }

    private void checkAddNewQueryInvalidId() {
        System.out.printf(GIST, "invalid id");
        String json = "{\"queryId\":\"abc\",\"query\":\"select * from Customer\"}";
        checkAddNewQueryWithParams(400, json);
    }

    private void checkAddNewQueryInvalidSql() {
        System.out.printf(GIST, "invalid sql");
        String json = "{\"queryId\":13,\"query\":\"абра-кадабра\"}";
        checkAddNewQueryWithParams(400, json);
    }

    private void checkAddNewQueryInvalidBody() {
        System.out.printf(GIST, "invalid body");
        String json = "{\"queryId\":6,\"text\":\"select * from Customer\"}";
        checkAddNewQueryWithParams(400, json);
    }

    private void checkAddNewQueryInvalidJson() {
        System.out.printf(GIST, "invalid json");
        String json = "{\"queryId\":7,\"query\":\"select * from Customer\"";
        checkAddNewQueryWithParams(400, json);
    }

    private void checkAddEmptyQuery() {
        System.out.printf(GIST, "empty query");
        String json = "{\"queryId\":8,\"query\":\"\"}";
        checkAddNewQueryWithParams(400, json);
    }

    private void checkAddNewQueryEmptyBody() {
        System.out.printf(GIST, "empty body");
        String json = "";
        checkAddNewQueryWithParams(400, json);
    }

    private void checkAddNewQueryEmptyJson() {
        System.out.printf(GIST, "empty json");
        String json = "{}";
        checkAddNewQueryWithParams(400, json);
    }

    private void checkAddNewQueryWithParams(int code, String json) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postSingleQueryResult(resultId, code, Urls.SINGLE_QUERY + Urls.ADD_NEW_QUERY_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.SINGLE_QUERY + Urls.ADD_NEW_QUERY,
                    RESULT_ID, resultId);
            RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = RequestBuilder.postRequestBuilder(url, requestBody);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkModifySingleQuery() {
        System.out.printf(GIST, "+");
        String json = "{\"queryId\":1,\"query\":\"select * from Customer as T\"}";
        checkModifySingleQueryWithParams(200, json);
    }

    private void checkModifySingleQueryNotExistsId() {
        System.out.printf(GIST, "not exists id");
        String json = "{\"queryId\":999,\"query\":\"select * from Customer as T\"}";
        checkModifySingleQueryWithParams(406, json);
    }

    private void checkModifySingleQueryInvalidId() {
        System.out.printf(GIST, "invalid id");
        String json = "{\"queryId\":\"abc\",\"query\":\"select * from Customer as T\"}";
        checkModifySingleQueryWithParams(406, json);
    }

    private void checkModifySingleQueryEmptyBody() {
        System.out.printf(GIST, "empty body");
        String json = "{}";
        checkModifySingleQueryWithParams(406, json);
    }

    private void checkModifySingleQueryEmptyJson() {
        System.out.printf(GIST, "empty json");
        String json = "{}";
        checkModifySingleQueryWithParams(406, json);
    }

    private void checkModifySingleQueryInvalidBody() {
        System.out.printf(GIST, "invalid body");
        String json = "{\"queryId\":1,\"text\":\"select * from Customer as T\"}";
        checkModifySingleQueryWithParams(406, json);
    }

    private void checkModifySingleQueryWithParams(int code, String json) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postSingleQueryResult(resultId, code, Urls.SINGLE_QUERY + Urls.MODIFY_SINGL_QUERY_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.SINGLE_QUERY + Urls.MODIFY_SINGL_QUERY,
                    RESULT_ID, resultId);
            RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = RequestBuilder.putRequestBuilder(url, requestBody);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkDeleteSingleQuery() {
        System.out.printf(GIST, "+");
        checkDeleteSingleQueryWithParams(202, "1");
    }

    private void checkDeleteSingleQueryNotExistsId() {
        System.out.printf(GIST, "not exists id");
        checkDeleteSingleQueryWithParams(406, "999");
    }

    private void checkDeleteSingleQueryInvalidId() {
        System.out.printf(GIST, "invalid id");
        checkDeleteSingleQueryWithParams(406, "abc");
    }

    private void checkDeleteSingleQueryWithParams(int code, String id) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postSingleQueryResult(resultId, code, Urls.SINGLE_QUERY + Urls.DELETE_SINGL_QUERY_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.SINGLE_QUERY + Urls.DELETE_SINGL_QUERY,
                    RESULT_ID, resultId).replace("{id}", id);
            Request request = RequestBuilder.deleteRequestBuilder(url);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkExecuteSingleQuerySelect() {
        System.out.printf(GIST, "+");
        checkExecuteSingleQueryWithParams(201, "1");
    }

    private void checkExecuteSingleQueryInsert() {
        System.out.printf(GIST, "+ [insert]");
        checkExecuteSingleQueryWithParams(201, "2");
    }

    private void checkExecuteSingleQueryDropTable() {
        System.out.printf(GIST, "drop table");
        checkExecuteSingleQueryWithParams(406, "3");
    }

    private void checkExecuteSingleQueryNotExistsId() {
        System.out.printf(GIST, "not exists id");
        checkExecuteSingleQueryWithParams(406, "999");
    }

    private void checkExecuteSingleQueryInvalidId() {
        System.out.printf(GIST, "invalid id");
        checkExecuteSingleQueryWithParams(406, "abc");
    }

    private void checkExecuteSingleQueryInvalidSql() {
        System.out.printf(GIST, "invalid sql");
        checkExecuteSingleQueryWithParams(406, "13");
    }

    private void checkExecuteSingleQueryWithParams(int code, String queryId) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postSingleQueryResult(resultId, code, Urls.SINGLE_QUERY + Urls.EXECUTE_SINGL_QUERY_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.SINGLE_QUERY + Urls.EXECUTE_SINGL_QUERY,
                    RESULT_ID, resultId).replace("{id}", queryId);
            Request request = RequestBuilder.getRequestBuilder(url);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkGetSingleQueryById() {
        System.out.printf(GIST, "+");
        checkGetSingleQueryByIdWithParams(200, "1");
    }

    private void checkGetSingleQueryByIdNotExistsId() {
        System.out.printf(GIST, "not exists id");
        checkGetSingleQueryByIdWithParams(500, "999");
    }

    private void checkGetSingleQueryByIdInvalidId() {
        System.out.printf(GIST, "invalid id");
        checkGetSingleQueryByIdWithParams(500, "999");
    }

    private void checkGetSingleQueryByIdWithParams(int code, String queryId) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postSingleQueryResult(resultId, code, Urls.SINGLE_QUERY + Urls.GET_SINGL_QUERY_BY_ID_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.SINGLE_QUERY + Urls.GET_SINGL_QUERY_BY_ID,
                    RESULT_ID, resultId).replace("{id}", queryId);
            Request request = RequestBuilder.getRequestBuilder(url);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkGetAllSingleQueries() {
        System.out.printf(COUNTER, counter);
        try {
            String url = Urls.SINGLE_QUERY + Urls.GET_ALL_SINGL_QUERIES;
            Request request = RequestBuilder.getRequestBuilder(url);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    // Table Query

    private void postTableQueryResult(Integer resultId, Integer code, String url, String tableQuery) throws IOException {
        String jsonResult = JsonBuilder.tableQueryResultJsonBuilder(resultId, code, tableQuery);
        postResult(jsonResult, url);
    }

    private void postTableQueryResult(Integer resultId, Integer code, String url) throws IOException {
        String jsonResult = JsonBuilder.tableQueryResultJsonBuilder(resultId, code);
        postResult(jsonResult, url);
    }

    private void checkAddNewQueryToTable() {
        System.out.printf(GIST, "+");
        String json = "{\"queryId\":1,\"tableName\":\"Customer\",\"query\":\"SELECT * FROM Customer\"}";
        checkAddNewQueryToTableWithParams(201, json);
    }

    private void checkAddNewQueryToTableInvalidSql() {
        System.out.printf(GIST, "invalid sql");
        String json = "{\"queryId\":13,\"tableName\":\"Customer\",\"query\":\"абра-кадабра\"}";
        checkAddNewQueryToTableWithParams(406, json);
    }

    private void checkAddNewQueryToTableNotExistsTable() {
        System.out.printf(GIST, "not exists table");
        String json = "{\"queryId\":13,\"tableName\":\"no_such_table\",\"query\":\"SELECT * FROM no_such_table\"}";
        checkAddNewQueryToTableWithParams(406, json);
    }

    private void checkAddNewQueryToTableEmptyBody() {
        System.out.printf(GIST, "empty body");
        String json = "";
        checkAddNewQueryToTableWithParams(406, json);
    }

    private void checkAddNewQueryToTableEmptyJson() {
        System.out.printf(GIST, "empty json");
        String json = "{}";
        checkAddNewQueryToTableWithParams(406, json);
    }

    private void checkAddNewQueryToTableInvalidBody() {
        System.out.printf(GIST, "invalid body");
        String json = "{\"queryId\":1,\"name\":\"Customer\",\"query\":\"SELECT * FROM Customer\"}";
        checkAddNewQueryToTableWithParams(406, json);
    }

    private void checkAddNewQueryToTableInvalidTableName() {
        System.out.printf(GIST, "invalid table name");
        String json = "{\"queryId\":6,\"tableName\":\"-?!\",\"query\":\"SELECT * FROM no_such_table\"}";
        checkAddNewQueryToTableWithParams(406, json);
    }

    private void checkAddNewQueryToTableWithParams(int code, String json) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postTableQueryResult(resultId, code, Urls.TABLE_QUERY + Urls.ADD_NEW_QUERY_TO_TABLE_RESULT, json);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.TABLE_QUERY + Urls.ADD_NEW_QUERY_TO_TABLE,
                    RESULT_ID, resultId);
            RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = RequestBuilder.postRequestBuilder(url, requestBody);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkGetAllQueriesByTableName() {
        System.out.printf(GIST, "+");
        checkGetAllQueriesByTableNameWithParams(200, "Customer");
    }

    private void checkGetAllQueriesByTableNameNotExistsTable() {
        System.out.printf(GIST, "not exists table");
        checkGetAllQueriesByTableNameWithParams(200, "no_such_table");
    }

    private void checkGetAllQueriesByTableNameWithParams(int code, String tableName) {
        System.out.printf(COUNTER, counter);
        try {
            String json = "";

            final Integer resultId = counter;
//            postTableQueryResult(resultId, code,
//                    Urls.TABLE_QUERY + Urls.GET_ALL_QUERIES_BY_TABLE_NAME_RESULT, json);
            postTableQueryResult(resultId, code, Urls.TABLE_QUERY + Urls.GET_ALL_QUERIES_BY_TABLE_NAME_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.TABLE_QUERY + Urls.GET_ALL_QUERIES_BY_TABLE_NAME,
                    RESULT_ID, resultId).replace("{name}", tableName);
            Request request = RequestBuilder.getRequestBuilder(url);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkGetTableQueryById() {
        System.out.printf(GIST, "+");
        checkGetTableQueryByIdWithParams(200, "1");
    }

    private void checkGetTableQueryByIdNotExistsId() {
        System.out.printf(GIST, "not exixsts id");
        checkGetTableQueryByIdWithParams(500, "999");
    }

    private void checkGetTableQueryByIdInvalidId() {
        System.out.printf(GIST, "invalid id");
        checkGetTableQueryByIdWithParams(500, "abc");
    }

    private void checkGetTableQueryByIdWithParams(int code, String queryId) {
        System.out.printf(COUNTER, counter);
        try {
            String json = "{\"queryId\":1,\"tableName\":\"Customer\",\"query\":\"SELECT * FROM Customer\"}";

            final Integer resultId = counter;
            postTableQueryResult(resultId, code, Urls.TABLE_QUERY + Urls.GET_TABLE_QUERY_BY_ID_RESULT, json);
//            postTableQueryResult(resultId, code, Urls.TABLE_QUERY + Urls.GET_TABLE_QUERY_BY_ID_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.TABLE_QUERY + Urls.GET_TABLE_QUERY_BY_ID,
                    RESULT_ID, resultId).replace("{id}", queryId);
            Request request = RequestBuilder.getRequestBuilder(url);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkExecuteTableQueryById() {
        System.out.printf(GIST, "+");
        checkExecuteTableQueryByIdWithParams(201, "1");
    }

    private void checkExecuteTableInvalidSql() {
        System.out.printf(GIST, "invalid sql");
        checkExecuteTableQueryByIdWithParams(406, "13");
    }

    private void checkExecuteTableQueryByIdNotExistsId() {
        System.out.printf(GIST, "not exists id");
        checkExecuteTableQueryByIdWithParams(406, "999");
    }

    private void checkExecuteTableQueryByIdInvalidId() {
        System.out.printf(GIST, "not exists id");
        checkExecuteTableQueryByIdWithParams(406, "abc");
    }

    private void checkExecuteTableQueryByIdWithParams(int code, String queryId) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postTableQueryResult(resultId, code, Urls.TABLE_QUERY + Urls.EXECUTE_TABLE_QUERY_BY_ID_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.TABLE_QUERY + Urls.EXECUTE_TABLE_QUERY_BY_ID,
                    RESULT_ID, resultId).replace("{id}", queryId);
            Request request = RequestBuilder.getRequestBuilder(url);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkDeleteTableQueryById() {
        System.out.printf(GIST, "+");
        checkDeleteTableQueryByIdWithParams(202, "1");
    }

    private void checkDeleteTableQueryByIdNotExistsId() {
        System.out.printf(GIST, "not exists id");
        checkDeleteTableQueryByIdWithParams(406, "999");
    }

    private void checkDeleteTableQueryByIdInvalidId() {
        System.out.printf(GIST, "invalid id");
        checkDeleteTableQueryByIdWithParams(406, "abc");
    }

    private void checkDeleteTableQueryByIdNotExistsTable() {
        System.out.printf(GIST, "not exists table");
        checkDeleteTableQueryByIdWithParams(406, "13");
    }

    private void checkDeleteTableQueryByIdWithParams(int code, String id) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postTableQueryResult(resultId, 201, Urls.TABLE_QUERY + Urls.DELETE_TABLE_QUERY_BY_ID_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.TABLE_QUERY + Urls.DELETE_TABLE_QUERY_BY_ID,
                    RESULT_ID, resultId).replace("{id}", id);
            Request request = RequestBuilder.deleteRequestBuilder(url);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkModifyTableQueryById() {
        System.out.printf(GIST, "+");
        String json = "{\"queryId\":1,\"tableName\":\"customer\",\"query\":\"SELECT * FROM Customer AS t;\"}";
        checkModifyTableQueryByIdWithParams(200, json);
    }

    private void checkModifyTableQueryByIdEmptyBody() {
        System.out.printf(GIST, "empty body");
        String json = "";
        checkModifyTableQueryByIdWithParams(406, json);
    }

    private void checkModifyTableQueryByIdEmptyJson() {
        System.out.printf(GIST, "empty body");
        String json = "{}";
        checkModifyTableQueryByIdWithParams(406, json);
    }

    private void checkModifyTableQueryByIdInvalidBody() {
        System.out.printf(GIST, "invalid body");
        String json = "{\"queryId\":2,\"name\":\"customer\",\"query\":\"SELECT * FROM Customer AS t;\"}";
        checkModifyTableQueryByIdWithParams(200, json);
    }

    private void checkModifyTableQueryByIdNotExistsTable() {
        System.out.printf(GIST, "not exists table");
        String json = "{\"queryId\":1,\"tableName\":\"no_such_table\",\"query\":\"SELECT * FROM no_such_table AS t;\"}";
        checkModifyTableQueryByIdWithParams(406, json);
    }

    private void checkModifyTableQueryByIdNotExistsId() {
        System.out.printf(GIST, "not exists id");
        String json = "{\"queryId\":999,\"tableName\":\"customer\",\"query\":\"SELECT * FROM Customer AS t;\"}";
        checkModifyTableQueryByIdWithParams(406, json);
    }

    private void checkModifyTableQueryByIdInvalidId() {
        System.out.printf(GIST, "invalid id");
        String json = "{\"queryId\":\"abc\",\"tableName\":\"customer\",\"query\":\"SELECT * FROM Customer AS t;\"}";
        checkModifyTableQueryByIdWithParams(406, json);
    }

    private void checkModifyTableQueryByIdWithParams(int code, String json) {
        System.out.printf(COUNTER, counter);
        try {

            final Integer resultId = counter;
//            postTableQueryResult(resultId, code, Urls.TABLE_QUERY + Urls.MODIFY_TABLE_QUERY_BY_ID_RESULT, json);
            postTableQueryResult(resultId, code, Urls.TABLE_QUERY + Urls.MODIFY_TABLE_QUERY_BY_ID_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.TABLE_QUERY + Urls.MODIFY_TABLE_QUERY_BY_ID,
                    RESULT_ID, resultId);
            RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = RequestBuilder.putRequestBuilder(url, requestBody);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    // Table

    private void postTableResult(Integer resultId, Integer code, String url) throws IOException {
        String jsonResult = JsonBuilder.tableResultJsonBuilder(resultId, code);
        postResult(jsonResult, url);
    }

    private void postTableResult(Integer resultId, Integer code, String url, String tableJson) throws IOException {
        String jsonResult = JsonBuilder.tableResultJsonBuilder(resultId, code, tableJson);
        postResult(jsonResult, url);
    }

    private void checkCreateTable() {
        System.out.printf(GIST, "+");
        String json = "{\"tableName\":\"Customer\",\"columnsAmount\":12," +
                "\"primaryKey\":\"CustomerId\",\"columnInfos\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"INT\"}," +
                "{\"title\":\"FirstName\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"LastName\",\"type\":\"VARCHAR(20)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}]}";
        checkCreateTableWithParams(201, json);
    }

    private void checkCreateTableAlreadyExists() {
        System.out.printf(GIST, "table already exists");
        String json = "{\"tableName\":\"Customer\",\"columnsAmount\":12," +
                "\"primaryKey\":\"CustomerId\",\"columnInfos\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"INT\"}," +
                "{\"title\":\"FirstName\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"LastName\",\"type\":\"VARCHAR(20)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}]}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableInvalidTableName() {
        System.out.printf(GIST, "invalid table name");
        String json = "{\"tableName\":\"-!?\",\"columnsAmount\":12," +
                "\"primaryKey\":\"CustomerId\",\"columnInfos\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"INT\"}," +
                "{\"title\":\"FirstName\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"LastName\",\"type\":\"VARCHAR(20)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}]}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableKeyWordTableName() {
        System.out.printf(GIST, "key word table name");
        String json = "{\"tableName\": \"insert\",\"columnsAmount\": 3,\"columnInfos\": [" +
                "{\"title\": \"id\",\"type\": \"int4\"}," +
                "{\"title\": \"name\",\"type\": \"varchar\"}," +
                "{\"title\": \"age\",\"type\": \"int4\"}]," +
                "\"primaryKey\": \"id\"}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableInvalidColumnType() {
        System.out.printf(GIST, "invalid column type");
        String json = "{\"tableName\":\"CustomerInvColType\",\"columnsAmount\":12," +
                "\"primaryKey\":\"CustomerId\",\"columnInfos\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"INVALID_TYPE\"}," +
                "{\"title\":\"FirstName\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"LastName\",\"type\":\"VARCHAR(20)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}]}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableEmptyColumnType() {
        System.out.printf(GIST, "empty column type");
        String json = "{\"tableName\":\"CustomerEmpColType\",\"columnsAmount\":12," +
                "\"primaryKey\":\"CustomerId\",\"columnInfos\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"\"}," +
                "{\"title\":\"FirstName\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"LastName\",\"type\":\"VARCHAR(20)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}]}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableEmptyColumnName() {
        System.out.printf(GIST, "empty column name");
        String json = "{\"tableName\":\"CustomerEmpColName\",\"columnsAmount\":12," +
                "\"primaryKey\":\"CustomerId\",\"columnInfos\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"INT\"}," +
                "{\"title\":\"\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"LastName\",\"type\":\"VARCHAR(20)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}]}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableInvalidColumnName() {
        System.out.printf(GIST, "invalid column name");
        String json = "{\"tableName\":\"CustomerInvColName\",\"columnsAmount\":12," +
                "\"primaryKey\":\"CustomerId\",\"columnInfos\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"INT\"}," +
                "{\"title\":\"-?!\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"LastName\",\"type\":\"VARCHAR(20)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}]}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableKeyWordColumnName() {
        System.out.printf(GIST, "key word column name");
        String json = "{\"tableName\": \"Artists\",\"columnsAmount\": 3,\"columnInfos\": [" +
                "{\"title\": \"id\",\"type\": \"int4\"}," +
                "{\"title\": \"select\",\"type\": \"varchar\"}," +
                "{\"title\": \"age\",\"type\": \"int4\"}]," +
                "\"primaryKey\": \"id\"}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableWrongColumnsNumber() {
        System.out.printf(GIST, "wrong columns number");
        String json = "{\"tableName\":\"CustomerWrongColNum\",\"columnsAmount\":3," +
                "\"primaryKey\":\"CustomerId\",\"columnInfos\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"INT\"}," +
                "{\"title\":\"FirstName\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"LastName\",\"type\":\"VARCHAR(20)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}]}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableEmptyColumns() {
        System.out.printf(GIST, "empty columns");
        String json = "{\"tableName\":\"CustomerEmptCol\",\"columnsAmount\":0," +
                "\"primaryKey\":\"CustomerId\",\"columnInfos\":" +
                "[]}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableWrongPrimayKey() {
        System.out.printf(GIST, "wrong primary key");
        String json = "{\"tableName\":\"CustomerWrongPK\",\"columnsAmount\":12," +
                "\"primaryKey\":\"wrongvalue\",\"columnInfos\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"INT\"}," +
                "{\"title\":\"FirstName\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"LastName\",\"type\":\"VARCHAR(20)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}]}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableEmptyPrimayKey() {
        System.out.printf(GIST, "empty primary key");
        String json = "{\"tableName\":\"CustomerEmptyPK\",\"columnsAmount\":12," +
                "\"primaryKey\":\"\",\"columnInfos\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"INT\"}," +
                "{\"title\":\"FirstName\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"LastName\",\"type\":\"VARCHAR(20)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}]}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableEmptyBody() {
        System.out.printf(GIST, "empty body");
        String json = "";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableEmptyJson() {
        System.out.printf(GIST, "empty json");
        String json = "{}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableInvalidBody() {
        System.out.printf(GIST, "invalid body");
        String json = "{\"tableName\":\"CustomerInvBody\",\"columnsAmount\":12," +
                "\"primaryKey\":\"CustomerId\",\"columnsData\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"INT\"}," +
                "{\"title\":\"FirstName\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"LastName\",\"type\":\"VARCHAR(20)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}]}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableInvalidJson() {
        System.out.printf(GIST, "invalid json");
        String json = "{\"tableName\":\"CustomerInvJson\",\"columnsAmount\":12," +
                "\"primaryKey\":\"CustomerId\",\"columnInfos\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"INT\"}," +
                "{\"title\":\"FirstName\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"LastName\",\"type\":\"VARCHAR(20)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableDoubleColumns() {
        System.out.printf(GIST, "double columns");
        String json = "{\"tableName\":\"CustomerDoubleCol\",\"columnsAmount\":12," +
                "\"primaryKey\":\"CustomerId\",\"columnInfos\":" +
                "[{\"title\":\"CustomerId\",\"type\":\"INT\"}," +
                "{\"title\":\"FirstName\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"FirstName\",\"type\":\"VARCHAR(41)\"}," +
                "{\"title\":\"Company\",\"type\":\"VARCHAR(80)\"}," +
                "{\"title\":\"Address\",\"type\":\"VARCHAR(70)\"}," +
                "{\"title\":\"City\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"Country\",\"type\":\"VARCHAR(40)\"}," +
                "{\"title\":\"PostalCode\",\"type\":\"VARCHAR(10)\"}," +
                "{\"title\":\"Phone\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Fax\",\"type\":\"VARCHAR(24)\"}," +
                "{\"title\":\"Email\",\"type\":\"VARCHAR(60)\"}," +
                "{\"title\":\"SupportRepId\",\"type\":\"int4\"}]}";
        checkCreateTableWithParams(406, json);
    }

    private void checkCreateTableWithParams(int code, String json) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postTableResult(resultId, code, Urls.TABLE + Urls.CREATE_TABLE_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.TABLE + Urls.CREATE_TABLE,
                    RESULT_ID, resultId);
            RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = RequestBuilder.postRequestBuilder(url, requestBody);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkGetTableByName() {
        System.out.printf(GIST, "+");
        checkGetTableByNameWithParams(200, "Customer");
    }

    private void checkGetTableByNameNotExistsTable() {
        System.out.printf(GIST, "not exists table");
        checkGetTableByNameWithParams(200, "no_such_table");
    }

    private void checkGetTableByNameWithParams(int code, String tableName) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postTableResult(resultId, code, Urls.TABLE + Urls.GET_TABLE_BY_NAME_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.TABLE + Urls.GET_TABLE_BY_NAME,
                    RESULT_ID, resultId).replace("{name}", tableName);
            Request request = RequestBuilder.getRequestBuilder(url);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkDropTable() {
        System.out.printf(GIST, "+");
        checkDropTableWithParams(201, "Customer");
    }

    private void checkDropTableNotExistsTable() {
        System.out.printf(GIST, "not exists table");
        checkDropTableWithParams(406, "no_such_table");
    }

    private void checkDropTableInvalidTableName() {
        System.out.printf(GIST, "invalid table name");
        checkDropTableWithParams(406, "-?!");
    }

    private void checkDropTableWithParams(int code, String tableName) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postTableResult(resultId, code, Urls.TABLE + Urls.DROP_TABLE_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.TABLE + Urls.DROP_TABLE,
                    RESULT_ID, resultId).replace("{name}", tableName);
            Request request = RequestBuilder.deleteRequestBuilder(url);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    // Report

    private void postReportResult(Integer resultId, Integer code, String url) throws IOException {
        String jsonResult = JsonBuilder.reportResultJsonBuilder(resultId, code);
        postResult(jsonResult, url);
    }

    private void postGetReportResult(Integer resultId, Integer code, String url, String getReport) throws IOException {
        String jsonResult = JsonBuilder.getReportResultJsonBuilder(resultId, code, getReport);
        postResult(jsonResult, url);
    }

    private void checkCreateReport() {
        System.out.printf(GIST, "+");
        String json = "{\"reportId\": 1,\"tableAmount\": 1,\"tables\": [" +
                "{\"tableName\": \"customer\",\"columns\": [" +
                "{\"title\": \"CustomerId\",\"type\": \"int4\"}," +
                "{\"title\": \"FirstName\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"LastName\",\"type\": \"VARCHAR(20)\"}," +
                "{\"title\": \"Company\",\"type\": \"VARCHAR(80)\"}," +
                "{\"title\": \"Address\",\"type\": \"VARCHAR(70)\"}," +
                "{\"title\": \"City\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"Country\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"PostalCode\",\"type\": \"VARCHAR(10)\"}," +
                "{\"title\": \"Phone\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Fax\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Email\",\"type\": \"VARCHAR(60)\"}," +
                "{\"title\": \"SupportRepId\",\"type\": \"int4\"}]}]}";
        checkCreateReportWithParams(201, json);
    }

    private void checkCreateReportNotExistsTable() {
        System.out.printf(GIST, "not exists table");
        String json = "{\"reportId\": 2,\"tableAmount\": 1,\"tables\":[" +
                "{\"tableName\": \"Cars\",\"columns\": [" +
                "{\"title\": \"id\",\"type\": \"int4\"}," +
                "{\"title\": \"price\",\"type\": \"int4\"}]}]}";
        checkCreateReportWithParams(406, json);
    }

    private void checkCreateReportWrongTableNumber() {
        System.out.printf(GIST, "wrong table name");
        String json = "{\"reportId\": 3,\"tableAmount\": 2,\"tables\": [" +
                "{\"tableName\": \"customer\",\"columns\": [" +
                "{\"title\": \"CustomerId\",\"type\": \"int4\"}," +
                "{\"title\": \"FirstName\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"LastName\",\"type\": \"VARCHAR(20)\"}," +
                "{\"title\": \"Company\",\"type\": \"VARCHAR(80)\"}," +
                "{\"title\": \"Address\",\"type\": \"VARCHAR(70)\"}," +
                "{\"title\": \"City\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"Country\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"PostalCode\",\"type\": \"VARCHAR(10)\"}," +
                "{\"title\": \"Phone\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Fax\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Email\",\"type\": \"VARCHAR(60)\"}," +
                "{\"title\": \"SupportRepId\",\"type\": \"int4\"}]}]}";
        checkCreateReportWithParams(406, json);
    }

    private void checkCreateReportEmptyTables() {
        System.out.printf(GIST, "empty tables");
        String json = "{\"reportId\": 1,\"tableAmount\": 0,\"tables\": []}";
        checkCreateReportWithParams(406, json);
    }

    private void checkCreateReportEmptyColumns() {
        System.out.printf(GIST, "empty columns");
        String json = "{\"reportId\": 1,\"tableAmount\": 1,\"tables\": [" +
                "{\"tableName\": \"customer\",\"columns\": []}]}";
        checkCreateReportWithParams(406, json);
    }

    private void checkCreateReportDoubleId() {
        System.out.printf(GIST, "double id");
        String json = "{\"reportId\": 1,\"tableAmount\": 1,\"tables\": [" +
                "{\"tableName\": \"Customer\",\"columns\": [" +
                "{\"title\": \"CustomerId\",\"type\": \"int4\"}," +
                "{\"title\": \"FirstName\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"LastName\",\"type\": \"VARCHAR(20)\"}," +
                "{\"title\": \"Company\",\"type\": \"VARCHAR(80)\"}," +
                "{\"title\": \"Address\",\"type\": \"VARCHAR(70)\"}," +
                "{\"title\": \"City\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"Country\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"PostalCode\",\"type\": \"VARCHAR(10)\"}," +
                "{\"title\": \"Phone\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Fax\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Email\",\"type\": \"VARCHAR(60)\"}," +
                "{\"title\": \"SupportRepId\",\"type\": \"int4\"}]}]}";
        checkCreateReportWithParams(406, json);
    }

    private void checkCreateReportInvalidColumnType() {
        System.out.printf(GIST, "invalid column type");
        String json = "{\"reportId\": 5\"tableAmount\": 1,\"tables\": [" +
                "{\"tableName\": \"customer\",\"columns\": [" +
                "{\"title\": \"CustomerId\",\"type\": \"wrong_type\"}," +
                "{\"title\": \"FirstName\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"LastName\",\"type\": \"VARCHAR(20)\"}," +
                "{\"title\": \"Company\",\"type\": \"VARCHAR(80)\"}," +
                "{\"title\": \"Address\",\"type\": \"VARCHAR(70)\"}," +
                "{\"title\": \"City\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"Country\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"PostalCode\",\"type\": \"VARCHAR(10)\"}," +
                "{\"title\": \"Phone\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Fax\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Email\",\"type\": \"VARCHAR(60)\"}," +
                "{\"title\": \"SupportRepId\",\"type\": \"int4\"}]}]}";
        checkCreateReportWithParams(406, json);
    }

    private void checkCreateReportInvalidColumnName() {
        System.out.printf(GIST, "invalid column name");
        String json = "{\"reportId\": 6,\"tableAmount\": 1,\"tables\": [" +
                "{\"tableName\": \"customer\",\"columns\": [" +
                "{\"title\": \"MasterId\",\"type\": \"int4\"}," +
                "{\"title\": \"FirstName\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"LastName\",\"type\": \"VARCHAR(20)\"}," +
                "{\"title\": \"Company\",\"type\": \"VARCHAR(80)\"}," +
                "{\"title\": \"Address\",\"type\": \"VARCHAR(70)\"}," +
                "{\"title\": \"City\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"Country\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"PostalCode\",\"type\": \"VARCHAR(10)\"}," +
                "{\"title\": \"Phone\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Fax\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Email\",\"type\": \"VARCHAR(60)\"}," +
                "{\"title\": \"SupportRepId\",\"type\": \"int4\"}]}]}";
        checkCreateReportWithParams(406, json);
    }

    private void checkCreateReportInvalidBody() {
        System.out.printf(GIST, "invalid body");
        String json = "{\"reportId\": 7,\"tables\": [" +
                "{\"tableName\": \"customer\",\"columns\": [" +
                "{\"title\": \"CustomerId\",\"type\": \"int4\",}," +
                "{\"title\": \"FirstName\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"LastName\",\"type\": \"VARCHAR(20)\"}," +
                "{\"title\": \"Company\",\"type\": \"VARCHAR(80)\"}," +
                "{\"title\": \"Address\",\"type\": \"VARCHAR(70)\"}," +
                "{\"title\": \"City\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"Country\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"PostalCode\",\"type\": \"VARCHAR(10)\"}," +
                "{\"title\": \"Phone\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Fax\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Email\",\"type\": \"VARCHAR(60)\"}," +
                "{\"title\": \"SupportRepId\",\"type\": \"int4\"}]}]}";
        checkCreateReportWithParams(406, json);
    }

    private void checkCreateReportInvalidJson() {
        System.out.printf(GIST, "invalid json");
        String json = "{\"reportId\": 8,\"tableAmount\": 1,\"tables\": [" +
                "{\"tableName\": \"customer\",\"columns\": [" +
                "{\"title\": \"CustomerId\",\"type\": \"int4\",}," +
                "{\"title\": \"FirstName\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"LastName\",\"type\": \"VARCHAR(20)\"}," +
                "{\"title\": \"Company\",\"type\": \"VARCHAR(80)\"}," +
                "{\"title\": \"Address\",\"type\": \"VARCHAR(70)\"}," +
                "{\"title\": \"City\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"Country\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"PostalCode\",\"type\": \"VARCHAR(10)\"}," +
                "{\"title\": \"Phone\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Fax\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Email\",\"type\": \"VARCHAR(60)\"}," +
                "{\"title\": \"SupportRepId\",\"type\": \"int4\"}]}";
        checkCreateReportWithParams(406, json);
    }

    private void checkCreateReportEmptyBody() {
        System.out.printf(GIST, "empty body");
        String json = "";
        checkCreateReportWithParams(406, json);
    }

    private void checkCreateReportEmptyJson() {
        System.out.printf(GIST, "empty json");
        String json = "{}";
        checkCreateReportWithParams(406, json);
    }

    private void checkCreateReportWithParams(int code, String json) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postReportResult(resultId, code, Urls.REPORT + Urls.CREATE_REPORT_RESULT);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.REPORT + Urls.CREATE_REPORT,
                    RESULT_ID, resultId);
            RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = RequestBuilder.postRequestBuilder(url, requestBody);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

    private void checkGetReport() {
        System.out.printf(GIST, "+");
        String getReport = "{\"reportId\":1,\"tableAmount\": 1,\"tables\": [" +
                "{\"tableName\": \"customer\",\"columns\": [" +
                "{\"title\": \"MasterId\",\"type\": \"int4\"}," +
                "{\"title\": \"FirstName\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"LastName\",\"type\": \"VARCHAR(20)\"}," +
                "{\"title\": \"Company\",\"type\": \"VARCHAR(80)\"}," +
                "{\"title\": \"Address\",\"type\": \"VARCHAR(70)\"}," +
                "{\"title\": \"City\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"Country\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"PostalCode\",\"type\": \"VARCHAR(10)\"}," +
                "{\"title\": \"Phone\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Fax\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Email\",\"type\": \"VARCHAR(60)\"}," +
                "{\"title\": \"SupportRepId\",\"type\": \"int4\"}" +
                "]}]}";
        checkGetReportWithParams(201, "1", getReport);
    }

    private void checkGetReportNotExistsId() {
        System.out.printf(GIST, "not exists id");
        String getReport = "{\"reportId\":999,\"tableAmount\": 1,\"tables\": [" +
                "{\"tableName\": \"customer\",\"columns\": [" +
                "{\"title\": \"MasterId\",\"type\": \"int4\"}," +
                "{\"title\": \"FirstName\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"LastName\",\"type\": \"VARCHAR(20)\"}," +
                "{\"title\": \"Company\",\"type\": \"VARCHAR(80)\"}," +
                "{\"title\": \"Address\",\"type\": \"VARCHAR(70)\"}," +
                "{\"title\": \"City\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"Country\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"PostalCode\",\"type\": \"VARCHAR(10)\"}," +
                "{\"title\": \"Phone\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Fax\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Email\",\"type\": \"VARCHAR(60)\"}," +
                "{\"title\": \"SupportRepId\",\"type\": \"int4\"}" +
                "]}]}";
        checkGetReportWithParams(406, "999", getReport);
    }

    private void checkGetReportInvalidId() {
        System.out.printf(GIST, "invalid id");
        String getReport = "{\"reportId\":\"abc\",\"tableAmount\": 1,\"tables\": [" +
                "{\"tableName\": \"customer\",\"columns\": [" +
                "{\"title\": \"MasterId\",\"type\": \"int4\"}," +
                "{\"title\": \"FirstName\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"LastName\",\"type\": \"VARCHAR(20)\"}," +
                "{\"title\": \"Company\",\"type\": \"VARCHAR(80)\"}," +
                "{\"title\": \"Address\",\"type\": \"VARCHAR(70)\"}," +
                "{\"title\": \"City\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"Country\",\"type\": \"VARCHAR(40)\"}," +
                "{\"title\": \"PostalCode\",\"type\": \"VARCHAR(10)\"}," +
                "{\"title\": \"Phone\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Fax\",\"type\": \"VARCHAR(24)\"}," +
                "{\"title\": \"Email\",\"type\": \"VARCHAR(60)\"}," +
                "{\"title\": \"SupportRepId\",\"type\": \"int4\"}" +
                "]}]}";
        checkGetReportWithParams(406, "abc", getReport);
    }

    private void checkGetReportWithParams(int code, String reportId, String getReport) {
        System.out.printf(COUNTER, counter);
        try {
            final Integer resultId = counter;
            postGetReportResult(resultId, code, Urls.REPORT + Urls.GET_REPORT_BY_ID_RESULT, getReport);

            String url = String.format(PARAM_URL_TEMPL,
                    Urls.REPORT + Urls.GET_REPORT_BY_ID,
                    RESULT_ID, resultId).replace("{id}", reportId);
            Request request = RequestBuilder.getRequestBuilder(url);
            Response response = client.newCall(request).execute();
            System.out.printf(CHECK_CODE_BODY, response.code(), bodyToString(response), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }

}
