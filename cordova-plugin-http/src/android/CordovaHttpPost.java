/**
 * A HTTP plugin for Cordova / Phonegap
 */
package com.gentle.exview.plugins;

import java.net.UnknownHostException;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLHandshakeException;

import android.util.Log;

import com.gentle.exview.plugins.HttpRequest;
import com.gentle.exview.plugins.HttpRequest.HttpRequestException;
 
public class CordovaHttpPost extends CordovaHttp implements Runnable {
    public CordovaHttpPost(String urlString, Map<?, ?> params, Map<String, String> headers,String charset, CallbackContext callbackContext) {
        super(urlString, params, headers,charset,true, callbackContext);
    }
    
    @Override
    public void run() {
        try {
            HttpRequest request = HttpRequest.post(this.getUrlString());
            this.setupSecurity(request);
            request.acceptCharset(this.getcharset());
            request.headers(this.getHeaders());
            request.form(this.getParams(),this.getcharset());
            int code = request.code();
            String body = request.body(this.getcharset());
            JSONObject response = new JSONObject();
            this.addResponseHeaders(request, response);
            response.put("status", code);
            if (code >= 200 && code < 300) {
                response.put("data", body);
                this.getCallbackContext().success(response);
            } else {
                response.put("error", body);
                this.getCallbackContext().error(response);
            }
        } catch (JSONException e) {
            this.respondWithError("There was an error generating the response");
        }  catch (HttpRequestException e) {
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
