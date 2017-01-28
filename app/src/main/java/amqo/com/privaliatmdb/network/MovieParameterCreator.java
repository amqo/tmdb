package amqo.com.privaliatmdb.network;

import java.util.Locale;
import java.util.Map;

public abstract class MovieParameterCreator {

    protected String getCountryCode() {
        String countryCode = Locale.getDefault().getCountry();
        String languageCode = Locale.getDefault().getLanguage();
       return  languageCode + "-" + countryCode;
    }

    public abstract Map<String, String> createParameters(int page);
}
