package com.qmdeve.android.shortdrama.lib.baidu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoDetailResponse {
    public int code;
    public String msg;
    public VideoData data;
    public String time;

    public static class VideoData {
        @SerializedName("351")
        public VideoInfo videoInfo;

        public static class VideoInfo {
            public String title;
            public String id;
            public VideoDetail videoInfo;
            public AuthorInfo author;
            public Statistics favourite;
            public Statistics praise;
            public Statistics comment;
            public ShareInfo shareInfo;

            public static class VideoDetail {
                public int duration;
                public String posterImage;
                public List<QualityInfo> clarityUrl;

                public static class QualityInfo {
                    public String key;
                    public String title;
                    public String vodVideoHW;
                    public int width;
                    public int height;
                    public String url;
                    public String videoSize;
                    public int videoBps;
                }
            }

            public static class AuthorInfo {
                public String name;
                public FollowInfo followInfo;
                public String intro;

                public static class FollowInfo {
                    public String fansNum;
                }
            }

            public static class Statistics {
                public int count;
                public int view;
            }

            public static class ShareInfo {
                public int shareNum;
            }
        }
    }
}