package com.weebkun.skipdq.util;

import android.app.Activity;
import android.content.Context;

import com.weebkun.skipdq.net.TokenResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class JWTWriter {

    public static void writeTokenToFile(Context context, String response, TokenResponse tokenResponse) {
        try(OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput("token.json", Context.MODE_PRIVATE))) {
            writer.write(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput("payload.json", Context.MODE_PRIVATE))) {
            writer.write(JWTDecoder.decode(tokenResponse.access));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
