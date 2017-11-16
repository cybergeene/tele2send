import java.util.HashMap;
import java.util.Map;

/**
 * Класс, содержащий константы
 *
 * @author Evgeniy.Mednov
 * @since 25.01.2017
 *
 */
public final class SMSconsts {
    /** название представления, где смотрим доки на отправку СМС */
    public static final String viewToSend = 	"viewSMSToSend";
    /** название представления, где хранятся классификаторы для вычисления логина-пароля */
    public static final String viewAccount = 	"viewAccount";
    /** номер телефона */
    public static final String fldPhoneTo = 	"fldPhoneTo";
    /** текст смс */
    public static final String fldSMStext = 	"fldSMStext";
    /** текст смс в транслите */
    public static final String fldSMStextTranslit = "fldSMStextTranslit";
    /** логин */
    public static final String fldLogin = 		"fldLogin";
    /** пароль */
    public static final String fldPass = 		"fldPassword";
    /** поле аккаунта, по которому вычислим логин-пароль */
    public static final String fldAccount =		"fldAccount";
    /** название профайла базы */
    public static final String docProfile = 	"formSetupProfile";
    /** сервер СМС в виде https://{ip dmz сервера}/multi.php */
    public static final String fldSMSserver = 	"fldSMSserver";
    /** кодировка */
    public static final String charsetEnc = 	"windows-1251";
    /** id SMS, по которому можно вычислить статус СМС */
    public static final String fldSMSid = 		"fldSmsID";
    /** Новый, Приостановлен, Отправлен, Не отправлен, Доставлен, Не доставлен */
    public static final String fldDocStatus = 	"fldDocStatus";
    /** описание ошибки */
    public static final String fldErrorDescr =	"fldErrorDescr";
    /** код ошибки */
    public static final String fldErrorCode =	"fldErrorCode";
    /** если пустое значение, то рассылка СМС по данному аккаунту выключена */
    public static final String fldEnableSMS =	"fldEnableSMS";
    /** порог критического значения баланса */
    public static final String fldCriticalBalance = "fldCriticalBalance";
    /** поле значения баланса в документе аккаунта */
    public static final String fldBalance =		"fldBalance";
    /** дата последней проверки баланса */
    public static final String fldBalanceLastCheck = "fldBalanceLastCheck";
    /** снять галку в чекбоксе */
    public static final String disableFlag =	"";
    /** поднять галку в чекбоксе */
    public static final String enableFlag = 	"1";
    /** признак автоматического (из агента) выключения рассылки в документе аккаунта */
    public static final String fldDisableSMSautomatic = "fldDisableSMSautomatic";
    /** при автоматическом включении/выключении  рассылки в документе аккаунта поставим дату события в это поле */
    public static final String fldEnblDisblSMSLastModif = "fldEnblDisblSMSLastModif";
    /** формат даты, который прибегает из xml файла */
    public static final String xmlDatePattern =			"yyyy-MM-dd HH:mm:ss";
    public static final String errDescrNoAccount = 		"Невозможно определить бюджет, аккаунт не найден";
    public static final String errDescrSMSDisabled = 	"Рассылка SMS сообщений для данного аккаунта выключена";
    public static final String dateTagTemplate =		"_date";
    public static final String emptyString =	""; //извращаюсь

    /** название тэга в XML, по которому определяем статус доствавки смс */
    public static final String statusTag = 		"status";
    public static final int sendSMS = 1;
    public static final int checkStatus = 2;
    public static final int doNothing = 0;
    /** результат выполнения запроса положительный */
    public static final String okCode = "0";
    /** временные проблемы на стороне smstraffic.ru, ничего не делаем с документом */
    public static final String delayCode = "1000";
    /** закончились деньги - выключим в аккаунте галку "Рассылка СМС включена" */
    public static final String noMoneyCode = "415";

    /** перевод в глобальный статус документа */
    public static final Map docStatuses;
    static
    {
        docStatuses = new HashMap();
        docStatuses.put("New", "Новый"); 				//не применяется, но для полноты картины :)
        docStatuses.put("OnHold", "Приостановлен");		//если оключена отправка автоматически по причине нехватка средств на балансе, либо вручную
        docStatuses.put("Sended", "Отправлен"); 		//просто пишем, если получили xml без ошибки при отправке смс
        docStatuses.put("Buffered SMSC", "Отправлен"); 	//статус, получаемый от смс-сервиса
        docStatuses.put("Failed", "Не отправлен");		//при любой ошибке при откравке смс в обратном xml файле будем писать этот статус
        docStatuses.put("Delivered", "Доставлен");		//статус, получаемый от смс-сервиса
        docStatuses.put("Non Delivered","Не доставлен");//статус, получаемый от смс-сервиса
        docStatuses.put("Rejected","Не доставлен");		//статус, получаемый от смс-сервиса
        docStatuses.put("Expired","Не доставлен");		//статус, получаемый от смс-сервиса
        docStatuses.put("Deleted","Не доставлен");		//статус, получаемый от смс-сервиса
        docStatuses.put("Unknown status","Не доставлен");//статус, получаемый от смс-сервиса
    }

    public static final Map errorDescriptions;
    static
    {
        errorDescriptions = new HashMap();
        errorDescriptions.put("401", "Не указан логин");
        errorDescriptions.put("402", "Не указан пароль");
        errorDescriptions.put("403", "Не указаны номера телефонов");
        errorDescriptions.put("404", "Несовместимые параметры запроса");
        errorDescriptions.put("405", "Не указан текст сообщения");
        errorDescriptions.put("406", "wap push сообщение слишком длинное");
        errorDescriptions.put("407", "Не указан ни один телефон");
        errorDescriptions.put("408", "Неподдерживаемый тип сообщения");
        errorDescriptions.put("409", "Не указан udh");
        errorDescriptions.put("410", "Автоматическая разбивка бинарных сообщений не поддерживается");
        errorDescriptions.put("411", "Неверный логин или пароль");
        errorDescriptions.put("412", "Неверный IP");
        errorDescriptions.put("415", "Недостаточно средств");
        errorDescriptions.put("416", "Неверный формат даты начала рассылки");
        errorDescriptions.put("417", "Дата начала рассылки находится в прошлом");
        errorDescriptions.put("418", "Идентификаторы не предоставляются для отложенных сообщений");
        errorDescriptions.put("419", "Вам не разрешено использовать данный маршрут");
        errorDescriptions.put("420", "Сообщение слишком длинное");
        errorDescriptions.put("421", "Имя отправителя слишком длинное");
        errorDescriptions.put("424", "Сообщение слишком длинное");
        errorDescriptions.put("425", "Номер телефона слишком короткий. Ни одно сообщение не было отправлено");
        errorDescriptions.put("426", "Номер телефона слишком длинный. Ни одно сообщение не было отправлено");
        errorDescriptions.put("427", "Неверная длина номера телефона. Ни одно сообщение не было отправлено");
        errorDescriptions.put("428", "Неверный формат номера телефона. Ни одно сообщение не было отправлено");
        errorDescriptions.put("429", "Неподдерживаемый оператор. Ни одно сообщение не было отправлено");
        errorDescriptions.put("430", "Неверный номер телефона. Ни одно сообщение не было отправлено");
        errorDescriptions.put("431", "Телефон не подписан на рассылку. Ни одно сообщение не было отправлено");
        errorDescriptions.put("432", "Заблокированный номер телефона. Ни одно сообщение не было отправлено");
        errorDescriptions.put("433", "Не указан параметр sms_id");
        errorDescriptions.put("434", "Такого сообщения нет или оно вам не принадлежит");
        errorDescriptions.put("435", "Невозможно отменить сообщение");
        errorDescriptions.put("436", "Отправитель запрещен");
        errorDescriptions.put("437", "Сообщение превышает 160 символов после транслитерации");
        errorDescriptions.put("438", "В сообщении найден шаблон, но не задана ни одна группа");
        errorDescriptions.put("439", "Вы не можете отправлять SMS-сообщения через HTTP");
        errorDescriptions.put("440", "Параметр \"phones\" не задан или задан некорректно");
        errorDescriptions.put("441", "Неверный формат файла параметров");
        errorDescriptions.put("442", "Неверное число параметров");
        errorDescriptions.put("501", "Время окончания рассылки в прошлом");
        errorDescriptions.put("502", "Время начала рассылки больше времени окончания рассылки");
        errorDescriptions.put("1000", "Временные проблемы на сервере");

    }

    /** переопределим тег в поле, куда нужно записать полученное из тега значение */
    public static final Map xmlToFldMap;
    static
    {
        xmlToFldMap = new HashMap();
        xmlToFldMap.put("sms_id","fldSmsID");
        xmlToFldMap.put("submition_date","fldSubmissionDate");
        xmlToFldMap.put("send_date","fldSendDate");
        xmlToFldMap.put("last_status_change_date","fldLastStatusChangeDate");
        xmlToFldMap.put("status","fldSmsStatus");
        xmlToFldMap.put("error","fldStatusError");
    }

    /**
     *
     * @param tagName имя тэга из XML
     * @return имя поля, в которое нужно записать значение из тэга
     */
    public static String fromTagNameToFldName(String tagName){
        String ret = "";
        ret = (String) xmlToFldMap.get(tagName);
        return ret;
    }

    private SMSconsts(){} // чтобы нельзя было создать экземпляр класса по ошибке
}
