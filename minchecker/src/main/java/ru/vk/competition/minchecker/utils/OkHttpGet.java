package ru.vk.competition.minchecker.utils;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpGet {

    public static Response run(String url, OkHttpClient client) throws IOException {
        Request request = new Request.Builder().url(url).get().build();

        try (Response response = client.newCall(request).execute()) {
            return response;
        }
    }

    public static String getHttp(String url) {
        try {
            String result;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            result = response.body().string();
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
