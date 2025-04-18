package tentandoCriarApostagem.redeSocial.services.resposta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tentandoCriarApostagem.redeSocial.Entities.Comentario;
import tentandoCriarApostagem.redeSocial.Entities.Resposta;
import tentandoCriarApostagem.redeSocial.Entities.RespostasDTO;
import tentandoCriarApostagem.redeSocial.Entities.Usuario;
import tentandoCriarApostagem.redeSocial.repository.CometarioRepository;
import tentandoCriarApostagem.redeSocial.repository.RespostaRepository;
import tentandoCriarApostagem.redeSocial.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RespostaService {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private CometarioRepository comentarioRepository;

    @Autowired
    private UserRepository usuarioRepository;

    @Transactional
    public Resposta criarResposta(Resposta resposta, Long usuarioId, Long comentarioId) {
        // Buscar o comentário e o usuário
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Associando a resposta ao comentário e ao usuário
        resposta.setComentario(comentario);
        resposta.setUsuario(usuario);

        // Salvando a resposta no banco de dados
        return respostaRepository.save(resposta);
    }



    public List<RespostasDTO> listarRespostasPorComentario(Long comentarioId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

        List<Resposta> respostas = respostaRepository.findByComentario(comentario);

        return respostas.stream()
                .map(RespostasDTO::new)
                .collect(Collectors.toList());
    }


}

