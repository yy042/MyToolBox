package cn.edu.fzu.mytoolbox.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetFeedListData implements Serializable {

    /**
     * feed流列表
     */
    public List<FeedListBean> feedList = new ArrayList<FeedListBean>();

    public static class FeedListBean implements Serializable, MultiItemEntity {
        /**
         * adLists : 10.0新增广告位列表 [{"imageUrl": "string","link": "string","linkType": "string","provinceCode": "string","recommender": "string","sceneId": "string","title": "string"}]
         * contentAreaList : 内容区域 [{"completionInfo":{"link":"string","linkType":"string","provinceCode":"string","recommender":"string","sceneId":"string","title":"string"},"countDown":{"endTime":"string","startTime":"string"},"location":{"icon":"string","title":"string"},"mainTitle":{"color":"string","title":"string","type":"string"},"numText":"string","picList":[{"imageRatio":"string","imageUrl":"string","link":"string","linkType":"string","provinceCode":"string","recommender":"string","sceneId":"string","title":"string"}],"price":{"isOriginalPriceLine":"string","isShowPriceUnit":"string","originalPrice":"string","originalPriceColor":"string","priceColor":"string","priceDecimal":"string","priceInteger":"string"},"saleTipList":[{"fontColor":"string","frameColor":"string","imageUrl":"string","title":"string","type":"string"}],"type":"string"}]
         * externalOfferCode : feed流卡片id string
         * firstSecondThreeClass : feed流卡片业务类型编码，用于查询单个feed流卡片使用 string
         * quickRecharge : 10.0新增快速充值 {"denominations": [{"mainTitle": "string","price": "string","subtitle": "string"}],"link": "string","linkType": "string","title": "string","withOutLoginRechargeFlag": "string"}
         * link : 跳转链接 string
         * linkType : 跳转类型 string
         * picArea : 图片区域 {"commentList":[{"backgroundColor":"string","fontColor":"string","title":"string"}],"endTime":"string","imageRatio":"string","imageUrl":"string","link":"string","linkType":"string","picList":[{"imageRatio":"string","imageUrl":"string","link":"string","linkType":"string","provinceCode":"string","recommender":"string","sceneId":"string","title":"string"}],"playType":"string","provinceCode":"string","recommender":"string","sceneId":"string","startTime":"string","stock":"string","title":"string","topImage":"string","topImageBeforeLive":"string","topImageLiving":"string","type":"string","videoCommerceId":"string","videoDuration":"string","videoStreamUrl":"string","videoStreamUrlType":"string"}
         * provinceCode : 省份、集团编码 string
         * recommender : 插码推荐码 string
         * sceneId : 大数据场景id string
         * title : 标题 string
         * type : 卡片类型 1、通用 2、短视频 3、直播 4：10.0新增广告位列表、5：10.0新增快速充值 string
         */

        public String externalOfferCode = "";
        public String firstSecondThreeClass = "";
        public String link = "";
        public String linkType = "";
        public PicAreaBean picArea = new PicAreaBean();
        public QuickRechargeBean quickRecharge = new QuickRechargeBean();
        public String provinceCode = "";
        public String recommender = "";
        public String sceneId = "";
        public String title = "";
        public String type = "0";
        public String feedTabName = "";
        public int pos = 0;
        public List<ContentAreaListBean> contentAreaList = new ArrayList<ContentAreaListBean>();
        public List<AdListBean> adLists = new ArrayList<AdListBean>();

        @Override
        public int getItemType() {
            switch (type) {
                case FEED_LIST_ITEM_TYPE.LIVE:
                case FEED_LIST_ITEM_TYPE.VIDEO:
                case FEED_LIST_ITEM_TYPE.ADVERTISE:
                case FEED_LIST_ITEM_TYPE.RECHARGE: {
                    return parseInt(type);
                }
                default: {
                    if (picArea != null) {
                        if (PICAREA_TYPE.MANY_IMAGE.equals(picArea.type)) {
                            return FEED_ADAPTER_ITEM_TYPE.MANY_IMAGE;
                        } else {
                            return FEED_ADAPTER_ITEM_TYPE.ONE_IMAGE;
                        }
                    } else {
                        return FEED_ADAPTER_ITEM_TYPE.NULL;
                    }
                }
            }
        }

        public static class PicAreaBean implements Serializable, MultiItemEntity{
            /**
             * commentList : 评论列表 [{"backgroundColor":"string","fontColor":"string","title":"string"}]
             * endTime : 直播结束时间（时间格式：yyyyMMddHHmmss）tring
             * imageRatio : 图片区域默认宽高比 string
             * imageUrl : 图片url string
             * link : 跳转链接 string
             * linkType : 跳转链接类型 string
             * picList : 主图区配图 [{"imageRatio":"string","imageUrl":"string","link":"string","linkType":"string","provinceCode":"string","recommender":"string","sceneId":"string","title":"string"}]
             * playType : 播放类型（0.不直接播放、1.直接播放）string
             * provinceCode : 省份、集团编码 string
             * recommender : 插码推荐码 string
             * sceneId : 大数据场景ID string
             * startTime : 直播开始时间（时间格式：yyyyMMddHHmmss）string
             * stock : 库存字符串 string
             * title : 标题 string
             * topImage : 配置标签 string
             * topImageBeforeLive : 直播开始前标签（倒计时前标签） string
             * topImageLiving : 直播中标签（倒计时中标签）string
             * type : 类型：1：单图2：多图 string
             * videoCommerceId : 直播间id string
             * videoDuration : 短视频时长(单位秒) string
             * videoStreamUrl : 视频流地址 string
             * videoStreamUrlType : 直播推流地址类型(1:m3u8 2:flv 3:rtmp) string
             */

            public String endTime = "";
            public String imageRatio = "";
            public String imageUrl = "";
            public String link = "";
            public String linkType = "";
            public String playType = "";
            public String provinceCode = "";
            public String recommender = "";
            public String sceneId = "";
            public String startTime = "";
            public String stock = "";
            public String title = "";
            public String topImage = "";
            public String topImageBeforeLive = "";
            public String topImageLiving;
            public String type = "";
            public String videoCommerceId = "";
            public String videoDuration = "";
            public String videoStreamUrl = "";
            public String videoStreamUrlType = "";
            public List<CommentListBean> commentList = new ArrayList<CommentListBean>();
            public List<PicListBean> picList = new ArrayList<PicListBean>();

            public static class CommentListBean implements Serializable {
                /**
                 * backgroundColor : 背景颜色 string
                 * fontColor : 字体颜色 string
                 * title : 标题 string
                 */

                public String backgroundColor = "";
                public String fontColor = "";
                public String title = "";
            }

            @Override
            public int getItemType() {
                switch (type) {
                    case PICAREA_TYPE.ONE_IMAGE:
                    case PICAREA_TYPE.MANY_IMAGE:{
                        return parseInt(type);
                    }
                    default: {
                        return parseInt(PICAREA_TYPE.ONE_IMAGE);
                    }
                }
            }

        }

        public static class ContentAreaListBean implements Serializable, MultiItemEntity {
            /**
             * completionInfo : 末尾卡片按钮 {"link":"string","linkType":"string","provinceCode":"string","recommender":"string","sceneId":"string","title":"string"}
             * countDown : 倒计时 {"endTime":"string","startTime":"string"}
             * location : 地址 {"icon":"string","title":"string"}
             * mainTitle : 主标题 {"color":"string","title":"string","type":"string"}
             * numText : 人数文案 string
             * picList : 内容区配图 [{"imageRatio":"string","imageUrl":"string","link":"string","linkType":"string","provinceCode":"string","recommender":"string","sceneId":"string","title":"string"}]
             * price : 价格 {"isOriginalPriceLine":"string","isShowPriceUnit":"string","originalPrice":"string","originalPriceColor":"string","priceColor":"string","priceDecimal":"string","priceInteger":"string"}
             * saleTipList : 随销条 [{"fontColor":"string","frameColor":"string","imageUrl":"string","title":"string","type":"string"}]
             * type : 类型：1：主文案、2：随销条、3：价格、4：位置、5：倒计时、6：人数、7：配图：一行一个、8：配图2：一行两个、9：末尾卡片按钮列表 string
             */

            public CompletionInfoBean completionInfo = new CompletionInfoBean();
            public CountDownBean countDown = new CountDownBean();
            public LocationBean location = new LocationBean();
            public MainTitleBean mainTitle = new MainTitleBean();
            public String numText = "";
            public PriceBean price = new PriceBean();
            public String type = "";
            public List<PicListBean> picList = new ArrayList<PicListBean>();
            public List<SaleTipListBean> saleTipList = new ArrayList<SaleTipListBean>();

            @Override
            public int getItemType() {
                switch (type) {
                    case CONTENTAREA_TYPE.SALE_TIP:
                    case CONTENTAREA_TYPE.PRICE:
                    case CONTENTAREA_TYPE.LOCATION:
                    case CONTENTAREA_TYPE.COUNTDOWN:
                    case CONTENTAREA_TYPE.NUM:
                    case CONTENTAREA_TYPE.PIC_ONE:
                    case CONTENTAREA_TYPE.PIC_TWO:
                    case CONTENTAREA_TYPE.COMPLETION: {
                        return parseInt(type);
                    }
                    default: {
                        return parseInt(CONTENTAREA_TYPE.MAIN_TITLE);
                    }
                }
            }

            public static class CompletionInfoBean implements Serializable {
                /**
                 * link : 跳转链接 string
                 * linkType : 跳转链接类型 string
                 * provinceCode : 省份、集团编码 string
                 * recommender : 插码推荐码 string
                 * sceneId : 大数据场景ID string
                 * title : 标题 string
                 */

                public String link = "";
                public String linkType = "";
                public String provinceCode = "";
                public String recommender = "";
                public String sceneId = "";
                public String title = "";
            }

            public static class CountDownBean implements Serializable {
                /**
                 * endTime : 结束时间（时间格式：yyyyMMddHHmmss）string
                 * startTime : 开始时间（时间格式：yyyyMMddHHmmss）string
                 */

                public String endTime = "";
                public String startTime = "";
            }

            public static class LocationBean implements Serializable {
                /**
                 * icon : 图标 string
                 * title : 标题 string
                 */

                public String icon = "";
                public String title = "";
            }

            public static class MainTitleBean implements Serializable {
                /**
                 * color : 色值 string
                 * title : 标题 string
                 * type : 主文案样式，1:单行样式，2：两行样式 string
                 */

                public String color = "";
                public String title = "";
                public String type = "";
            }

            public static class PriceBean implements Serializable {
                /**
                 * isOriginalPriceLine : 原价是否划横线：0：否1：是 string
                 * isShowPriceUnit : 售价是否显示人民币符号：0：否1：是 string
                 * originalPrice : 原价 string
                 * originalPriceColor : 原价颜色 string
                 * priceColor : 售价颜色 string
                 * priceDecimal : 售价小数部分和其他 string
                 * priceInteger : 售价整数部分 string
                 */

                public String isOriginalPriceLine = "";
                public String isShowPriceUnit = "";
                public String originalPrice = "";
                public String originalPriceColor = "";
                public String priceColor = "";
                public String priceDecimal = "";
                public String priceInteger = "";
            }

            public static class SaleTipListBean implements Serializable {
                /**
                 * fontColor : 字体颜色 string
                 * frameColor : 边框颜色 string
                 * imageUrl : 图片url string
                 * title : 标题 string
                 * type : 随销条显示类型，1：文字 2：图片 string
                 */

                public String fontColor = "";
                public String frameColor = "";
                public String imageUrl = "";
                public String title = "";
                public String type = "";
            }
        }

        public static class QuickRechargeBean implements Serializable {
            /**
             * denominations : 面额列表 [{"mainTitle": "string","price": "string","subtitle": "string"}]
             * dialogText : 弹窗提示文案
             * loginRechargeText : 充值折扣文案
             * withOutLoginRechargeFlag : 未登录充值开关 1 ：引导弹窗，2：需要登录，3：都不需要 用QueryRechargeSudokuData.WITHOUT_LOGIN_RECHARGE_FLAG赋值 string
             * title : 标题 string
             * link : 跳转链接 string
             * linkType : 跳转链接类型 string
             */

            public String title = "";
            public String link = "";
            public String linkType = "";
            public String dialogText = "";
            public String loginRechargeText = "";
            public String withOutLoginRechargeFlag = "";
            public List<DenominationBean> denominations = new ArrayList<DenominationBean>();

            public static class DenominationBean implements Serializable {
                /**
                 * mainTitle : 主标题 string
                 * price : 金额  string
                 * subtitle : 副标题 string
                 */

                public String mainTitle = "";
                public String price = "";
                public String subtitle = "";
            }
        }

        public static class PicListBean implements Serializable {
            /**
             * imageRatio : 图片区域默认宽高比 string
             * imageUrl : 图片url string
             * link : 跳转链接 string
             * linkType : 跳转链接类型 string
             * provinceCode : 省份、集团编码 string
             * recommender : 插码推荐码 string
             * sceneId : 大数据场景id string
             * title : 标题 string
             */

            public String imageRatio = "";
            public String imageUrl = "";
            public String link = "";
            public String linkType = "";
            public String provinceCode = "";
            public String recommender = "";
            public String sceneId = "";
            public String title = "";
        }

        public static class AdListBean implements Serializable{
            /**
             * imageUrl : 图片url string
             * link : 跳转链接 string
             * linkType : 跳转链接类型 string
             * provinceCode : 省份、集团编码 string
             * recommender : 插码推荐码 string
             * sceneId : 大数据场景id string
             * title : 标题 string
             */

            public String imageUrl = "";
            public String link = "";
            public String linkType = "";
            public String provinceCode = "";
            public String recommender = "";
            public String sceneId = "";
            public String title = "";
        }
    }

    /**
     * 卡片类型 1、通用 2、短视频 3、直播 4：10.0新增广告位列表、5：10.0新增快速充值
     */
    public static class FEED_LIST_ITEM_TYPE {
        public static final String COMMON = "1";
        public static final String VIDEO = "2";
        public static final String LIVE = "3";
        public static final String ADVERTISE = "4";
        public static final String RECHARGE = "5";
    }


    /**
     * 主图区item类型 0、单图 1、多图 2、短视频 3、直播 4：10.0新增广告位列表、5：10.0新增快速充值
     */
    public static class FEED_ADAPTER_ITEM_TYPE {
        public static final int NULL = -1; // 无主图区类型
        public static final int ONE_IMAGE = 0;
        public static final int MANY_IMAGE = 1;
        public static final int VIDEO = 2;
        public static final int LIVE = 3;
        public static final int ADVERTISE = 4;
        public static final int RECHARGE = 5;
    }

    /**
     * 播放类型（0.不直接播放、1.直接播放）
     */
    public static class PICAREA_PLAY_TYPE {
        public static final String NO_AUTO_PALY = "0";
        public static final String AUTO_PALY = "1";
    }

    /**
     * 类型：1：单图 2：多图 ContentArea
     */
    public static class PICAREA_TYPE {
        public static final String ONE_IMAGE = "1";
        public static final String MANY_IMAGE = "2";
    }

    /**
     * 直播推流地址类型 1:m3u8 2:flv 3:rtmp
     */
    public static class PICAREA_VIDEO_STREAM_URL_TYPE {
        public static final String M3U8 = "1";
        public static final String FLV = "2";
        public static final String RTMP = "3";
    }

    /**
     * 内容区类型：1：主文案、2：随销条、3：价格、4：位置、5：倒计时、6：人数、7：配图：一行一个、8：配图2：一行两个、9：末尾卡片按钮列表
     */
    public static class CONTENTAREA_TYPE {
        public static final String MAIN_TITLE = "1";
        public static final String SALE_TIP = "2";
        public static final String PRICE = "3";
        public static final String LOCATION = "4";
        public static final String COUNTDOWN = "5";
        public static final String NUM = "6";
        public static final String PIC_ONE = "7";
        public static final String PIC_TWO = "8";
        public static final String COMPLETION = "9";
    }

    /**
     * 主文案样式，1:单行样式，2：两行样式 SaleTipListBean
     */
    public static class CONTENTAREA_MIANTITLE_TYPE {
        public static final String ONE_LINE = "1";
        public static final String TWO_LINE = "2";
    }

    /**
     * 原价是否划横线：0：否1：是
     */
    public static class CONTENTAREA_PRICE_IS_ORIGINALPRICE_LINE {
        public static final String NO = "0";
        public static final String YES = "1";
    }

    /**
     * 售价是否显示人民币符号：0：否1：是
     */
    public static class CONTENTAREA_PRICE_IS_SHOW_PRICE_UNIT {
        public static final String NO = "0";
        public static final String YES = "1";
    }

    /**
     * 随销条显示类型，1：文字 2：图片
     */
    public static class CONTENTAREA_SALE_TIP_TYPE {
        public static final String TEXT = "1";
        public static final String IMAGE = "2";
    }

    public static int parseInt(String number){
        int num;
        try {
            num = Integer.parseInt(number);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return num;
    }
}
