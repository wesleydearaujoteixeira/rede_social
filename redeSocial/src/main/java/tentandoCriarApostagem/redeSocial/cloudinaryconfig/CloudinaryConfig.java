package tentandoCriarApostagem.redeSocial.cloudinaryconfig;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "wesley",
                "api_key", "476122445779419",
                "api_secret", "QGua9Ex2IhLHQjpMLCez6Qn_PEk"));
    }
}