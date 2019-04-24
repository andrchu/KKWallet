package com.vip.wallet.entity;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/15 0015 18:05
 * 描述	      ${TODO}
 */

public class EventMessage {
    /**
     * 创建钱包后关闭一些页面
     */
    public static class CreateWalletFinish {
    }

    /**
     * 刷新钱包资产
     */
    public static class WalletFresh {
    }

    /**
     * 刷新所持币种
     */
    public static class TokenFresh {
    }

    /**
     * 切换货币单位刷新
     */
    public static class UnitFresh {
    }

    /**
     * 交易成功后刷新
     */
    public static class DealFresh {
    }

    /**
     * 隔离模式刷新
     */
    public static class GlModeFresh {
    }

    /**
     * 备份完成,finish
     */
    public static class BackUpSeedWord {
    }

    /**
     * 刷新资产UI
     */
    public static class PropertyUpdateUi {
    }

    /**
     * 删除钱包
     */
    public static class DelelteWallet {
    }

    public static class StartImport {
        public String pwd;

        public StartImport(String pwd) {
            this.pwd = pwd;
        }
    }

    /**
     * 活动
     */
    public static class UpdateAsKan {

    }

    /**
     * 更新钱包名
     */
    public static class UpdateWalletName {
    }

    /**
     * 刷新是否显示资产
     */
    public static class UpdateShowProperty {
    }
}
