package tentandoCriarApostagem.redeSocial.services.follow;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tentandoCriarApostagem.redeSocial.Entities.Follow;
import tentandoCriarApostagem.redeSocial.Entities.Usuario;
import tentandoCriarApostagem.redeSocial.Entities.UsuarioResumoDTO;
import tentandoCriarApostagem.redeSocial.repository.FollowRepository;
import tentandoCriarApostagem.redeSocial.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class FollowServices {

    @Autowired
    FollowRepository followRepository;

    @Autowired
    UserRepository usuarioRepository;

    public FollowServices(FollowRepository followRepository, UserRepository usuarioRepository) {
        this.followRepository = followRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void seguirUsuario(Long seguidorId, Long seguidoId) {
        if (seguidorId.equals(seguidoId)) {
            throw new IllegalArgumentException("Você não pode seguir a si mesmo");
        }

        Usuario seguidor = usuarioRepository.findById(seguidorId)
                .orElseThrow(() -> new RuntimeException("Seguidor não encontrado"));
        Usuario seguido = usuarioRepository.findById(seguidoId)
                .orElseThrow(() -> new RuntimeException("Usuário a ser seguido não encontrado"));

        if (followRepository.existsBySeguidorAndSeguido(seguidor, seguido)) {
            throw new RuntimeException("Você já está seguindo esse usuário");
        }

        Follow follow = new Follow(seguidor, seguido);
        followRepository.save(follow);
    }
    @Transactional
    public void deixarDeSeguir(Long seguidorId, Long seguidoId) {
        Usuario seguidor = usuarioRepository.findById(seguidorId)
                .orElseThrow(() -> new RuntimeException("Seguidor não encontrado"));
        Usuario seguido = usuarioRepository.findById(seguidoId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        followRepository.deleteBySeguidorAndSeguido(seguidor, seguido);
    }

    public List<UsuarioResumoDTO> listarSeguindo(Long seguidorId) {
        Usuario seguidor = usuarioRepository.findById(seguidorId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return followRepository.findBySeguidor(seguidor).stream()
                .map(f -> new UsuarioResumoDTO(f.getSeguido()))
                .collect(Collectors.toList());
    }

    public List<UsuarioResumoDTO> listarSeguidores(Long seguidoId) {
        Usuario seguido = usuarioRepository.findById(seguidoId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return followRepository.findBySeguido(seguido).stream()
                .map(f -> new UsuarioResumoDTO(f.getSeguidor()))
                .collect(Collectors.toList());
    }
}
