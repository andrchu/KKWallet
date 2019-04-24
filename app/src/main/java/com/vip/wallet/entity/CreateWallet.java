package com.vip.wallet.entity;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/8 0008 13:58
 * 描述	      ${TODO}
 */

public class CreateWallet implements Serializable {
    public String walletName;
    public String walletPwd;
    public String walletPwd2;
    public String walletPwdHint;
    public boolean isConsent;
}
