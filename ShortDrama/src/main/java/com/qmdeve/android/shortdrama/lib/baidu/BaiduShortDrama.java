package com.qmdeve.android.shortdrama.lib.baidu;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BaiduShortDrama {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public interface OnResultListener<T> {
        void onSuccess(T result);
        void onFailure(String errorMsg);
    }


    /**
     * 百度短剧 搜索短剧
     * @param name 短剧名
     * @param page 页码
     * @param listener 回调监听器
     */
    public void searchDrama(String name, int page, OnResultListener<SearchResponse> listener) {
        new SearchTask(listener).execute(name, String.valueOf(page));
    }


    /**
     * 百度短剧 获取集数列表
     * @param id 短剧ID
     * @param listener 回调监听器
     */
    public void getEpisodeList(String id, OnResultListener<EpisodeListResponse> listener) {
        new EpisodeListTask(listener).execute(id);
    }


    /**
     * 百度短剧 获取剧集详情
     * @param videoId 剧集视频ID
     * @param listener 回调监听器
     */
    public void getVideoDetail(String videoId, OnResultListener<VideoDetailResponse> listener) {
        new VideoDetailTask(listener).execute(videoId);
    }


    private class SearchTask extends AsyncTask<String, Void, String> {
        private OnResultListener<SearchResponse> listener;

        public SearchTask(OnResultListener<SearchResponse> listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String page = params[1];

            try {
                SearchParams searchParams = new SearchParams();
                searchParams.query = name;
                searchParams.page = Integer.parseInt(page);
                searchParams.attribute = new String[]{"title"};
                searchParams.fe_page_type = "search";

                SearchExtra extra = new SearchExtra();
                extra.tab_id = "216";
                extra.flow_tabid = "13";
                extra.shortplay_source = "feed";
                extra.from = "feed";
                extra.tab_type = "搜索";
                extra.sub_template = "playlet_search_result";
                searchParams.extra = extra;

                String jsonData = gson.toJson(searchParams);

                FormBody body = new FormBody.Builder()
                        .add("data", jsonData)
                        .build();

                Request request = new Request.Builder()
                        .url("https://mbd.baidu.com/feedapi/v1/videoserver/playlets/search")
                        .addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 18_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 SP-engine/3.49.0 baiduboxapp/15.23.0.11 (Baidu; P2 18.5) Talos/1.8.14")
                        .addHeader("Talos-Module-Name", "FeedSnVideoSearch")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return null;
                }
            } catch (IOException e) {
                listener.onFailure("搜索失败: ");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    Type type = new TypeToken<SearchResponse>() {}.getType();
                    SearchResponse response = gson.fromJson(result, type);
                    listener.onSuccess(response);
                } catch (Exception e) {
                    listener.onFailure("解析数据失败: " + e.getMessage());
                }
            } else {
                listener.onFailure("请求失败");
            }
        }
    }

    private class EpisodeListTask extends AsyncTask<String, Void, String> {
        private OnResultListener<EpisodeListResponse> listener;

        public EpisodeListTask(OnResultListener<EpisodeListResponse> listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            try {
                String url = "https://sv.baidu.com/v?vid=4409265997409778202&_format=json&collection_id=" + id;

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Cookie", "BAIDUID=")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return null;
                }
            } catch (IOException e) {
                listener.onFailure("获取集数列表失败: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    Type type = new TypeToken<EpisodeListResponse>() {}.getType();
                    EpisodeListResponse response = gson.fromJson(result, type);
                    listener.onSuccess(response);
                } catch (Exception e) {
                    listener.onFailure("解析数据失败: " + e.getMessage());
                }
            } else {
                listener.onFailure("请求失败");
            }
        }
    }

    private class VideoDetailTask extends AsyncTask<String, Void, String> {
        private OnResultListener<VideoDetailResponse> listener;

        public VideoDetailTask(OnResultListener<VideoDetailResponse> listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            String videoId = params[0];
            try {
                VideoDetailParams paramsObj = new VideoDetailParams();
                paramsObj.freq_control_map = new HashMap<>();
                paramsObj.video_play_score = 100;
                paramsObj.vid = videoId;
                paramsObj.resource_id = "5377";
                paramsObj.refresh_timestamp_ms = "";
                paramsObj.sc_tab = "recom";
                paramsObj.hitDiversion = "0";
                paramsObj.info_pd = "unknown";
                paramsObj.is_close_individual = "0";
                paramsObj.row = 1;
                paramsObj.bubblelist = "1";
                paramsObj.tpl = "flowcollection";
                paramsObj.from = "feed";

                Map<String, Object> playserverParams = new HashMap<>();
                playserverParams.put("srs", "1");
                playserverParams.put("ps", 100);
                playserverParams.put("ps2k", 100);
                paramsObj.playserver_params = playserverParams;

                paramsObj.need_hot_guide = new String[]{"0", "1"};
                paramsObj.page = "collection_video_landing";
                paramsObj.vid_type = "unknown";

                Map<String, Object> extRequest = new HashMap<>();
                extRequest.put("tab_id", "216");
                extRequest.put("is_microvideo", "1");
                extRequest.put("resourceType", "miniVideo");
                paramsObj.extRequest = extRequest;

                Map<String, Object> playerParams = new HashMap<>();
                playerParams.put("ps", 100);
                paramsObj.player_params = playerParams;

                paramsObj.pd = "feed";
                paramsObj.device_static_score = 1.3259999752044678;

                String jsonData = gson.toJson(paramsObj);

                FormBody body = new FormBody.Builder()
                        .add("data", jsonData)
                        .build();

                String urlParams = "action=feed&cmd=351&branchname=baiduboxapp&cfrom=1099a" +
                        "&cp_isbg=0&ds_lv=4&ds_stc=1.3260&dt=0&from=1099a&matrixstyle=0" +
                        "&mps=4279363467&mpv=1&network=1_0&osbranch=i0&osname=baiduboxapp" +
                        "&puid=_a2b8guV-fLlAqqqB&service=bdbox&st=0&ua=1179_2556_iphone_15.23.0.11_0" +
                        "&uid=DB6660D0DB8A23C23223D09BBB687D843BDD51733OBLHOTLECI" +
                        "&zid=vw1MwdGJnJRIEdRlgP31f3hu-p6bRo3v72-WYYNo2yXBv68qFVVK__ZKYqYxzFsCC_kUTgDPpxuE3-roIR4NlXg";

                String url = "https://mbd.baidu.com/searchbox?" + urlParams;

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 18_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 SP-engine/3.49.0 baiduboxapp/15.23.0.11 (Baidu; P2 18.5)")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return null;
                }
            } catch (IOException e) {
                listener.onFailure("获取视频详情失败: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    Type type = new TypeToken<VideoDetailResponse>() {}.getType();
                    VideoDetailResponse response = gson.fromJson(result, type);
                    listener.onSuccess(response);
                } catch (Exception e) {
                    listener.onFailure("解析数据失败: " + e.getMessage());
                }
            } else {
                listener.onFailure("请求失败");
            }
        }
    }
}