package lucasjc.ftl2pdf;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class FTLProcessor {

    private Configuration config;

    public FTLProcessor(@Value(("${templates.path:}")) String location) throws IOException {
        TemplateLoader loader = null;

        // use a specific filesystem location or default to classpath for templates loading
        if (!ObjectUtils.isEmpty(location)) {
            loader = new FileTemplateLoader(new File(location));
        } else {
            loader = new ClassTemplateLoader(getClass(), "/templates");
        }

        config = new Configuration((Configuration.VERSION_2_3_31));
        config.setDefaultEncoding("UTF-8");
        config.setTemplateLoader(loader);
    }

    /**
     * @param templateName template name
     * @param params map of parameters to use inside the template
     * @return procesing result
     * @throws IOException
     * @throws TemplateException
     */
    public String process(String templateName, Map<String, Object> params)
            throws IOException, TemplateException {
        Template template = config.getTemplate(templateName);
        try (StringWriter writer = new StringWriter()) {
            template.process(params, writer);
            return writer.toString();
        }
    }
}
