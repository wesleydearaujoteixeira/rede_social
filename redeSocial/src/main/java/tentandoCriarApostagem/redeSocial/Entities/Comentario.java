package tentandoCriarApostagem.redeSocial.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "comentario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas;


    private String texto;

    // Muitos comentários pertencem a um post
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // Muitos comentários pertencem a um usuário
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Construtores

    public Comentario() {}

    public Comentario(String texto, Post post, Usuario usuario) {
        this.texto = texto;
        this.post = post;
        this.usuario = usuario;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
