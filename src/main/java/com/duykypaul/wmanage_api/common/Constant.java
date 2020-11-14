package com.duykypaul.wmanage_api.common;

import org.javatuples.Triplet;

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
    public static final Integer LENGTH_DEFAULT = 13000;

    public static class Auth {
        public static final String ADMIN_EMAIL = "lminh9812@gmail.com";
        public static final String ADMIN_PASSWORD = "890*()iop";
        public static final String ADMIN_NAME = "admin";
        public static final String AVATAR = "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png";
        public static final int EXPIRATION = 60 * 24;
        public static final String AVATAR_DEFAULT = "avatar_default.jpg";
    }

    public static class MATERIAL_TYPE {
        public static final List<Triplet<String, String, String>> LST_MATERIAL_TYPE =
            Arrays.asList(
                new Triplet<>("A", "Good", "6X150X150"),
                new Triplet<>("A", "Good", "6X200X200"),
                new Triplet<>("A", "Good", "6X250X250"),
                new Triplet<>("B", "Medium", "6X150X150"),
                new Triplet<>("B", "Medium", "6X200X200"),
                new Triplet<>("B", "Medium", "6X250X250"),
                new Triplet<>("A", "Good", "8X150X150"),
                new Triplet<>("A", "Good", "8X200X200"),
                new Triplet<>("A", "Good", "8X250X250"),
                new Triplet<>("B", "Medium", "8X150X150"),
                new Triplet<>("B", "Medium", "8X200X200"),
                new Triplet<>("B", "Medium", "8X250X250")
            );
    }
    public static class BRANCH {
        public static final List<Triplet<String, String, Integer>> LST_BRANCH =
            Arrays.asList(
                new Triplet<>("SG", "SaiGon", 10000),
                new Triplet<>("DN", "DaNang", 5000),
                new Triplet<>("HN", "HaNoi", 12000)
            );
    }

    public static class MATERIAL {
        public static final List<Triplet<String, String, Integer>> LST_BRANCH =
            Arrays.asList(
                new Triplet<>("SG", "SaiGon", 10000),
                new Triplet<>("DN", "DaNang", 5000),
                new Triplet<>("HN", "HaNoi", 12000)
            );
    }
}
