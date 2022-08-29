package ru.vk.competition.minchecker.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpDelete {

    public static Response run(String url, OkHttpClient client) throws IOException {
        Request request = new Request.Builder().url(url).delete().build();

        try (Response response = client.newCall(request).execute()) {
            return response;
        }
    }
}
