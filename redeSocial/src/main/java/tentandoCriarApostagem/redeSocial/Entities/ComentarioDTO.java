package tentandoCriarApostagem.redeSocial.Entities;

public class ComentarioDTO {
    private Long id;
    private String texto;
    private Usuario usuario;

    public ComentarioDTO(Long id, String texto, Usuario usuario) {
        this.id = id;
        this.texto = texto;
        this.usuario = usuario;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

