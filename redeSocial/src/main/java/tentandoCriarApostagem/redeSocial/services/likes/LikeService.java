package tentandoCriarApostagem.redeSocial.services.likes;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tentandoCriarApostagem.redeSocial.Entities.Like;
import tentandoCriarApostagem.redeSocial.Entities.Post;
import tentandoCriarApostagem.redeSocial.Entities.Usuario;
import tentandoCriarApostagem.redeSocial.repository.LikeRepository;
import tentandoCriarApostagem.redeSocial.repository.PostRepository;
import tentandoCriarApostagem.redeSocial.repository.UserRepository;

import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository usuarioRepository;



    public boolean darLike(Long postId, Long usuarioId) {
        Optional<Like> likeExistente = likeRepository.findByPostIdAndUsuarioId(postId, usuarioId);

        if (likeExistente.isPresent()) {
            // Já curtiu — vamos remover (deslike)
            likeRepository.delete(likeExistente.get());
            return false; // Retorna false indicando que foi um deslike
        }

        // Ainda não curtiu — vamos adicionar like
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Like novoLike = new Like();
        novoLike.setPost(post);
        novoLike.setUsuario(usuario);

        likeRepository.save(novoLike);
        return true; // Retorna true indicando que foi um like
    }

}


