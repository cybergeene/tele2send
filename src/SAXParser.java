/**
 * Created by Evgeniy.Mednov on 20.01.2017.
 */

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;

public class SAXParser{
    private String resultStr = "";
    private String descriptionStr = "";
    private String codeStr = "";
    private String accountStr = "";
    private HashMap phoneSMSid = new HashMap();
    private InputSource inSrc;
    private XMLReader parser = null;
    private HashMap smsIdStatus = new HashMap();

    public SAXParser(InputSource inSrc) throws SAXException, IOException {
        this.inSrc = inSrc;

        try {
            parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


    public void parseSMSid() throws SAXException, IOException {
        parser.setContentHandler(new smsIDHandler());
        parser.parse(this.inSrc);
    }

    public void parseStatus() throws SAXException, IOException {
        parser.setContentHandler(new smsStatusHandler());
        parser.parse(this.inSrc);
    }

    public void parseAccount() throws SAXException, IOException{
        parser.setContentHandler(new smsAccountHandler());
        parser.parse(this.inSrc);
    }

    /**
     *
     * @return либо OK, либо ERROR
     */
    public String getResult(){
        return  resultStr;
    }

    /**
     *
     * @return описание выполненного действия, либо описание ошибки (getResult = "ERROR")
     */
    public String getDescription(){
        String rusDescr = (String) SMSconsts.errorDescriptions.get(codeStr);
        if (rusDescr != null){
            return rusDescr;
        }else{
            return descriptionStr;
        }

    }

    /**
     *
     * @return словарь типа возвращенный номер телефона (отформатирован и приведен
     * к типу 79262220033) - возвращенный системой sms id
     */
    public HashMap getPhoneSMSid(){
        return phoneSMSid;
    }


    public HashMap getStatusParams(){
        return this.smsIdStatus;
    }

    /**
     *
     * @return код ошибки (если не равен "0")
     */
    public String getCode(){
        return codeStr;
    }

    /**
     *
     * @return сколько денег осталось на счете
     */
    public String getAccountBalance() {
        return accountStr;
    }

    class smsIDHandler extends DefaultHandler {
        private final boolean DEBUG = false;
        private boolean result = false;
        private boolean smsId = false;
        private boolean phone = false;
        private boolean code = false;
        private boolean description = false;
        private boolean message_info = false;
        private String phoneStr = "";
        private String smsIdStr = "";


        public void startElement(String nsURI, String localName,
                                 String rawName, Attributes attributes) throws SAXException{
            if (DEBUG) {
                System.out.println("startElement " + localName + ", " + rawName );
            }
            if (rawName.equalsIgnoreCase("result"))
                result = true;
            if (rawName.equalsIgnoreCase("sms_id"))
                smsId = true;
            if (rawName.equalsIgnoreCase("phone"))
                phone = true;
            if (rawName.equalsIgnoreCase("code"))
                code = true;
            if (rawName.equalsIgnoreCase("description"))
                description = true;
        }

        public void endElement(String nsURI, String localName,
                               String rawName) throws SAXException{
            if (rawName.equalsIgnoreCase("message_info"))
                message_info = true;
        }

        public void characters(char[] ch, int start, int length){
            if (smsId) {
                smsIdStr = new String(ch, start, length);
                smsIdStr = smsIdStr.trim();
                if (DEBUG)System.out.println("sms_id: " + smsIdStr);
                smsId = false;
            }else if (result) {
                resultStr = new String(ch, start, length);
                resultStr = resultStr.trim();
                if (DEBUG)System.out.println("result is "+ resultStr);
                result = false;
            }else if (phone) {
                phoneStr = new String(ch, start, length);
                phoneStr = phoneStr.trim();
                if (DEBUG) System.out.println("phone is " + phoneStr);
                phone = false;
            }else if(code) {
                codeStr = new String(ch, start, length);
                codeStr = codeStr.trim();
                code = false;
            }else if(description) {
                descriptionStr = new String(ch, start, length);
                descriptionStr = descriptionStr.trim();
                if (!descriptionStr.trim().equals(""))
                    if (DEBUG)System.out.println("description is \"" + descriptionStr + "\"");
                description = false;
            }else if(message_info) {
                phoneSMSid.put(phoneStr, smsIdStr);
                phoneStr = "";
                smsIdStr = "";
                message_info = false;

            }
        }
    }

    class smsStatusHandler extends DefaultHandler {
        private final boolean DEBUG = false;
        private boolean smsId = false;
        private boolean status = false;
        private boolean error = false;
        private boolean sms = false;
        private boolean send_date = false;
        private boolean submition_date = false;
        private boolean last_status_change_date = false;
        private String smsIdStr = "";
        private String statusStr = "";
        private String errStr = "";
        private String send_dateStr = "";
        private String submDateStr = "";
        private String lastStDateStr = "";

        public void startElement(String nsURI, String localName,
                                 String rawName, Attributes attributes) throws SAXException {
            if (DEBUG) {
                System.out.println("startElement " + localName + ", " + rawName);
            }
            if (rawName.equalsIgnoreCase("sms_id"))
                smsId = true;
            if (rawName.equalsIgnoreCase("status"))
                status = true;
            if (rawName.equalsIgnoreCase("error"))
                error = true;
            if (rawName.equalsIgnoreCase("send_date"))
                send_date = true;
            if (rawName.equalsIgnoreCase("submition_date"))
                submition_date = true;
            if (rawName.equalsIgnoreCase("last_status_change_date"))
                last_status_change_date = true;
        }

        public void endElement(String nsURI, String localName,
                               String rawName) throws SAXException {
            if (DEBUG) {
                System.out.println("endElement " + localName + ", " + rawName);
            }
            if (rawName.equalsIgnoreCase("sms")) {
                if (DEBUG) System.out.println("***sms passed");
                sms = true;
            }
            if (rawName.equalsIgnoreCase("reply")) {
                if (DEBUG) System.out.println("###reply passed");
                saveBlockIfNeed(); //для случая одноблочного xml
                sms = true;
            }
        }

        private void saveBlockIfNeed(){
            if(!smsIdStr.equals("")) {
                HashMap otherParamsStatus = new HashMap();
                if(!statusStr.equals(""))
                    otherParamsStatus.put("status", statusStr);
                if(!send_dateStr.equals(""))
                    otherParamsStatus.put("send_date", send_dateStr);
                if(!submDateStr.equals(""))
                    otherParamsStatus.put("submition_date", submDateStr);
                if(!lastStDateStr.equals(""))
                    otherParamsStatus.put("last_status_change_date", lastStDateStr);
                if(!errStr.equals(""))
                    otherParamsStatus.put("error", errStr);
                smsIdStatus.put(smsIdStr, otherParamsStatus);
            }
        }

        private void clearVars(){
            smsIdStr = "";
            statusStr = "";
            errStr = "";
            send_dateStr = "";
            submDateStr = "";
            lastStDateStr = "";
        }

        public void characters(char[] ch, int start, int length) {
            if (smsId) {
                smsIdStr = new String(ch, start, length);
                smsIdStr = smsIdStr.trim();
                if (DEBUG)System.out.println("sms_id: " + smsIdStr);
                smsId = false;
            } else if (status) {
                statusStr = new String(ch, start, length);
                statusStr = statusStr.trim();
                if (DEBUG)System.out.println("status is " + statusStr.toLowerCase());
                status = false;
            } else if (error) {
                errStr = new String(ch, start, length);
                errStr = errStr.trim();
                if (!errStr.equals(""))
                    if (DEBUG) System.out.println("error is \"" + errStr + "\"");
                error = false;
            }else if (send_date){
                send_dateStr = new String (ch, start, length);
                send_dateStr = send_dateStr.trim();
                if (DEBUG)System.out.println("send_date: " + send_dateStr);
                send_date = false;
            }else if (submition_date){
                submDateStr = new String (ch, start, length);
                submDateStr = submDateStr.trim();
                if (DEBUG)System.out.println("submition_date: " + submDateStr);
                submition_date = false;
            }else if (last_status_change_date){
                lastStDateStr = new String (ch, start, length);
                lastStDateStr = lastStDateStr.trim();
                if (DEBUG)System.out.println("last_status_change_date: " + lastStDateStr);
                last_status_change_date = false;
            }else if (sms){
                if (DEBUG) System.out.println("-----------------------------------------------------------------");
                saveBlockIfNeed();
                clearVars();

                sms = false;
            }
        }
    }

    class smsAccountHandler extends DefaultHandler{
        private final boolean DEBUG = false;
        private boolean account = false;
        private boolean result = false;
        private boolean code = false;
        private boolean description = false;

        public void startElement(String nsURI, String localName,
                                 String rawName, Attributes attributes) throws SAXException{
            if (DEBUG) {
                System.out.println("startElement " + localName + ", " + rawName );
            }
            if (rawName.equalsIgnoreCase("account"))
                account = true;
            if (rawName.equalsIgnoreCase("result"))
                result = true;
            if (rawName.equalsIgnoreCase("code"))
                code = true;
            if (rawName.equalsIgnoreCase("description"))
                description = true;
        }

        public void characters(char[] ch, int start, int length) {
            if (account){
                accountStr = new String(ch, start, length);
                accountStr = accountStr.trim();
                account = false;
            }else if (result) {
                resultStr = new String(ch, start, length);
                resultStr = resultStr.trim();
                if (DEBUG)System.out.println("result is "+ resultStr);
                result = false;
            }else if(code) {
                codeStr = new String(ch, start, length);
                codeStr = codeStr.trim();
                code = false;
            }else if(description) {
                descriptionStr = new String(ch, start, length);
                descriptionStr = descriptionStr.trim();
                if (!descriptionStr.equals(""))
                    if (DEBUG)System.out.println("description is \"" + descriptionStr + "\"");
                description = false;
            }
        }

    }
}

