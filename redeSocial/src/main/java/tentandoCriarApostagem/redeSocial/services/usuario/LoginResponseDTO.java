package tentandoCriarApostagem.redeSocial.services.usuario;

public class LoginResponseDTO {
    private String token;
    private Long id;
    private String nome;
    private String email;
    private String imagemPerfilUrl;

    // Construtores
    public LoginResponseDTO(String token, Long id, String nome, String email, String imagemPerfilUrl) {
        this.token = token;
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.imagemPerfilUrl = imagemPerfilUrl;
    }

    // Getters e Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagemPerfilUrl() {
        return imagemPerfilUrl;
    }

    public void setImagemPerfilUrl(String imagemPerfilUrl) {
        this.imagemPerfilUrl = imagemPerfilUrl;
    }
}

