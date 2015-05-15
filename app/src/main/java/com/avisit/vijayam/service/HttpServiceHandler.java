package com.avisit.vijayam.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by User on 5/15/2015.
 */
public class HttpServiceHandler {
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    /**
     *
     * @param url url to make request
     * @param method http request method
     * @return String response
     */
    public String makeServiceCall(String url, int method) throws Exception{
        return this.makeServiceCall(url, method, null);
    }

    /**
     * Making service call
     * @param url url to make request
     * @param method http request method
     * @param params http request params
     * */
    public String makeServiceCall(String url, int method, List<NameValuePair> params) throws Exception{
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 5000);
            HttpEntity httpEntity;
            HttpResponse httpResponse = null;

            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }
            if(httpResponse!=null){
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
            }

        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        return response;
    }
}
