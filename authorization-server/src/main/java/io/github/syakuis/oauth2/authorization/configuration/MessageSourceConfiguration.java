package io.github.syakuis.oauth2.authorization.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.Locale;

@Configuration
public class MessageSourceConfiguration {
    @Bean("i18n")
    public MessageSourceAccessor i18n(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource, Locale.getDefault());
    }
}
