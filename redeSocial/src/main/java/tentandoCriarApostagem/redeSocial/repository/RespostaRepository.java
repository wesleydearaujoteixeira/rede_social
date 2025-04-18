package tentandoCriarApostagem.redeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tentandoCriarApostagem.redeSocial.Entities.Comentario;
import tentandoCriarApostagem.redeSocial.Entities.Resposta;

import java.util.List;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {
    List<Resposta> findByComentario(Comentario comentario);
}

