import org.xml.sax.InputSource;

import java.io.StringReader;

public class Main {
    private String urlStr;
    private String responseStr;
    public static void main(String[] args) {
        System.out.println("Hello World!"); // Display the string.
    }

    private void sendRequestPrepare() {
        try {
            responseStr = sendRequest();
            //responseInpSrc = this.stringToInputSource(responseStr);
            //this.sprs = new SAXParser(this.responseInpSrc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String sendRequest(){
            String retStr = "";
            try{

                SMSTrafficSend jws = new SMSTrafficSend(urlStr);
                //jws.showDebugInformation();
                jws.setUseTSL();
                jws.postData();
                retStr = jws.getResponse();
            }catch(Exception e){
                e.printStackTrace();
            }
            return retStr;
    }

}
