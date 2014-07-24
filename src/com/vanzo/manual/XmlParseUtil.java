package com.vanzo.manual;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

public class XmlParseUtil {

    public static List<DataEntity> getDatas(InputStream inputStream)
            throws Exception {
        List<DataEntity> datas = null;
        DataEntity data = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        Log.e("hw", ">>>>>>>>>>>>>>>>>>");
        int event = parser.getEventType();
        Log.e("hw", ">>>>>>>>>>>>>>>>>>");
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
            case XmlPullParser.START_DOCUMENT:
                Log.e("hw", ">>>>>>>>>11>>>>>>>>>");
                datas = new ArrayList<DataEntity>();
                break;
            case XmlPullParser.START_TAG:
                Log.e("hw", ">>>>>>>>>22>>>>>>>>>" + parser.getName());
                if ("kind".equals(parser.getName())) {
                    data = new DataEntity();
                    data.setId(Integer.parseInt(parser.getAttributeValue(0)));
                }
                if (data != null) {
                    if ("name".equals(parser.getName())) {
                        data.setName(parser.nextText());
                    } else if ("page".equals(parser.getName())) {
                        data.setPage(Integer.parseInt(parser.nextText()));
                    }
                }
                break;
            case XmlPullParser.END_TAG:
                if ("kind".equals(parser.getName())) {
                    datas.add(data);
                    data = null;
                }
                break;
            }
            event = parser.next();
        }
        return datas;
    }
}
