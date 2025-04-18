package tentandoCriarApostagem.redeSocial.webImagem;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve arquivos da pasta 'uploads' para o navegador
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}

