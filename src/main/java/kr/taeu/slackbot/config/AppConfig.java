package kr.taeu.slackbot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.core.convert.converter.*;

import kr.taeu.slackbot.model.LineBotGroup;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        Converter<String, LineBotGroup> converter = n -> LineBotGroup.valueOf(n);
        registry.addConverter(converter);
    }
}
