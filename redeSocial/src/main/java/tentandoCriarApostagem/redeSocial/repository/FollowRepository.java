package tentandoCriarApostagem.redeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tentandoCriarApostagem.redeSocial.Entities.Follow;
import tentandoCriarApostagem.redeSocial.Entities.Usuario;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsBySeguidorAndSeguido(Usuario seguidor, Usuario seguido);

    List<Follow> findBySeguidor(Usuario seguidor);

    List<Follow> findBySeguido(Usuario seguido);

    void deleteBySeguidorAndSeguido(Usuario seguidor, Usuario seguido);
}
