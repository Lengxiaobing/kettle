package org.kettle.sxdata.util.task;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

/**
 * @description: 客户端http请求
 * @author: ZX
 * @date: 2018/11/20 14:25
 */
public class HttpClientUtil {
    private static ThreadSafeClientConnManager cm = null;

    static {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
                .getSocketFactory()));

        cm = new ThreadSafeClientConnManager(schemeRegistry);
        try {
            int maxTotal = 100;
            cm.setMaxTotal(maxTotal);
        } catch (NumberFormatException e) {
            System.err.println(
                    "Key[httpclient.max_total] Not Found in systemConfig.properties");
            e.printStackTrace();
        }

        // 每条通道的并发连接数设置（连接池）
        try {
            int defaultMaxConnection = 100;
            cm.setDefaultMaxPerRoute(defaultMaxConnection);
        } catch (NumberFormatException e) {
            System.err.println(
                    "Key[httpclient.max_total] Not Found in systemConfig.properties");
            e.printStackTrace();
        }
    }

    public static synchronized HttpClient getHttpClient() {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
        // 3000ms
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
        return new DefaultHttpClient(cm, params);
    }

    public static void release() {
        if (cm != null) {
            cm.shutdown();
        }
    }
}
