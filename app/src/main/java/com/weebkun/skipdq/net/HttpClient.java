package com.weebkun.skipdq.net;

import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.function.Consumer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {
    private static final OkHttpClient client = new OkHttpClient.Builder().build();
    private static final String root = "https://skipdq.herokuapp.com";
    private static final String content_type = "application/json; charset=utf-8";
    private static final Moshi moshi = new Moshi.Builder().build();

    public static  <T> void get(String endPoint, Class<T> type, Consumer<T> callback) {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url(root + endPoint)
                    .build();
            try(Response response = client.newCall(request).execute()) {
                callback.accept(moshi.adapter(type).fromJson(response.body().source()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void post(String endPoint, String json) {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url(root + endPoint)
                    .post(RequestBody.create(json, MediaType.get(content_type)))
                    .build();
            try(Response response = client.newCall(request).execute()) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void post(String endPoint, String json, Consumer<Response> callback) {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url(root + endPoint)
                    .post(RequestBody.create(json, MediaType.get(content_type)))
                    .build();
            try(Response response = client.newCall(request).execute()) {
                callback.accept(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void put(String endPoint, String json) {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url(root + endPoint)
                    .put(RequestBody.create(json, MediaType.get(content_type)))
                    .build();
            try(Response response = client.newCall(request).execute()) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void patch(String endPoint, String json) {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url(root + endPoint)
                    .patch(RequestBody.create(json, MediaType.get(content_type)))
                    .build();
            try(Response response = client.newCall(request).execute()) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void delete(String endPoint) {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url(root + endPoint)
                    .delete()
                    .build();
            try(Response response = client.newCall(request).execute()) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void delete(String endPoint, String json) {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url(root + endPoint)
                    .delete(RequestBody.create(json, MediaType.get(content_type)))
                    .build();
            try(Response response = client.newCall(request).execute()) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
