package com.codinggyd.diff;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DateParser {
    private final List<DateFormat> dateFormats = new ArrayList<>();

    public DateParser() {
        //默认添加gson的序列化规则
        dateFormats.add(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            dateFormats.add(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
        }
        //添加常用的格式
        dateFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public void addDateFormat(DateFormat dateFormat) {
        dateFormats.add(dateFormat);
    }

    public Date deserializeToDate(String data) {
        for (DateFormat dateFormat : dateFormats) {
            try {
                return dateFormat.parse(data);
            } catch (ParseException ignored) {
            }
        }
        throw new RuleParseException("date formatter parse error,format no configuration. originText:" + data);
    }
}
