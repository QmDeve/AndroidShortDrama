package com.qmdeve.android.shortdrama.lib.baidu;

import java.util.List;

public class EpisodeListResponse {
    public int code;
    public String msg;
    public List<EpisodeItem> data;
    public int total;
    public String id;
    public String title;
    public String time;

    public static class EpisodeItem {
        public String video_id;
        public String title;
        public String cover;
    }
}