package com.vip.wallet.http;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 创建者     金国栋
 * 创建时间   2018/1/17 14:17
 * 描述	      ${TODO}
 */
public class ScGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    ScGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String string = value.string();
        LogUtils.json(string);
        try {
            return adapter.fromJson(string);
        } finally {
            value.close();
        }
    }
}
