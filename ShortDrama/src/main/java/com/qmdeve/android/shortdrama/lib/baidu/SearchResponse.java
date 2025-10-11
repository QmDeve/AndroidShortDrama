package com.qmdeve.android.shortdrama.lib.baidu;

import java.util.List;

public class SearchResponse {
    public int code;
    public String msg;
    public SearchData data;
    public String time;

    public static class SearchData {
        public List<DramaItem> itemList;

        public static class DramaItem {
            public String nid;
            public String title;
            public int collNum;
            public String img;
        }
    }
}
