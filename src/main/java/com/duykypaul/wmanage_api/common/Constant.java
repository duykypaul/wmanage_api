package com.duykypaul.wmanage_api.common;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Constant {
    public static final String DCOM_API_URL = "http://localhost:1102/api";
    public static final String DCOM_VUE_URL = "http://localhost:8080/verify-email";
    public static final String UPLOAD_ROOT = "uploads";
    public static final String UPLOAD_POST = "posts" + File.separator + "images";
    public static final String UPLOAD_USER = "users" + File.separator + "images";
    public static final String BACKUP_DB = "backupDB";
    public static final int DB_PORT = 3308;
    public static final String COMMA = ",";

    public static class Auth {
        public static final String ADMIN_EMAIL = "lminh9812@gmail.com";
        public static final String ADMIN_PASSWORD = "890*()iop";
        public static final String ADMIN_NAME = "admin";
        public static final int EXPIRATION = 60 * 24;
        public static final String AVATAR_DEFAULT = "avatar_default.jpg";
    }

    public static class Category {
        public static final List<String> LST_CATEGORY = Arrays.asList(
            "Ảnh troll", "Cách gặp ma", "Clip funvl", "Xác ướp", "Ảo tưởng sức mạnh", "Running man", "Ảnh bựa VL", "Faptv", "500 anh em",
            "Ancient aliens", "Video cảm động", "Siêu nhân", "Video kinh dị", "Thánh cuồng", "Comment hài", "Nhạc Remix hay", "Ảnh ấn tượng",
            "Pháo hoa Tết", "Ji Suk Jin", "Cao Bá Hưng", "Hài Quang Thắng", "Xăm trổ", "Chuyện tình lãng mạn", "Giang hồ");
    }

    public static class Post {
        public static final List<String> LST_URL_IMAGE = Arrays.asList(
            "https://i.memeful.com/media/post/GMEZxyR_700wa_0.gif", "https://i.memeful.com/media/post/ER5rbyd_700wa_0.gif",
            "https://i.memeful.com/media/post/lMzzGBM_700wa_0.gif", "https://i.memeful.com/media/post/edvgX8w_700wa_0.gif",
            "https://i.memeful.com/media/post/odgmQGM_700wa_0.gif", "https://i.memeful.com/media/post/edvEj5R_700wa_0.gif",
            "https://i.memeful.com/media/post/kRp6z2w_700wa_0.gif", "https://i.memeful.com/media/post/mdGW0Vd_700wa_0.gif",
            "https://i.memeful.com/media/post/NwrGA7M_700wa_0.gif", "https://i.memeful.com/media/post/YMKmXGd_700wa_0.gif"
        );
    }
}
