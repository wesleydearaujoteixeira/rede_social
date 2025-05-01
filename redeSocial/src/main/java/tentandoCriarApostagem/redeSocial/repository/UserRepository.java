package tentandoCriarApostagem.redeSocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tentandoCriarApostagem.redeSocial.Entities.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.id <> :userId AND u.id NOT IN (" +
            "SELECT f.seguido.id FROM Follow f WHERE f.seguidor.id = :userId)")
    List<Usuario> findUsuariosNaoSeguidos(@Param("userId") Long userId);

}
