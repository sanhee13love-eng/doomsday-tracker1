package com.doomsday.tracker;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.Intent;
import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
  private WebView web;
  @Override public void onCreate(Bundle b) { super.onCreate(b);
    web = new WebView(this); setContentView(web);
    web.getSettings().setJavaScriptEnabled(true);
    web.getSettings().setDomStorageEnabled(true);
    web.addJavascriptInterface(new Bridge(), "Native");
    web.setWebViewClient(new WebViewClient());
    web.loadUrl("file:///android_asset/index.html");
  }
  class Bridge {
    @JavascriptInterface public void search(String query) {
      new Thread(() -> {
        try {
          JSONArray news = fetch(query);
          String payload = JSONObject.quote(news.toString());
          runOnUiThread(() -> web.evaluateJavascript("receiveNews("+payload+")",null));
        } catch(Exception e) {
          runOnUiThread(() -> web.evaluateJavascript("showError('뉴스를 가져오지 못했어요. 인터넷 연결을 확인해 주세요.')",null));
        }
      }).start();
    }
    @JavascriptInterface public void openLink(String link) {
      try { Uri u=Uri.parse(link); if (!"https".equals(u.getScheme())) return;
        runOnUiThread(() -> startActivity(new Intent(Intent.ACTION_VIEW,u)));
      } catch(Exception ignored) {}
    }
  }
  private JSONArray fetch(String query) throws Exception {
    // Google News Korean RSS. No private API keys or phone-hosted Python server required.
    String term = "어벤져스 둠스데이 "+query;
    String address="https://news.google.com/rss/search?q="+URLEncoder.encode(term,"UTF-8")+"&hl=ko&gl=KR&ceid=KR:ko";
    HttpURLConnection c=(HttpURLConnection)new URL(address).openConnection();
    c.setConnectTimeout(12000);c.setReadTimeout(12000);
    c.setRequestProperty("User-Agent","Mozilla/5.0 DoomsdayTracker/1.0");
    if(c.getResponseCode()!=200)throw new IOException("HTTP "+c.getResponseCode());
    XmlPullParser parser=Xml.newPullParser();
    parser.setInput(c.getInputStream(),"UTF-8");
    JSONArray out=new JSONArray();JSONObject item=null;String field="";
    for(int event=parser.getEventType();event!=XmlPullParser.END_DOCUMENT;event=parser.next()) {
      if(event==XmlPullParser.START_TAG) {
        String tag=parser.getName();
        if(tag.equals("item")) item=new JSONObject();
        if(item!=null&&(tag.equals("title")||tag.equals("link")||tag.equals("pubDate")||tag.equals("description")))field=tag;
      } else if(event==XmlPullParser.TEXT&&item!=null&&!field.isEmpty()) {
        item.put(field,item.optString(field)+parser.getText());
      } else if(event==XmlPullParser.END_TAG) {
        String tag=parser.getName();
        if(tag.equals("item")&&item!=null){out.put(item);item=null;if(out.length()>=35)break;}
        if(tag.equals(field))field="";
      }
    }
    c.disconnect();return out;
  }
  @Override public void onBackPressed(){if(web.canGoBack())web.goBack();else super.onBackPressed();}
}
