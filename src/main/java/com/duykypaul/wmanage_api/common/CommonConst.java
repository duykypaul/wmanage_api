package com.duykypaul.wmanage_api.common;

import com.duykypaul.wmanage_api.beans.MaterialBean;
import com.duykypaul.wmanage_api.model.Material;
import org.javatuples.Triplet;
import org.modelmapper.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class CommonConst {
    public static final String DCOM_API_URL = "http://localhost:1102/api";
    public static final String DCOM_VUE_URL = "http://localhost:8080/verify-email";
    public static final String UPLOAD_ROOT = "uploads";
    public static final String UPLOAD_POST = "posts" + File.separator + "images";
    public static final String UPLOAD_USER = "users" + File.separator + "images";
    public static final String BACKUP_DB = "backupDB";
    public static final int DB_PORT = 3308;
    public static final String COMMA = ",";
    public static final Integer LENGTH_DEFAULT = 13000;
    public static final String BLANK = "";
    public static final String SPACE = " ";
    public static final String UNDERSCORE = "_";
    public static final String STEEL_BLADE_THICKNESS = "5";
    public static final String NUMBER_0 = "0";
    public static final String NUMBER_1 = "1";
    public static final String NUMBER_2 = "2";
    public static final String NUMBER_3 = "3";
    public static final String NUMBER_4 = "4";
    public static final String NUMBER_5 = "5";
    public static final String NUMBER_6 = "6";
    public static final String NUMBER_7 = "7";
    public static final String NUMBER_8 = "8";
    public static final String NUMBER_9 = "9";
    public static final String NUMBER_10 = "10";
    public static final String NUMBER_100 = "100";
    public static final String NUMBER_1000 = "1000";

    public static class AUTH {
        public static final String ADMIN_EMAIL = "lminh9812@gmail.com";
        public static final String ADMIN_PASSWORD = "890*()iop";
        public static final String ADMIN_NAME = "admin";
        public static final String AVATAR = "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png";
        public static final int EXPIRATION = 60 * 24;
        public static final String AVATAR_DEFAULT = "avatar_default.jpg";

        public enum ROLE {
            ROLE_USER,
            ROLE_MODERATOR,
            ROLE_ADMIN
        }

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
        public static final Type TYPE_LIST_BEAN = new TypeToken<List<MaterialBean>>() {}.getType();
        public static final Type TYPE_LIST_ENTITY = new TypeToken<List<Material>>() {}.getType();

        public enum STATUS {
            /**
             * đang hiện hữu trong kho
             */
            ACTIVE,
            /**
             * đã cắt
             */
            INACTIVE,
            /**
             * đã được đưa vào dự kiến toriai
             */
            PLAN,
            /**
             * các nguyên liệu ảo phục vụ cho việc toriai, tránh gián đoạn
             */
            FAKE
        }

        public enum SEI_KBN {
            /**
             * nguyen lieu goc
             */
            B,
            /**
             * sản phẩm xuất kho
             */
            P,
            /**
             * sản phẩm còn lại
             */
            R,
            /**
             * sản phẩm dự định
             */
            Y
        }
    }

    /**
     * CONSIGNMENT <--> ORDER
     */
    public static class ORDER {
        public enum INVENTORY_STATUS {
            /**
             * mới nhập kho
             */
            IMPORTED,
            /**
             * đã đưa vào kế hoạch gia công
             */
            PLAN,
            /**
             * đã gia công xong
             */
            TORIAI,

            /**
             * đã xuất kho
             */
            EXPORTED
        }
    }

    public static class TORIAI {
        public static final int NUMBER_COLUMN_RETSU = 12;
        public enum TYPE_TORIAI {
            /**
             * cắt nhanh, tiết kiệm thời gian được ưu tiên trước, sau đó mới xét đến tiết kiệm
             */
            FAST,

            /**
             * cắt tiết kiệm, tiết kiệm phần thừa được ưu tiên trước, sau đó mới xét đến thời gian
             */
            SAVES
        }
        public enum STATUS {
            /**
             * đăng kí toriai
             */
            REGISTER,
            /**
             * đã toriai xong
             */
            DONE
        }
    }
}
