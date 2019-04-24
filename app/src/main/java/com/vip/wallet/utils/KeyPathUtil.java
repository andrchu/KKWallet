package com.vip.wallet.utils;

import com.google.common.collect.ImmutableList;

import org.bitcoinj.crypto.ChildNumber;

import java.util.ArrayList;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/14 0014 14:38
 * 描述	      ${TODO}
 */

public class KeyPathUtil {
    public static ImmutableList<ChildNumber> parsePath(String str) {
        String[] split = str.split("/");
        ArrayList<ChildNumber> childNumbers = new ArrayList<>();
        for (String numberStr : split) {
            if (!"m".equals(numberStr)) {
                boolean b = numberStr.substring(numberStr.length() - 1).equals("\'");
                String newNumberStr = numberStr;
                if (b) {
                    newNumberStr = numberStr.substring(0, numberStr.length() - 1);
                }
                childNumbers.add(new ChildNumber(Integer.parseInt(newNumberStr), b));
            }

        }
        return ImmutableList.copyOf(childNumbers);
    }
}
