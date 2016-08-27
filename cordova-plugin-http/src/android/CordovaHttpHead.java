/**
 * A HTTP plugin for Cordova / Phonegap
 */
package com.gentle.exview.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HostnameVerifier;

import javax.net.ssl.SSLHandshakeException;

import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.gentle.exview.plugins.HttpRequest;
import com.gentle.exview.plugins.HttpRequest.HttpRequestException;
 
public class CordovaHttpHead extends CordovaHttp implements Runnable {
    public CordovaHttpHead(String urlString, Map<?, ?> params, Map<String, String> headers,String charset,Boolean autoencode, CallbackContext callbackContext) {
        super(urlString, params, headers,charset,autoencode, callbackContext);
    }
    
    @Override
    public void run() {
        try {
            HttpRequest request = HttpRequest.head(this.getUrlString(), this.getParams(), this. getautoencode());
            this.setupSecurity(request);
            request.acceptCharset(this.getcharset());
            request.headers(this.getHeaders());
            int code = request.code();
            JSONObject response = new JSONObject();
            this.addResponseHeaders(request, response);
            response.put("status", code);
            if (code >= 200 && code < 300) {
                // no 'body' to return for HEAD request
                this.getCallbackContext().success(response);
            } else {
                String body = request.body(this.getcharset());
                response.put("error", body);
                this.getCallbackContext().error(response);
            }
        } catch (JSONException e) {
            this.respondWithError("There was an error generating the response");
        } catch (HttpRequestException e) {
            if (e.getCause() instanceof UnknownHostException) {
                this.respondWithError(0, "The host could not be resolved");
            } else if (e.getCause() instanceof SSLHandshakeException) {
                this.respondWithError("SSL handshake failed");
            } else {
                this.respondWithError("There was an error with the request");
            }
        }
    }
}