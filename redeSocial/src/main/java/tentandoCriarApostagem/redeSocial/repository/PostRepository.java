package tentandoCriarApostagem.redeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tentandoCriarApostagem.redeSocial.Entities.Post;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUsuarioId(@Param("userId") Long userId);


    @Query("SELECT p FROM Post p WHERE LOWER(p.conteudo) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Post> buscarPorConteudo(@Param("termo") String termo);


}
