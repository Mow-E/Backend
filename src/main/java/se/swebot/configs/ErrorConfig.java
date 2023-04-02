package se.swebot.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Configuration
public class ErrorConfig {
    @Bean(name = {"error"})
    public View errorView() {
        return new AbstractView() {
            @Override
            protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                if (response.isCommitted()) {
                    logger.error(model.toString());
                } else {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    StringBuilder builder = new StringBuilder();
                    Object timestamp = model.get("timestamp");
                    Object message = model.get("message");
                    Object trace = model.get("trace");
                    if (response.getContentType() == null) {
                        response.setContentType(this.getContentType());
                    }

                    builder.append("{\n\"timestamp\":\"").append(timestamp).append("\",\n")
                            .append("\"error\":\"").append(HtmlUtils.htmlEscape((String) model.getOrDefault("error", ""))).append("\",\n")
                            .append("\"status\":\"").append(model.getOrDefault("status", "")).append("\"");
                    if (message != null) {
                        builder.append(",\n\"message\":\"").append(HtmlUtils.htmlEscape((String) message)).append("\"");
                    }

                    builder.append("\n}");
                    response.getWriter().append(builder.toString());
                }
            }
        };
    }
}
