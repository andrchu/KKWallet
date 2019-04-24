package com.vip.wallet.utils;

import java.util.List;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2017/3/13 15:58   <br/><br/>
 * 描述:	      ${TODO}
 */
public class ListUtil {
    /**
     * 获取集合对象，  只知道对象的情况下获取
     *
     * @param list 集合
     * @param data 对象
     */
    public static <T> T getObj(List<T> list, T data) {
        if (data == null || list == null) {
            return null;
        }
        int position = list.indexOf(data);
        if (position < 0) {
            return null;
        }
        return list.get(position);
    }

    /**
     * 获取对象position
     *
     * @param list
     * @param data
     * @param <T>
     * @return
     */
    public static <T> int getPosition(List<T> list, T data) {
        if (data == null) {
            return -1;
        }

        return list.indexOf(data);
    }

    /**
     * 判断集合是否为空
     */
    public static boolean isEmpty(List list) {
        return list == null || list.size() <= 0;
    }

    public static boolean equals(List a1, List a2) {
        if ((isEmpty(a1) && isEmpty(a2)))
            return true;
        if (a1.size() != a2.size())
            return false;
        for (Object o : a1) {
            if (!a2.contains(o)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isContains(List a1, List a2) {
        if (ListUtil.isEmpty(a1) || ListUtil.isEmpty(a2)) {
            return false;
        }
        for (Object o : a1) {
            if (a2.contains(o)) {
                return true;
            }
        }
        return false;
    }

    public static <T> void replace(int position, List<T> list, T t) {
        list.remove(position);
        list.add(position, t);
    }

    public static int getSize(List list) {
        if (isEmpty(list))
            return 0;
        return list.size();
    }
}
