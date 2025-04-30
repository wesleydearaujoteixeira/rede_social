package tentandoCriarApostagem.redeSocial.Entities;

public class UsuarioResumoDTO {
    private Long id;
    private String nome;
    private String imagemPerfilUrl;
    private String bio;
    private String background;
    private String link;


    public UsuarioResumoDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.imagemPerfilUrl = usuario.getImagemPerfilUrl();
        this.bio = usuario.getBio();
        this.background = usuario.getPerfilBackground();
        this.link = usuario.getLink();
    }

    // getters e setters


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

    public String getImagemPerfilUrl() {
        return imagemPerfilUrl;
    }

    public void setImagemPerfilUrl(String imagemPerfilUrl) {
        this.imagemPerfilUrl = imagemPerfilUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getLink() {
        return link;
    }
}

