package com.weebkun.skipdq.net;

import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// todo client
public class HttpClient {
    private static final OkHttpClient client = new OkHttpClient.Builder().build();
    private static final String root = "https://";
    private static final String content_type = "application/json; charset=utf-8";
    private static final Moshi moshi = new Moshi.Builder().build();

    public static  <T> T get(String endPoint, Class<T> type) {
        Request request = new Request.Builder()
                .url(root + endPoint)
                .build();
        T result;
        try(Response response = client.newCall(request).execute()) {

            result = moshi.adapter(type).fromJson(response.body().source());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            return null;
        }
        return result;
    }

    public static void post(String endPoint, String json) {
        Request request = new Request.Builder()
                .url(root + endPoint)
                .post(RequestBody.create(json, MediaType.get(content_type)))
                .build();
        try(Response response = client.newCall(request).execute()) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void put(String endPoint, String json) {
        Request request = new Request.Builder()
                .url(root + endPoint)
                .put(RequestBody.create(json, MediaType.get(content_type)))
                .build();
        try(Response response = client.newCall(request).execute()) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void patch(String endPoint, String json) {
        Request request = new Request.Builder()
                .url(root + endPoint)
                .patch(RequestBody.create(json, MediaType.get(content_type)))
                .build();
        try(Response response = client.newCall(request).execute()) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delete(String endPoint) {
        Request request = new Request.Builder()
                .url(root + endPoint)
                .delete()
                .build();
        try(Response response = client.newCall(request).execute()) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delete(String endPoint, String json) {
        Request request = new Request.Builder()
                .url(root + endPoint)
                .delete(RequestBody.create(json, MediaType.get(content_type)))
                .build();
        try(Response response = client.newCall(request).execute()) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
