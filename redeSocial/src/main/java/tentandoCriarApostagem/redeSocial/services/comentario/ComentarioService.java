package tentandoCriarApostagem.redeSocial.services.comentario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tentandoCriarApostagem.redeSocial.Entities.*;
import tentandoCriarApostagem.redeSocial.repository.CometarioRepository;
import tentandoCriarApostagem.redeSocial.repository.NotificacaoRepository;
import tentandoCriarApostagem.redeSocial.repository.PostRepository;
import tentandoCriarApostagem.redeSocial.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    @Autowired
    private CometarioRepository cometarioRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    public Comentario criarComentario(Comentario comentario, Long usuarioId, Long postId) {
        // Buscar o post e o usuário
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Associando o comentário ao post e ao usuário
        comentario.setPost(post);
        comentario.setUsuario(usuario);

        // Salvando o comentário no banco de dados

        // Enviar notificação para o dono do post
        String postOwnerUsername = post.getUsuario().getUsername();  // Nome de usuário do dono do post
        String commenterUsername = usuario.getUsername();  // Nome de usuário do comentarista
        Comentario comentarioSaved = cometarioRepository.save(comentario);

        if (!usuario.getId().equals(post.getUsuario().getId())) { // evita notificação se o próprio dono comentar
            Notificacao notificacao = new Notificacao();
            notificacao.setDestinatario(post.getUsuario());
            notificacao.setMensagem(usuario.getNome() + " comentou no seu post. ");
            notificacao.setComentario(comentarioSaved);
            notificacaoRepository.save(notificacao);
        }


        return comentarioSaved;
    }


    public List<ComentarioDTO> getComentariosByPost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            return post.getComentarios().stream()
                    .map(c -> new ComentarioDTO(c.getId(), c.getTexto(), c.getUsuario()))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();


    }


    public Optional<Comentario> updateComentario(Comentario comentarioNew, Long comentarioId, Long usuarioId) {

        Optional<Comentario> comentarioOptional = cometarioRepository.findById(comentarioId);

        if (comentarioOptional.isPresent()) {
            Comentario comentarioExistente = comentarioOptional.get();

            // Verifica se o comentário pertence ao usuário que está tentando editar
            if (comentarioExistente.getUsuario().getId().equals(usuarioId)) {

                comentarioExistente.setTexto(comentarioNew.getTexto());

                return Optional.of(cometarioRepository.save(comentarioExistente));
            }
        }

        return Optional.empty();
    }


    public boolean deleteComentario (Long comentId, Long usuarioId) {

        Optional<Comentario> commentExisting = cometarioRepository.findById(comentId);

        if(commentExisting.isPresent()){
            Comentario comentDelete = commentExisting.get();

            if(comentDelete.getUsuario().getId().equals(usuarioId)){
                cometarioRepository.deleteById(comentId);
                return true;
            }

        }

        return false;



    }






}
