package tentandoCriarApostagem.redeSocial.Entities;

public class UsuarioResumoDTO {
    private Long id;
    private String nome;
    private String imagemPerfilUrl;

    public UsuarioResumoDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.imagemPerfilUrl = usuario.getImagemPerfilUrl();
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
}

