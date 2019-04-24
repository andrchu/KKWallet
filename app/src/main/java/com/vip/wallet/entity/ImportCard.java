package com.vip.wallet.entity;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/17 0017 13:55
 * 描述	      ${TODO}
 */

public class ImportCard implements Serializable{
    public String cardName;
    public boolean isConsent;
    public String seedKey;
    public String privateKey;
    public Path path = new Path("m/44'/60'/0'/0/0", "m/44'/60'/0'/0/0 Jaxx,Metamask,imToken (ETH)", true);
    public int chainType;   //0 - ETH  1 - BTC   2 - EOS
    public int importType;   //0 - 助记词    1 - 私钥
}
