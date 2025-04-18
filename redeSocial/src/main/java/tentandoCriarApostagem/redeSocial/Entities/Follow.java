package tentandoCriarApostagem.redeSocial.Entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuário que está seguindo
    @ManyToOne
    @JoinColumn(name = "seguidor_id")
    private Usuario seguidor;

    // Usuário que está sendo seguido
    @ManyToOne
    @JoinColumn(name = "seguido_id")
    private Usuario seguido;

    @Column(name = "data_follow", nullable = false)
    private LocalDateTime dataFollow = LocalDateTime.now();


    // Construtores, getters e setters
    public Follow() {
    }

    public Follow(Usuario seguidor, Usuario seguido) {
        this.seguidor = seguidor;
        this.seguido = seguido;
    }

    // Getters e setters omitidos para simplificar


    public Usuario getSeguidor() {
        return seguidor;
    }

    public void setSeguidor(Usuario seguidor) {
        this.seguidor = seguidor;
    }

    public Usuario getSeguido() {
        return seguido;
    }

    public void setSeguido(Usuario seguido) {
        this.seguido = seguido;
    }

    public LocalDateTime getDataFollow() {
        return dataFollow;
    }

    public void setDataFollow(LocalDateTime dataFollow) {
        this.dataFollow = dataFollow;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

