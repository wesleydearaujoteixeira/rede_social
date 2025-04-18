package tentandoCriarApostagem.redeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tentandoCriarApostagem.redeSocial.Entities.Comentario;
import tentandoCriarApostagem.redeSocial.Entities.Post;

import java.util.List;
import java.util.Optional;

public interface CometarioRepository extends JpaRepository<Comentario, Long> {
    // Método para buscar comentários de um post
    List<Comentario> findByPostId(Long postId);


}
