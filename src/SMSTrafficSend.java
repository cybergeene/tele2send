import java.io.*;
import java.util.*;
import java.net.*;
import javax.net.ssl.*;


import sun.misc.BASE64Encoder;

public class SMSTrafficSend {

    private String urlStr ;
    private String Response = "";
    private int ResponseCode;
    private Properties customHeaders = new Properties();
    private boolean DEBUG = false;
    private boolean useSSL = true;
    private InputStream is;

    public SMSTrafficSend(String urlStr){
        this.urlStr = urlStr;
    }

    public static void setNoProxy() {
        System.setProperty("proxySet", "false");
        System.setProperty("http.proxyHost", "") ;
        System.setProperty("http.proxyPort",  "") ;
    };


    public void showDebugInformation(){
        DEBUG = true;
    }

    /**
     * В класс уже добавлены значения по умолчанию:
     * "Content-Type", "application/x-www-form-urlencoded"
     * @param key - ключ, например, Content-Type
     * @param value - значение
     */
    public void setCustomHeader(String key, String value) {
        customHeaders.setProperty(key, value);
    }; //setCustomHeader

    public static String setBASE64EncodeLoginPass (String codeString) {
        int string_size;
        String decodeString;
        string_size = codeString.length();

        if (string_size > 0){
            decodeString = (String) new BASE64Encoder().encodeBuffer(codeString.getBytes());
        } else {
            decodeString = "";
        }

        return (decodeString);
    }

    public void setUseSSL(){
        this.useSSL = true;
    }

    public void setUseTSL(){
        this.useSSL = false;
    }


    /**
     * попробуем выполнить запрос на сервер
     * @throws Exception
     */
    public void postData() throws Exception {

        BufferedReader rd=null;
        DataOutputStream wd=null;
        HttpURLConnection urlConn = null;


        if (urlStr.length() == 0) {return;};

        try {
            if (DEBUG) System.out.println("HTTP Con start trying ...");

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };
            /*
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true; }
            }; */


            // Install the all-trusting trust manager
            //HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            try {
                SSLContext sc;
                if (useSSL) {
                    sc = SSLContext.getInstance("SSL");
                }else{
                    sc = SSLContext.getInstance("TLS");
                }

                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) { }

            // Creating new URL Connection
            if (DEBUG) System.out.println("before creating url connection ...");
            URL url = new URL(urlStr);
            if (DEBUG) System.out.println("after creating url connection ...");
            urlConn = (HttpURLConnection)url.openConnection();


            //urlConn.setRequestMethod("POST");
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Setting custom headers
            for (Iterator it=customHeaders.keySet().iterator(); it.hasNext(); )
            {
                Object key = it.next();
                Object value = customHeaders.get(key);
                urlConn.setRequestProperty((String) key, (String) value);
                System.out.println("setting request property " + key + " = " + value);
            }

            // Writing data to Connection
            if (DEBUG) System.out.println("URL: "+url.getPath());
            if (DEBUG) System.out.println("URL to string: "+url.toString());
            if (DEBUG) System.out.println("java.proxy:" + System.getProperty("proxySet"));
            if (DEBUG) System.out.println("HTTP Con end writing to connection and start reading");
            if (DEBUG) System.out.println("java.version:" + System.getProperty("java.version"));


            // Getting response code from server
            ResponseCode = urlConn.getResponseCode();
            if (DEBUG) {
                System.out.println("ResponseCode: " + ResponseCode);
                System.out.println("proxy: " + urlConn.usingProxy());
            }

            if (ResponseCode <= 400) {
                // if response code is not error code - read response data from server
                if (DEBUG) System.out.println("Try to read urlConn inputStream");
                is = urlConn.getInputStream();
                if (DEBUG) System.out.print("after trying read urlConn");
            } else {
                // if server returns error response code
                is = urlConn.getErrorStream();
                // for java version older than 1.4 getErrorStream() returns null
                if (is == null) {
                    Response = "Exeption: server returns error code. Error message unavailable.";
                    return;
                }
            }

            // Read response
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[2097152];
            int bytesRead = 0;
            StringBuffer strBuf = new StringBuffer();
            while ((bytesRead = bis.read(buffer)) != -1) {
                strBuf.append(new String(buffer, 0, bytesRead, "UTF-8"));
            }
            Response = strBuf.toString();
            //Response = Response.replaceAll("><",">\n<");
            if (DEBUG) System.out.println("HTTP Con end while (length="+Response.length()+"), disconnect");
            urlConn.disconnect();

        } catch (Exception e) {
            Response = "Exception:" + e.getMessage();
            e.printStackTrace();
        } finally {
            // System.out.println("Finally");
            urlConn.disconnect();
            if (wd != null) wd.close();
            if (rd != null) rd.close();
        }

        if (DEBUG) System.out.println("HTTP Con returning:"+Response);
    }

    public String getResponse(){
        return Response;
    }
}
