package com.vip.wallet.http;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/22 14:44   <br/><br/>
 * 描述:		自定义Gson适配器，解决基本数据类型为null时候解析失败
 */
public class GsonAdapter {

    private static class IntegerDefaultAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                    return 0;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsInt();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    private static class DoubleDefaultAdapter implements JsonSerializer<Double>, JsonDeserializer<Double> {
        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                    return 0.00;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsDouble();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    private static class FloatDefaultAdapter implements JsonSerializer<Float>, JsonDeserializer<Float> {
        @Override
        public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                    return 0.0f;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsFloat();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Float src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    private static class LongDefaultAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                    return 0l;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsLong();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    public static class SerializeExclusionStrategy implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            Expose annotation = f.getAnnotation(Expose.class);
            return !(annotation == null || annotation.serialize());
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    public static class DeserializeExclusionStrategy implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            Expose annotation = f.getAnnotation(Expose.class);
            return !(annotation == null || annotation.deserialize());
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    private static Gson mGson;

    public static Gson getGson() {
        if (mGson == null) {
            mGson = new GsonBuilder()
                    .addSerializationExclusionStrategy(new SerializeExclusionStrategy())
                    .addDeserializationExclusionStrategy(new DeserializeExclusionStrategy())
                    .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(Long.class, new LongDefaultAdapter())
                    .registerTypeAdapter(long.class, new LongDefaultAdapter())
                    .registerTypeAdapter(Float.class, new FloatDefaultAdapter())
                    .registerTypeAdapter(float.class, new FloatDefaultAdapter())
                    .create();
        }
        return mGson;
    }
}