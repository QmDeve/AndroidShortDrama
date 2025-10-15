package com.qmdeve.android.shortdrama.lib.hema;

import android.util.Base64;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.gson.factory.GsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HemaShortDrama {
    private static final String ENCRYPTION_KEY = "dzkjgfyxgshylgzm";
    private static final String ENCRYPTION_IV = "apiupdownedcrypt";
    private static volatile HemaShortDrama instance;
    private final OkHttpClient client;
    private final Gson gson;
    private final ExecutorService executorService;

    private HemaShortDrama() {
        client = new OkHttpClient();
        gson = new Gson();
        executorService = Executors.newFixedThreadPool(3);
    }

    public static HemaShortDrama getInstance() {
        if (instance == null) {
            synchronized (HemaShortDrama.class) {
                if (instance == null) {
                    instance = new HemaShortDrama();
                }
            }
        }
        return instance;
    }

    public interface OnResultListener<T> {
        void onSuccess(T result);
        void onFailure(String errorMsg);
    }


    /**
     * 河马短剧 短剧搜索
     * @param name 短剧
     * @param listener 回调监听器
     */
    public void searchListDrama(String name, OnResultListener<String> listener) {
        executorService.execute(() -> getSearchListDrama(name, listener));
    }


    /**
     * 河马短剧 获取播放链接
     * @param bookId 短剧ID
     * @param chapterId 剧集ID
     * @param chapterIds 剧集ID列表
     * @param listener 回调监听器
     */
    public void playListDrama(String bookId, String chapterId, ArrayList<String> chapterIds, OnResultListener<String> listener) {
        executorService.execute(() -> getPlayListDrama(bookId, chapterId, chapterIds, listener));
    }


    /**
     * 河马短剧 获取剧集列表
     * @param id 短剧ID
     * @param listener 回调监听器
     */
    public void episodeListDrama(String id, OnResultListener<String> listener) {
        executorService.execute(() -> getEpisodeListDrama(id, listener));
    }


    /**
     * 河马短剧 获取分类推荐列表
     * @param id 短剧ID
     * @param name 分类名称
     * @param page 页码
     * @param listener 回调监听器
     */
    public void typeListDrama(String id, String name, String page, OnResultListener<String> listener) {
        executorService.execute(() -> getTypeListDrama(id, name, page, listener));
    }


    /**
     * 河马短剧 获取分类标签(通过此方法获取所有分类的id)
     * @param listener 回调监听器
     */
    public void typeDrama(OnResultListener<String> listener) {
        executorService.execute(() -> getTypeDrama(listener));
    }


    private void getSearchListDrama(String name, OnResultListener<String> listener) {
        try {
            Map<String, Object> headers = createDefaultHeaders();
            Map<String, Object> params = new HashMap<>();
            params.put("keyword", name);
            params.put("page", 1);
            params.put("size", 15);

            byte[] headersBytes = AesCbcEncryption.encrypt(
                    gson.toJson(headers),
                    ENCRYPTION_KEY,
                    ENCRYPTION_IV
            );

            String headersEncrypted = Base64.encodeToString(headersBytes, Base64.DEFAULT)
                    .replaceAll("[\\p{Cntrl}]", "");

            byte[] paramsBytes = AesCbcEncryption.encrypt(
                    gson.toJson(params),
                    ENCRYPTION_KEY,
                    ENCRYPTION_IV
            );
            String paramsEncrypted = Base64.encodeToString(paramsBytes, Base64.DEFAULT)
                    .replaceAll("[\\p{Cntrl}]", "");

            String url = "https://freevideo.zqqds.cn/free-video-portal/portal/1803";

            RequestBody body = RequestBody.create(
                    paramsEncrypted,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("alg", "HG45LKBS")
                    .addHeader("datas", headersEncrypted)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    if (listener != null) {
                        listener.onFailure("短剧搜索请求失败: " + e.getMessage());
                    }
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    try (response) {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            if (listener != null) {
                                try {
                                    HashMap<String, Object> map = GsonFactory.getSingletonGson().fromJson(responseData, new TypeToken<HashMap<String, Object>>() {}.getType());
                                    String data = AesCbcEncryption.decrypt(Base64.decode(String.valueOf(map.get("data")), Base64.DEFAULT), "dzkjgfyxgshylgzm", "apiupdownedcrypt");

                                    listener.onSuccess(data);
                                } catch (Exception e) {
                                    listener.onFailure(e.getMessage());
                                }
                            }
                        } else {
                            if (listener != null) {
                                listener.onFailure("短剧搜索请求失败，状态码: " + response.code());
                            }
                        }
                    } catch (Exception e) {
                        if (listener != null) {
                            listener.onFailure("处理短剧搜索响应失败: " + e.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFailure("短剧搜索失败: " + e.getMessage());
            }
        }
    }

    private void getPlayListDrama(String bookId, String chapterId, ArrayList<String> chapterIds, OnResultListener<String> listener) {
        try {
            Map<String, Object> headers = createDefaultHeaders();
            Map<String, Object> params = new HashMap<>();
            params.put("bookId", bookId);
            params.put("chapterIds", chapterIds);
            params.put("unClockType", "load");
            params.put("chapterId", chapterId);

            byte[] headersBytes = AesCbcEncryption.encrypt(
                    gson.toJson(headers),
                    ENCRYPTION_KEY,
                    ENCRYPTION_IV
            );

            String headersEncrypted = Base64.encodeToString(headersBytes, Base64.DEFAULT)
                    .replaceAll("[\\p{Cntrl}]", "");

            byte[] paramsBytes = AesCbcEncryption.encrypt(
                    gson.toJson(params),
                    ENCRYPTION_KEY,
                    ENCRYPTION_IV
            );
            String paramsEncrypted = Base64.encodeToString(paramsBytes, Base64.DEFAULT)
                    .replaceAll("[\\p{Cntrl}]", "");

            String url = "https://freevideo.zqqds.cn/free-video-portal/portal/1139";

            RequestBody body = RequestBody.create(
                    paramsEncrypted,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("alg", "HG45LKBS")
                    .addHeader("datas", headersEncrypted)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    if (listener != null) {
                        listener.onFailure("获取播放链接请求失败: " + e.getMessage());
                    }
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    try (response) {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            if (listener != null) {
                                try {
                                    HashMap<String, Object> map = GsonFactory.getSingletonGson().fromJson(responseData, new TypeToken<HashMap<String, Object>>() {}.getType());
                                    String data = AesCbcEncryption.decrypt(Base64.decode(String.valueOf(map.get("data")), Base64.DEFAULT), "dzkjgfyxgshylgzm", "apiupdownedcrypt");

                                    listener.onSuccess(data);
                                } catch (Exception e) {
                                    listener.onFailure(e.getMessage());
                                }
                            }
                        } else {
                            if (listener != null) {
                                listener.onFailure("获取播放链接请求失败，状态码: " + response.code());
                            }
                        }
                    } catch (Exception e) {
                        if (listener != null) {
                            listener.onFailure("处理播放链接响应失败: " + e.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFailure("获取播放链接失败: " + e.getMessage());
            }
        }
    }

    private void getEpisodeListDrama(String id, OnResultListener<String> listener) {
        try {
            Map<String, Object> headers = createDefaultHeaders();
            Map<String, Object> params = new HashMap<>();
            params.put("bookId", id);

            byte[] headersBytes = AesCbcEncryption.encrypt(
                    gson.toJson(headers),
                    ENCRYPTION_KEY,
                    ENCRYPTION_IV
            );

            String headersEncrypted = Base64.encodeToString(headersBytes, Base64.DEFAULT)
                    .replaceAll("[\\p{Cntrl}]", "");

            byte[] paramsBytes = AesCbcEncryption.encrypt(
                    gson.toJson(params),
                    ENCRYPTION_KEY,
                    ENCRYPTION_IV
            );

            String paramsEncrypted = Base64.encodeToString(paramsBytes, Base64.DEFAULT)
                    .replaceAll("[\\p{Cntrl}]", "");

            String url = "https://freevideo.zqqds.cn/free-video-portal/portal/1131";

            RequestBody body = RequestBody.create(
                    paramsEncrypted,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("alg", "HG45LKBS")
                    .addHeader("datas", headersEncrypted)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    if (listener != null) {
                        listener.onFailure("获取剧集列表请求失败: " + e.getMessage());
                    }
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    try (response) {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            if (listener != null) {
                                try {
                                    HashMap<String, Object> map = GsonFactory.getSingletonGson().fromJson(responseData, new TypeToken<HashMap<String, Object>>() {}.getType());
                                    String data = AesCbcEncryption.decrypt(Base64.decode(String.valueOf(map.get("data")), Base64.DEFAULT), "dzkjgfyxgshylgzm", "apiupdownedcrypt");

                                    listener.onSuccess(data);
                                } catch (Exception e) {
                                    listener.onFailure(e.getMessage());
                                }
                            }
                        } else {
                            if (listener != null) {
                                listener.onFailure("获取剧集列表请求失败，状态码: " + response.code());
                            }
                        }
                    } catch (Exception e) {
                        if (listener != null) {
                            listener.onFailure("处理剧集列表响应失败: " + e.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFailure("获取剧集列表失败: " + e.getMessage());
            }
        }
    }

    private void getTypeListDrama(String id, String name, String page, OnResultListener<String> listener) {
        try {
            Map<String, Object> headers = createDefaultHeaders();
            Map<String, Object> params = new HashMap<>();
            params.put("recSwitch", false);
            params.put("storePageId", 10031);
            params.put("channelGroupId", "67");
            params.put("channelId", Integer.valueOf(id));
            params.put("channelName", name);
            params.put("lastColumnStyle", 3);
            params.put("fromColumnId", "1");
            params.put("pageFlag", page);
            params.put("theaterSubscriptSwitch", true);

            byte[] headersBytes = AesCbcEncryption.encrypt(
                    gson.toJson(headers),
                    ENCRYPTION_KEY,
                    ENCRYPTION_IV
            );

            String headersEncrypted = Base64.encodeToString(headersBytes, Base64.DEFAULT)
                    .replaceAll("[\\p{Cntrl}]", "");

            byte[] paramsBytes = AesCbcEncryption.encrypt(
                    gson.toJson(params),
                    ENCRYPTION_KEY,
                    ENCRYPTION_IV
            );

            String paramsEncrypted = Base64.encodeToString(paramsBytes, Base64.DEFAULT)
                    .replaceAll("[\\p{Cntrl}]", "");

            String url = "https://freevideo.zqqds.cn/free-video-portal/portal/1125";

            RequestBody body = RequestBody.create(
                    paramsEncrypted,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("alg", "HG45LKBS")
                    .addHeader("datas", headersEncrypted)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    if (listener != null) {
                        listener.onFailure("获取分类推荐列表请求失败: " + e.getMessage());
                    }
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    try (response) {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            if (listener != null) {
                                try {
                                    HashMap<String, Object> map = GsonFactory.getSingletonGson().fromJson(responseData, new TypeToken<HashMap<String, Object>>() {}.getType());
                                    String data = AesCbcEncryption.decrypt(Base64.decode(String.valueOf(map.get("data")), Base64.DEFAULT), "dzkjgfyxgshylgzm", "apiupdownedcrypt");

                                    listener.onSuccess(data);
                                } catch (Exception e) {
                                    listener.onFailure(e.getMessage());
                                }
                            }
                        } else {
                            if (listener != null) {
                                listener.onFailure("获取分类推荐列表请求失败，状态码: " + response.code());
                            }
                        }
                    } catch (Exception e) {
                        if (listener != null) {
                            listener.onFailure("处理分类推荐列表响应失败: " + e.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            if (listener != null) {
                listener.onFailure("获取分类推荐列表失败: " + e.getMessage());
            }
        }
    }

    private void getTypeDrama(OnResultListener<String> listener) {
        try {
            Map<String, Object> headers = createDefaultHeaders();
            Map<String, Object> params = new HashMap<>();
            params.put("recSwitch", false);
            params.put("pageFlag", "");
            params.put("theaterSubscriptSwitch", true);

            byte[] headersBytes = AesCbcEncryption.encrypt(
                    gson.toJson(headers),
                    ENCRYPTION_KEY,
                    ENCRYPTION_IV
            );

            String headersEncrypted = Base64.encodeToString(headersBytes, Base64.DEFAULT)
                    .replaceAll("[\\p{Cntrl}]", "");

            byte[] paramsBytes = AesCbcEncryption.encrypt(
                    gson.toJson(params),
                    ENCRYPTION_KEY,
                    ENCRYPTION_IV
            );
            String paramsEncrypted = Base64.encodeToString(paramsBytes, Base64.DEFAULT)
                    .replaceAll("[\\p{Cntrl}]", "");

            String url = "https://freevideo.zqqds.cn/free-video-portal/portal/1125";

            RequestBody body = RequestBody.create(
                    paramsEncrypted,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("alg", "HG45LKBS")
                    .addHeader("datas", headersEncrypted)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    if (listener != null) {
                        listener.onFailure("获取分类标签失败: " + e.getMessage());
                    }
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    try (response) {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            if (listener != null) {
                                try {
                                    HashMap<String, Object> map = GsonFactory.getSingletonGson().fromJson(responseData, new TypeToken<HashMap<String, Object>>() {}.getType());
                                    String data = AesCbcEncryption.decrypt(Base64.decode(String.valueOf(map.get("data")), Base64.DEFAULT), "dzkjgfyxgshylgzm", "apiupdownedcrypt");

                                    listener.onSuccess(data);
                                } catch (Exception e) {
                                    listener.onFailure(e.getMessage());
                                }
                            }
                        } else {
                            if (listener != null) {
                                listener.onFailure("获取分类标签请求失败，状态码: " + response.code());
                            }
                        }
                    } catch (Exception e) {
                        if (listener != null) {
                            listener.onFailure("处理分类标签响应失败: " + e.getMessage());
                        }
                    }
                }
            });

        } catch (Exception e) {
            if (listener != null) {
                listener.onFailure("获取分类标签失败: " + e.getMessage());
            }
        }
    }

    private Map<String, Object> createDefaultHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("version", "2.11.3");
        headers.put("pname", "com.dz.hmjc");
        headers.put("channelCode", "HMJC1000002");
        headers.put("utdidTmp", "A20250906220521536MPpGED");
        headers.put("token", "");
        headers.put("utdid", "");
        headers.put("os", "android");
        headers.put("osv", 28);
        headers.put("brand", "Meizu");
        headers.put("model", "M973Q");
        headers.put("manu", "Meizu");
        headers.put("userId", "2658323215");
        headers.put("launch", "packagename");
        headers.put("mchid", "HMJC1000002");
        headers.put("nchid", "HMJC1000002");
        headers.put("session1", "26f58e87-4871-435a-b0e0-0eadd0eca10e");
        headers.put("session2", "26f58e87-4871-435a-b0e0-0eadd0eca10e");
        headers.put("startScene", "packagename");
        headers.put("recSwitch", false);
        headers.put("installTime", System.currentTimeMillis());
        headers.put("p", 40);
        return headers;
    }
}