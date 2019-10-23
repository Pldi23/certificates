package com.epam.esm.util;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * message translator
 *
 * @author Dzmitry Platonov on 2019-10-21.
 * @version 0.0.1
 */
@Component
public class Translator {

    private static ResourceBundleMessageSource messageSource;

    private Translator(ResourceBundleMessageSource messageSource) {
        Translator.messageSource = messageSource;
    }

    public static String toLocale(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, null, locale);
    }
}
