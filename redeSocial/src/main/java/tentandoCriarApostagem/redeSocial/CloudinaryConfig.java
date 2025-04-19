package tentandoCriarApostagem.redeSocial;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", "daigkrxng",
                "api_key", "535398233871288",
                "api_secret", "geAt9QzCTilFkF6Ffa3G39vyVLo"
        );
        return new Cloudinary(config);
    }
}
