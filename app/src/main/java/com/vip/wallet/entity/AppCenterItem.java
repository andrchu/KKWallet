package com.vip.wallet.entity;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/9 0009 18:22
 * 描述	      ${TODO}
 */

public class AppCenterItem {
    public AppCenterItem(String category) {
        this.category = category;
    }

    /**
     * datas : [{"img":"http://p7gmotqt7.bkt.clouddn.com/center_i_sw.png","state":0,"wait_img":"http://p7gmotqt7.bkt.clouddn.com/center_c_waiting.png","mark":"http://p7gmotqt7.bkt.clouddn.com/center_c_jy.png","url":"http://wallet.cc/app/daoliu/convert"},{"img":"http://p7gmotqt7.bkt.clouddn.com/center_i_sw.png","state":0,"wait_img":"http://p7gmotqt7.bkt.clouddn.com/center_c_waiting.png","mark":"http://p7gmotqt7.bkt.clouddn.com/center_c_jy.png","url":"http://wallet.cc/app/daoliu/convert"}]
     * category : 其他
     */
    public List<DatasEntity> datas;
    public String category;
    public boolean isSeleced;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AppCenterItem that = (AppCenterItem) o;

        return category != null ? category.equals(that.category) : that.category == null;

    }

    @Override
    public int hashCode() {
        return category != null ? category.hashCode() : 0;
    }

    public static class DatasEntity extends SectionEntity {
        /**
         * img : http://p7gmotqt7.bkt.clouddn.com/center_i_sw.png
         * state : 0
         * wait_img : http://p7gmotqt7.bkt.clouddn.com/center_c_waiting.png
         * mark : http://p7gmotqt7.bkt.clouddn.com/center_c_jy.png
         * url : http://wallet.cc/app/daoliu/convert
         */
        public String img = "";
        public int state;
        public String wait_img;
        public String mark;
        public String url;
        public String smallUrl = "";
        public int scard;   //0-不选择卡包 1-选择卡包
        public int chain_type = 0;  //0  ETH  1 BTC  2 EOS
        //        public String text = "天天抽奖";
        public String dappTitle;
        public String dappContent;
        public String dappSource;

        public DatasEntity(boolean isHeader, String header) {
            super(isHeader, header);
        }

        public boolean isNeedCard() {
            return scard == 1;
        }


        public boolean hasClick() {
            return state == 1;
        }
    }
}
