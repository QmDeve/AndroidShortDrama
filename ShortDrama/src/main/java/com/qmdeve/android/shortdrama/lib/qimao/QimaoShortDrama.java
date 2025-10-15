package com.qmdeve.android.shortdrama.lib.qimao;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QimaoShortDrama {
    private static final String SIGN_KEY = "d3dGiJc651gSQ8w1";
    private final OkHttpClient client = new OkHttpClient();


    /**
     * 七猫短剧 搜索短剧
     *
     * @param name     短剧名称
     * @param page     页码
     * @param listener 回调监听器
     */
    public void searchDrama(String name, String page, OnResultListener listener) {
        try {
            Map<String, String> headers = createDefaultHeaders();
            Map<String, String> params = new HashMap<>();
            params.put("playlet_privacy", "1");
            params.put("tab", "0");
            params.put("page", page);
            params.put("read_preference", "0");
            params.put("track_id", "481EC7F8-AEA4-4DD9-937E-1D10B9B5205A1756152280");
            params.put("wd", name);
            String headerSign = generateSign(headers, SIGN_KEY);
            String paramSign = generateSign(params, SIGN_KEY);
            headers.put("sign", headerSign);
            params.put("sign", paramSign);
            String url = "https://api-store.qmplaylet.com/api/v1/playlet/search?" + buildParams(params);
            sendRequest(url, headers, null, listener);
        } catch (Exception e) {
            if (listener != null) {
                listener.onFailure(e);
            }
        }
    }


    /**
     * 七猫短剧 推荐短剧
     *
     * @param page     页码
     * @param listener 回调监听器
     */
    public void recommendDrama(String page, OnResultListener listener) {
        try {
            Map<String, String> headers = createDefaultHeaders();
            Map<String, String> params = new HashMap<>();
            params.put("page", page);
            params.put("playlet_privacy", "1");
            params.put("read_preference", "0");
            params.put("tag_id", "0");
            String headerSign = generateSign(headers, SIGN_KEY);
            String paramSign = generateSign(params, SIGN_KEY);
            headers.put("sign", headerSign);
            params.put("sign", paramSign);
            String url = "https://api-gw.wtzw.com/playlet/api/v2/list-by-tagId";
            sendRequest(url, headers, params, listener);
        } catch (Exception e) {
            if (listener != null) {
                listener.onFailure(e);
            }
        }
    }


    /**
     * 七猫短剧 获取短剧详情
     *
     * @param id       短剧id
     * @param listener 回调监听器
     */
    public void getDramaInfo(String id, OnResultListener listener) {
        try {
            Map<String, String> headers = createDefaultHeaders();
            Map<String, String> params = new HashMap<>();
            params.put("is_material", "0");
            params.put("playlet_id", id);
            String headerSign = generateSign(headers, SIGN_KEY);
            String paramSign = generateSign(params, SIGN_KEY);
            headers.put("sign", headerSign);
            params.put("sign", paramSign);
            String url = "https://api-read.qmplaylet.com/player/api/v1/playlet/info?" + buildParams(params);
            sendRequest(url, headers, null, listener);
        } catch (Exception e) {
            if (listener != null) {
                listener.onFailure(e);
            }
        }
    }

    private Map<String, String> createDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("app-version", "1901020");
        headers.put("platform", "ios");
        headers.put("reg", "");
        headers.put("AUTHORIZATION", "");
        headers.put("application-id", "com.moon.read");
        headers.put("net-env", "1");
        headers.put("channel", "vi-appstore_wm");
        headers.put("qm-params", "cLG5Ozo7paHWH-owOyn2H5w5pyRlmqN2tq2-HTZ5FS0wkekUcI28qzxjuE2xNRsHmesYhC2LfRGWcfQaFRxLuyQEf0ngFRRjul2URf2FpqZlF5QGFzYFhSRhf0YLcRG-kqFEpypNqTpFCaJs3S2T4fuvOhpFpeMJH5w5mqkz43HWH5HjHz2Qpq-5A5HlNe4MNMFeF5MrNT0nthk0Nh9QFhOrgaMwNe4egIRsgMpoFeK5taGQBlk2BaHWHz2FmyJ7ph0UtIH5taGLpqpLpCNZtCkYOyf5A5HwH5w5OlReOl2DB5U1paHWHT0ENh4nNhHnNhOLNI07gaHjHSNDuCGTpCR1paHWH5gMAIooFeuyAaUskf0Mthk0kI-QAhgEk3MnkI0wFT2aNhHwNf05taGeuyoMmqN6OlNDOzf5A5HntT95taGecCgQuzRLHTZ5ghK7N3HjHSk-HTZ5gaHjHSkLuCNMpqFQmqF5A5H5taGMOSReuyR-tq2-pz05A5H5taGUuq2-HTZ5NTOlNIu0gMHQAI4ng3MMkIfwtf0EAI9QgIOlge9UFhNykfgrH5w5uln5tq2Qpq-5A5HlNe4MNMFeF5MrNT0nthk0Nh9QFhOrgaMwNe4egIRsgMpoFeK5taGEByHQuq2-HTZ5HeFrgfRINM4rtfooFhFQN0k0A3MYgeuotho0ghsaAfHUgT9UF3GJ");
        return headers;
    }

    private String generateSign(Map<String, String> data, String signKey) {
        List<String> keys = new ArrayList<>(data.keySet());
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(data.get(key));
        }
        sb.append(signKey);

        return md5(sb.toString());
    }

    private String buildParams(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return sb.toString();
    }

    private void sendRequest(String url, Map<String, String> headers, Map<String, String> params, OnResultListener listener) {
        Request.Builder builder = new Request.Builder().url(url);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        if (params != null && !params.isEmpty()) {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
            builder.post(formBodyBuilder.build());
        }

        Request request = builder.build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (listener != null) {
                    listener.onFailure(e);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        if (url.contains("search")) {
                            responseBody = handleSearchResult(responseBody);
                        }
                        if (listener != null) {
                            listener.onSuccess(responseBody);
                        }
                    } else {
                        if (listener != null) {
                            listener.onFailure(new Exception("请求失败，状态码: " + response.code()));
                        }
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onFailure(e);
                    }
                }
            }
        });
    }

    private String handleSearchResult(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.has("data")) {
            JSONObject data = jsonObject.getJSONObject("data");
            if (data.has("list")) {
                JSONArray list = data.getJSONArray("list");
                for (int i = 0; i < list.length(); i++) {
                    JSONObject item = list.getJSONObject(i);
                    if (item.has("title")) {
                        String title = item.getString("title");
                        title = title.replaceAll("<[^>]*>", "");
                        item.put("title", title);
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public interface OnResultListener {
        void onSuccess(String result);

        void onFailure(Exception e);
    }
}