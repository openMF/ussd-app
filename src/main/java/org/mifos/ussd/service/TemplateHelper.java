package org.mifos.ussd.service;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TemplateHelper {

    private final StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

    private final Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);

    @PostConstruct
    public void initSmsService() {
        configuration.setTemplateLoader(stringTemplateLoader);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
    }

    public String render(String templateName, String templateText, Map<String, Object> params) {
        stringTemplateLoader.putTemplate(templateName, templateText);
        try {
            final Template template = configuration.getTemplate(templateName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
        } catch (IOException | TemplateException e) {
            log.error("Error while rendering templateName={}, template={}, params={}", templateName, templateText, params);
        } finally {
            stringTemplateLoader.removeTemplate(templateName);
        }
        return "";
    }
}
