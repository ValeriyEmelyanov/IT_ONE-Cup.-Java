package ru.vk.competition.minchecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.vk.competition.minchecker.dto.results.SingleQueryResult;
import ru.vk.competition.minchecker.utils.JsonBuilder;
import ru.vk.competition.minchecker.utils.RequestBuilder;
import ru.vk.competition.minchecker.utils.Urls;

@Service
@Slf4j
@RequiredArgsConstructor
public class StartService {

    OkHttpClient client = new OkHttpClient();

    private final String RESULT_ID = "resultId";

    private Integer counter = 0;

    public void onStartMission() {

        System.out.println("------ " + counter + " ------");
        try {
            /*
            Это правильный запрос, так как код = 400
             */
            SingleQueryResult singleQueryResult = new SingleQueryResult(1, 400);
            String singleQueryResultJson = JsonBuilder.addSingleQueryResultJsonBuilder(singleQueryResult.resultId, singleQueryResult.code);
            RequestBody addSingleQueryResultRequestBody = RequestBody.create(MediaType.parse("application/json"), singleQueryResultJson);
            Request addNewSingleQueryResult = RequestBuilder.postRequestBuilder(Urls.SINGLE_QUERY + Urls.ADD_NEW_QUERY_RESULT, addSingleQueryResultRequestBody);
            System.out.println(addNewSingleQueryResult.url());
            Call call = client.newCall(addNewSingleQueryResult);
            Response response = call.execute();
            System.out.println(response.code());

            /*
            Отправляем модель SingleQuery с параметром
             */
            HttpUrl url = HttpUrl.parse(RequestBuilder.API_ROOT + Urls.SINGLE_QUERY + Urls.ADD_SINGLE_QUERY).newBuilder()
                    .addQueryParameter(RESULT_ID, singleQueryResult.resultId.toString())
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call1 = client.newCall(request);
            call1.execute();
            System.out.println("---- END " + "(" + counter + ")\n");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter++;
    }
}
