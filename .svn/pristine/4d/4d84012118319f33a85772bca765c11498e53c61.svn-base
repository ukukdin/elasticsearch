package nurier.wave.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Pattern;

public class WaveService {
    
    private static URL url;
    private static final int DEFAULT_CONNECT_TIMEOUT = 1000;
    private static final int DEFAULT_READ_TIMEOUT = 1000;
    private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
    
    private Pattern regex;
    private Pattern exclusionRegex;
    
    static {
        try {
            String requestUrl = "http://112.170.121.80:8080/";
            url = new URL(requestUrl);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public WaveService() {
    }

    public WaveService(String regex, String exclusionRegex)
    {
        //setProperties();
        
        if ((regex != null) && (regex.length() > 0)) {
          this.regex = Pattern.compile(regex);
        }
        if ((exclusionRegex != null) && (exclusionRegex.length() > 0)) {
          this.exclusionRegex = Pattern.compile(exclusionRegex);
        }
    }

    public String submitServletDataSync(String submitString) {
        String result = "";
        
        HttpURLConnection conn = null;
        OutputStreamWriter wr = null;
        BufferedReader br = null;
        
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
            conn.setReadTimeout(DEFAULT_READ_TIMEOUT);
            conn.setRequestMethod("POST");
            //conn.setRequestMethod("GET"); 
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            wr.write("" + submitString);
            wr.flush();
            
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
            String t ="";
            while(  (t = br.readLine()) != null ) {
                if ( !"null".equals(t)) {
                    result += t;
                }
            }
        } catch (Exception e){
            result = "Connection refused";
            e.printStackTrace();
        } finally {
            if (wr != null ) {
                try {
                    wr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br != null ) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null ) {
                conn.disconnect();
            }
        }
        return result;
    }

    public boolean isRegexMatched(String uri) {
        if (uri == null) {
            return false;
        }
        if ((this.exclusionRegex != null) && (this.exclusionRegex.matcher(uri).find())) {
            return false;
        }
        if (this.regex != null) {
            return this.regex.matcher(uri).find();
        }
        return true;
    }
    
    public static void main(String[] args) {
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36";
        
        WaveEvent event = new WaveEvent();
        
        event.put("protocol"      , "http"                                                                                                                             );
        event.put("host"          , "192.168.0.201"                                                                                                                    );
        event.put("userAgent"     , userAgent                                                                                                                          );
        event.put("clientID"      , WaveUtils.newClientID()                                                                                                            );
        event.put("uri"           , "/servlet/nfds/callcenter/search.fds"                                                                                              );
        event.put("url"           , "/servlet/nfds/callcenter/search.fds"                                                                                              );
        event.put("ip"            , WaveUtils.newIP()                                                                                                                  );
        event.put("uuid"          , UUID.randomUUID().toString()                                                                                                       );
        event.put("cookie"        , "cookies.js=1; _npas=03bc59a641cbbc2d7efa0b40ec8509f824d1e8f252a1631c3bb6297937780207; JSESSIONID=97C68CB271410F074E8D1E15F8CDB6D7");
        event.put("time"          , WaveUtils.getNowDateTimeMs()                                                                                                       );
        event.put("serverHostname", "192.168.40.201:10080"                                                                                                             );
        event.put("resCode"       , "200"                                                                                                                              );
        event.put("method"        , "GET"                                                                                                                              );
        event.put("accept"        , "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"     );
        event.put("acceptEncoding", "gzip, deflate"                                                                                                                    );
        event.put("acceptLanguage", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7"                                                                                              );
        event.put("referer"       , "http://192.168.40.201:10080/servlet/nfds/callcenter/search.fds"                                                                   );

        System.out.println(event.getJsonString());
        
        WaveService wave = new WaveService();
        System.out.println(wave.submitServletDataSync(event.getJsonString()));
    }
    
}