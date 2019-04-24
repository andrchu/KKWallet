package com.vip.wallet.entity;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/8 0008 13:58
 * 描述	      ${TODO}
 */

public class ImportWallet implements Serializable {
    public String walletName = "未命名";
    public boolean isConsent;
    public String seedKey;
    public Path path = new Path("m/44'/60'/0'/0/0", "m/44'/60'/0'/0/0 Jaxx,Metamask,imToken (ETH)", true);
}
