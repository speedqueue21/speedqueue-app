package com.weebkun.skipdq.net;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.squareup.moshi.Moshi;
import com.weebkun.skipdq.util.JWTDecoder;
import com.weebkun.skipdq.util.JWTReader;
import com.weebkun.skipdq.util.JWTWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.function.Consumer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {
    private static OkHttpClient client = new OkHttpClient.Builder().build();
    private static final String root = "https://skipdq.herokuapp.com";
    private static final String content_type = "application/json; charset=utf-8";
    private static final Moshi moshi = new Moshi.Builder().build();

    public static String getRoot() {
        return root;
    }

    public static void authorise(String token) {
        client = client.newBuilder().addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                // replace token with refreshed one if have
                .header("authorization", "Bearer " + token).build())).build();
    }

    public static void refresh(Context context, String id, String refresh, Runnable callback) {
        post("/refresh", String.format("{" +
                "\"id\": \"%s\"," +
                "\"refresh\": \"%s\"" +
                "}", id, refresh), response -> {
            try {
                String body = response.body().string();
                System.out.println(body);
                TokenResponse tokenResponse = moshi.adapter(TokenResponse.class).fromJson(body);
                //set token to header
                authorise(tokenResponse.access);
                // overwrite files
                JWTWriter.writeTokenToFile(context, body, tokenResponse);
                // done
                callback.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static <T> void get(String endPoint, Class<T> type, Consumer<T> callback) {
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

    public static void get(String endPoint, Consumer<Response> callback) {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url(root + endPoint)
                    .build();
            try(Response response = client.newCall(request).execute()) {
                callback.accept(response);
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
