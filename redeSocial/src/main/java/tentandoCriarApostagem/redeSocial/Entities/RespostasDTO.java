package tentandoCriarApostagem.redeSocial.Entities;

public class RespostasDTO {
    private Long id;
    private String texto;
    private UsuarioResumoDTO usuario;

    public RespostasDTO(Resposta resposta) {
        this.id = resposta.getId();
        this.texto = resposta.getTexto();
        this.usuario = new UsuarioResumoDTO(resposta.getUsuario());
    }

    // getters e setters


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

    public UsuarioResumoDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResumoDTO usuario) {
        this.usuario = usuario;
    }
}