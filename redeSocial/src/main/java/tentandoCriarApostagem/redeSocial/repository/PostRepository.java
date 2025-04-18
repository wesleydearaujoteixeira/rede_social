package tentandoCriarApostagem.redeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tentandoCriarApostagem.redeSocial.Entities.Post;


public interface PostRepository extends JpaRepository<Post, Long> {


}
