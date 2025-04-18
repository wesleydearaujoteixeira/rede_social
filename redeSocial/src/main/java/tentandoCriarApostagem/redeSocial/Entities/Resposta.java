package tentandoCriarApostagem.redeSocial.Entities;

import jakarta.persistence.*;

@Entity
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;

    // Relacionamento de muitos para um (Várias respostas pertencem a um comentário)
    @ManyToOne
    @JoinColumn(name = "comentario_id")
    private Comentario comentario;

    // Relacionamento de muitos para um (Várias respostas pertencem a um usuário)
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

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

    public Comentario getComentario() {
        return comentario;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }

    // Getters and Setters
}

