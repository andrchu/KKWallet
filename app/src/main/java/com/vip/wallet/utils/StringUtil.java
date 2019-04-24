package com.vip.wallet.utils;

import android.support.annotation.StringRes;

import com.blankj.utilcode.util.StringUtils;
import com.google.common.base.Charsets;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Wallet;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/26 0026 17:53
 * 描述	      ${TODO}
 */

public class StringUtil {
    public static byte[] toBytes(String str) {
        return str.getBytes(Charsets.UTF_8);
    }

    public static String getHideString(String str) {
        return getHideString(str, 4);
    }

    public static String getHideString(String str, int length) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        if (str.length() <= 22) {
            return str;
        }
        return str.replaceAll("(.{" + length + "}).+(.{" + length + "})", "$1...$2");
    }

    public static String getFirstStr(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str.substring(0, 1);
    }

    public static String getString(@StringRes int resId) {
        return ScApplication.getInstance().getString(resId);
    }

    public static String formatAddress(String address) {
        if (address.toLowerCase().startsWith("0x")) {
            return address;
        } else {
            return "0x" + address;
        }
    }

    public static String getNo0xAddress(String address) {
        if (StringUtils.isEmpty(address))
            return address;
        if (address.toLowerCase().startsWith("0x")) {
            return address.substring(2, address.length());
        } else {
            return address;
        }
    }

    public static String getAllWalletString(List<Wallet> wallets) {
        StringBuilder sb = new StringBuilder();
        for (Wallet wallet : wallets) {
            sb.append(StringUtil.formatAddress(wallet.getWallet_address())).append(",");
        }
        String address = sb.toString();
        return address.substring(0, address.length() - 1);
    }

    public static boolean isBtcAddress(String address) {
        if (StringUtils.isEmpty(address))
            return false;
        return address.matches("^[mn213][a-km-zA-HJ-NP-Z1-9]{25,34}$");
    }

    public static boolean isEthAddress(String address) {
        if (StringUtils.isEmpty(address))
            return false;
        return address.matches("^(0x|0X)?[0-9A-Fa-f]{40}$");
    }

    public static boolean isEosAccount(String account) {
        if (StringUtils.isEmpty(account))
            return false;
        return account.matches("[0-5a-z.]{3,13}");
    }

    public static String getSignPartByTime() {
        StringBuilder builder = new StringBuilder();
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        builder.append("timestamp=").append(time);
        builder.append("&");
        builder.append("sign=").append(EncryptUtil.getSignString(time));
        return builder.toString();
    }

    public static String lastSubString(String string, int length) {
        if (StringUtils.isEmpty(string))
            return "";
        if (string.length() < length)
            return string;

        return string.substring(string.length() - length, string.length());
    }

    public static String getChainName(int chainType) {
        switch (chainType) {
            case 0:
                return "ETH";
            case 1:
                return "BTC";
            case 2:
                return "EOS";
            default:
                return "ETH";
        }
    }

    public static String signBuilder(String address) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder
                .append("?address=")
                .append(address)
                .append("&")
                .append("sign=")
                .append(EncryptUtil.getSignString(address))
                .append("&")
                .append("version=")
                .append(Constants.API_VERSION);
        return urlBuilder.toString();
    }
}
