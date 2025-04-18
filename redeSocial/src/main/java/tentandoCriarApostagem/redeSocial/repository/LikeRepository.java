package tentandoCriarApostagem.redeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tentandoCriarApostagem.redeSocial.Entities.Like;
import tentandoCriarApostagem.redeSocial.Entities.Post;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostIdAndUsuarioId(Long postId, Long usuarioId);

}
