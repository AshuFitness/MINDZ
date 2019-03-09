package com.mindzglobal.www.mindz.Feeds;


import com.mindzglobal.www.mindz.Feeds.enums.ItemType;

public class Constants {

    public final static String PREF_MESH_PIXELS = "MeshPixels";
    public final static String PREF_DURATION    = "Duration";
    public final static String PREF_PAGE_MODE   = "PageMode";


    public static class Profile {
        public static final int MAX_AVATAR_SIZE = 1280; //px, side of square
        public static final int MIN_AVATAR_SIZE = 100; //px, side of square
        public static final int MAX_NAME_LENGTH = 120;
    }

    public static class Post extends com.mindzglobal.www.mindz.Feeds.model.Post {
        public static final int MAX_TEXT_LENGTH_IN_LIST = 300; //characters
        public static final int MAX_POST_TITLE_LENGTH = 255; //characters
        public static final int POST_AMOUNT_ON_PAGE = 10;

        @Override
        public ItemType getItemType() {
            return null;
        }

        @Override
        public void setItemType(ItemType itemType) {

        }
    }

    public static class Database {
        public static final int MAX_UPLOAD_RETRY_MILLIS = 60000; //1 minute
    }

    public static class PushNotification {
        public static final int LARGE_ICONE_SIZE = 256; //px
    }
}
