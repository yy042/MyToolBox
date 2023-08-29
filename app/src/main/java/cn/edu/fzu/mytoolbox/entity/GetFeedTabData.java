package cn.edu.fzu.mytoolbox.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetFeedTabData implements Serializable {

    /**
     * 10.3新增，是否显示副标题：1：不带副标题样式 2：带副标题样式
     */
    public String isShowSubTitle = "";
    /**
     * feed流tab列表
     */
    public List<TabListBean> tabList = new ArrayList<TabListBean>();

    /**
     * 10.3新增，跳转引导条
     */
    public JumpGuideBarBean jumpGuideBar = new JumpGuideBarBean();

    public static class TabListBean implements Serializable {
        /**
         * isDefault : 10.0新增是否默认选中（0：否，1：是） string
         * link : wap页面跳转链接 string
         * linkType : wap页面跳转链接类型 string
         * order : tab栏对应feed流查询order string
         * redFlag : 是否显示红点：0否 1是 string
         * subTitle : 10.3新增，副标题 string
         * tabIcon : tab栏图标 string
         * tabName : tab栏名称 string
         * tabType : tab栏显示类型：1：显示标题 2：显示图标 string
         * timestamp : 时间戳（yyyyMMddHHmmss） string
         * tagList : 10.4新增,标签列表 [{"tagID": "string","tagIcon": "string","tagName": "string","tagType": "string"}]
         * type : tab栏显示内容类型：1.显示feed流原生列表 2：显示wap页面 3：10.0新增显示原生关注页 string
         */

        public String isDefault = "";
        public String link = "";
        public String linkType = "";
        public String order = "";
        public String redFlag = "";
        public String subTitle = "";
        public String tabIcon = "";
        public String tabName = "";
        public String tabType = "";
        public String timestamp = "";
        public String type = "";
        public List<TagListBean> tagList = new ArrayList<TagListBean>();

        public static class TagListBean implements Serializable {
            /**
             * tagID : 标签ID string
             * tagIcon : 图标 string
             * tagName : 标题 string
             * tagType : 类型（1：普通类型 2：强调类型） string
             */

            public String tagID = "";
            public String tagIcon = "";
            public String tagName = "";
            public String tagType = "";
        }
    }

    public static class JumpGuideBarBean implements Serializable {
        /**
         * iconUrl : 图标地址 string
         * provinceCode : 大数据省编码 string
         * recommender : 大数据插码推荐码 string
         * sceneId : 大数据场景id string
         * title : 标题 string
         */

        public String iconUrl = "";
        public String provinceCode = "";
        public String recommender = "";
        public String sceneId = "";
        public String title = "";
    }

    /**
     * 是否默认选中（0：否，1：是）
     */
    public static class IS_DEFAULT {
        public static final String NO = "0";
        public static final String YES = "1";
    }

    /**
     * 是否显示红点：0否 1是
     */
    public static class RED_FLAG {
        public static final String NO = "0";
        public static final String YES = "1";
    }

    /**
     * tab栏显示类型：1：显示标题 2：显示图标
     */
    public static class TAB_TYPE {
        public static final String TITLE = "1";
        public static final String ICON = "2";
    }

    /**
     *  tab栏显示内容类型：1.显示feed流原生列表 2：显示wap页面 3：10.0新增显示原生关注页
     */
    public static class TYPE {
        public static final String LIST = "1";
        public static final String WAP = "2";
        public static final String FOCUS = "3";
    }

    /**
     * 是否显示副标题：1：不带副标题样式 2：带副标题样式
     */
    public static class IS_SHOW_SUB_TITLE {
        public static final String NO = "1";
        public static final String YES = "2";
    }

    /**
     * tag标签类型：（1：普通类型 2：强调类型）
     */
    public static class TAG_TYPE {
        public static final String NORMAL = "1";
        public static final String EMPHASIS = "2";
    }
}
