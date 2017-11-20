package cn.czyugang.tcg.client.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;

/**
 * Created by wuzihong on 2016/9/11.
 * Json解析类，采用Gson解析
 */

public class JsonParse {
    private static Gson mGson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
//            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .registerTypeAdapter(JSONObject.class, new JSONObjectDeserializer())
            .create();

    public static <T> T fromJson(String json, java.lang.reflect.Type typeOfT) {
        return mGson.fromJson(json, typeOfT);
    }

    public static String toJson(Object o) {
        return mGson.toJson(o);
    }

    public static class Type implements ParameterizedType {
        private java.lang.reflect.Type raw;
        private java.lang.reflect.Type[] args;

        public Type(java.lang.reflect.Type raw, java.lang.reflect.Type... args) {
            this.raw = raw;
            this.args = args;
        }

        @Override
        public java.lang.reflect.Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public java.lang.reflect.Type getOwnerType() {
            return null;
        }

        @Override
        public java.lang.reflect.Type getRawType() {
            return raw;
        }
    }

    private static class JSONObjectDeserializer implements JsonSerializer<JSONObject>, JsonDeserializer {
        @Override
        public Object deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return new JSONObject(json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public JsonElement serialize(JSONObject src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            return new JsonParser().parse(src.toString());
        }
    }

//    private static class DateTypeAdapter extends TypeAdapter<Date> {
//        @Override
//        public void write(JsonWriter out, Date value) throws IOException {
//            out.value(DateFormat.format(value));
//        }
//
//        @Override
//        public Date read(JsonReader in) throws IOException {
//            if (in.peek() == JsonToken.NULL) {
//                in.nextNull();
//                return null;
//            }
//            return DateFormat.parse(in.nextString());
//        }
//    }
}
