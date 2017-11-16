import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class StringToJDate {
    /**
     *
     * @param dateFromXML - значениен даты из xml
     * @return
     * @throws ParseException
     */
    public static Date convert(String dateFromXML) throws ParseException{
        Date dt;
        DateFormat formatter = new SimpleDateFormat(SMSconsts.xmlDatePattern); //дата такого вида - 2017-02-14 12:04:29
        dt = formatter.parse(dateFromXML);
        return dt;
    }

    private StringToJDate(){} //cannot create instance of class
}