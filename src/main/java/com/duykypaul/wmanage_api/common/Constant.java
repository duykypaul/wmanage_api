package com.duykypaul.wmanage_api.common;

import javafx.util.Pair;
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

    public static class Auth {
        public static final String ADMIN_EMAIL = "lminh9812@gmail.com";
        public static final String ADMIN_PASSWORD = "890*()iop";
        public static final String ADMIN_NAME = "admin";
        public static final int EXPIRATION = 60 * 24;
        public static final String AVATAR_DEFAULT = "avatar_default.jpg";
    }

    public static class MATERIAL_TYPE {
        public static final List<Pair<String, String>> LST_MATERIAL_TYPE =
            Arrays.asList(
                new Pair<>("A", "6X150X150"),
                new Pair<>("A", "6X200X200"),
                new Pair<>("A", "6X250X250"),
                new Pair<>("B", "6X150X150"),
                new Pair<>("B", "6X200X200"),
                new Pair<>("B", "6X250X250"),
                new Pair<>("A", "8X150X150"),
                new Pair<>("A", "8X200X200"),
                new Pair<>("A", "8X250X250"),
                new Pair<>("B", "8X150X150"),
                new Pair<>("B", "8X200X200"),
                new Pair<>("B", "8X250X250")
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
}
