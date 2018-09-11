package com.wmall.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Description:
 * @Author: niexx <br>
 * @Date: 2017-11-23 21:01 <br>
 */
public class WeChatUtil {

    private static Logger log = LoggerFactory.getLogger(WeChatUtil.class);
    public static String POST = "POST";
    public static String GET = "GET";

    /**
     * 1.发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return String 返回String类型字符串
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        String resultJson = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new SSLTrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
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
            resultJson = buffer.toString();
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return resultJson;
    }

    /**
     * 微信上传临时文件
     *
     * @param requestUrl 微信上传临时素材的接口url
     * @param file       要上传的文件
     * @return String  上传成功后，微信服务器返回的消息
     * @desc ：微信上传素材的请求方法
     */
    public static String httpRequest(String requestUrl, File file) {
        StringBuffer buffer = new StringBuffer();

        try {
            //1.建立连接
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  //打开链接

            //1.1输入输出设置
            httpUrlConn.setDoInput(true);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setUseCaches(false); // post方式不能使用缓存
            //1.2设置请求头信息
            httpUrlConn.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("Charset", "UTF-8");
            //1.3设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            httpUrlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            // 请求正文信息
            // 第一部分：
            //2.将文件头输出到微信服务器
            StringBuilder sb = new StringBuilder();
            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length()
                    + "\";filename=\"" + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes("utf-8");
            // 获得输出流
            OutputStream outputStream = new DataOutputStream(httpUrlConn.getOutputStream());
            // 将表头写入输出流中：输出表头
            outputStream.write(head);

            //3.将文件正文部分输出到微信服务器
            // 把文件以流文件的方式 写入到微信服务器中
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                outputStream.write(bufferOut, 0, bytes);
            }
            in.close();
            //4.将结尾部分输出到微信服务器
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
            outputStream.write(foot);
            outputStream.flush();
            outputStream.close();


            //5.将微信服务器返回的输入流转换成字符串
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


        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 发起http请求获取返回结果
     *
     * @param requestUrl 请求地址
     * @return
     */
    public static String httpGet(String requestUrl) {
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(false);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();

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

        } catch (Exception e) {
        }
        return buffer.toString();
    }

    /**
     * 文件下载和文件保存
     *
     * @param getUrl  下载路径
     * @param saveUrl 保存路径
     * @return
     */
    public static boolean httpDownLoadFile(String getUrl, String saveUrl) {
        boolean uploadResult = true;
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(getUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // 设置网络连接超时时间
            httpURLConnection.setConnectTimeout(3000);
            // 设置应用程序要从网络连接读取数据
            httpURLConnection.setDoInput(true);

            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                // 从服务器返回一个输入流
                String fileName = getHttpFileName(httpURLConnection.getHeaderField("Content-Disposition"));
                log.info("HttpResponse fileName {}", fileName);
                inputStream = httpURLConnection.getInputStream();
                byte[] data = new byte[2048];
                int len = 0;
                FileOutputStream fileOutputStream = new FileOutputStream(saveUrl);
                while ((len = inputStream.read(data)) != -1) {
                    fileOutputStream.write(data, 0, len);
                }
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            uploadResult = false;
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            uploadResult = false;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadResult;
    }

    public static String getHttpFileName(String contentDesc) throws Exception {
        String fileName = null;
        if (StringUtils.isBlank(contentDesc)) throw new Exception("HttpResponse fileName not exist!");
        System.out.println(contentDesc);
        int markLen = contentDesc.indexOf("filename=\"") + "filename=\"".length();
        fileName = contentDesc.substring(markLen, contentDesc.length()-1);
        return fileName;
    }


    public static void main(String[] args) {
        String mediaUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=99r8iIGGfttjAFnF52SZosx0DADZMjQtJGFEFq1G5iNu5M8_hQZRjgem3gz0ar5iklcjaKp1YSi8o_LtFkB73G66W6maykqrsxEbDEN31a__jVt91gO983nrQfJ3b2oPPl1XubNF-DjQ2vhyIBkIJqPUrufqIoEIOEqt1bKk4HcC_n3If1cjIxxKUhWMHszss2SVesMZp6unsB7VBNOU9w&media_id=1c5Zl6q13k_3vy4GhHaU9YqAzF7Ya-614pYS_aD4aQUbS-nZr2urRu2CP-ol0JOqn";
        WeChatUtil.httpDownLoadFile(mediaUrl, "D:\\tempFile\\downloadtest");
    }
}
