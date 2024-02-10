package com.app.produce.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;

@Configuration
public class FeignConfig  implements FeignFormatterRegistrar {

    @Override
    public void registerFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter());
    }

    /*
     * SimpleDateFormat is not thread safe!
     * consider using joda time or java8 time instead
     */
    private static class DateFormatter implements Formatter<Date> {
        static final ThreadLocal<SimpleDateFormat> FORMAT = ThreadLocal.withInitial(
                () -> new SimpleDateFormat("yyyy-mm-dd HH:mm:ss")
        );

        @Override
        public Date parse(String text, Locale locale) throws ParseException {
            return FORMAT.get().parse(text);
        }

        @Override
        public String print(Date date, Locale locale) {
            return FORMAT.get().format(date);
        }
    }
}