package com.wmall.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final String TAG = "SDK_Sample.Util";
    public static String POST = "POST";
    public static String GET = "GET";

    /**
     * 返回String类型的字符串
     *
     * @param url 请求路径
     * @return
     */
    public static String httpGetJs(final String url) {
        byte[] result = httpGet(url);
        if (null != result) {
            try {
                return new String(result, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] httpGet(final String url) {
        if (url == null || url.length() == 0) {
            return null;
        }

        HttpClient httpClient = getNewHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse resp = httpClient.execute(httpGet);
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }

            return EntityUtils.toByteArray(resp.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String httpPostJs(String url, String reqJson) {
        byte[] result = httpPost(url, reqJson);
        if (null != result) {
            try {
                return new String(result, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] httpPost(String url, String entity) {
        if (url == null || url.length() == 0) {
            return null;
        }
        HttpClient httpClient = getNewHttpClient();

        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new StringEntity(entity));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse resp = httpClient.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }

            return EntityUtils.toByteArray(resp.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class SSLSocketFactoryEx extends SSLSocketFactory {

        SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryEx(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType)
                        throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType)
                        throws java.security.cert.CertificateException {
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host,
                    port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    private static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        if (offset < 0) {
            return null;
        }
        if (len <= 0) {
            return null;
        }
        if (offset + len > (int) file.length()) {
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len]; // ´´½¨ºÏÊÊÎÄ¼þ´óÐ¡µÄÊý×é
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static String sha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes());

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String> stringsToList(final String[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        final List<String> result = new ArrayList<String>();
        for (int i = 0; i < src.length; i++) {
            result.add(src[i]);
        }
        return result;
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        String resultJs = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new SSLTrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            javax.net.ssl.SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(null, requestUrl, new sun.net.www.protocol.https.Handler());
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            resultJs = buffer.toString();
        } catch (ConnectException ce) {
            logger.error("Weixin server connection timed out.");
        } catch (Exception e) {
            logger.error("https request error:{}", e);
        }
        return resultJs;
    }
}
