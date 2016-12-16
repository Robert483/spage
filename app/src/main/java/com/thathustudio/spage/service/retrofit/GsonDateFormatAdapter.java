package com.thathustudio.spage.service.retrofit;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GsonDateFormatAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private final DateFormat primaryDf;

    public GsonDateFormatAdapter() {
        primaryDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        primaryDf.setTimeZone(TimeZone.getDefault());
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return primaryDf.parse(json.getAsString());
        } catch (ParseException exPrimary) {
            return null;
        }
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(primaryDf.format(src));
    }
}
