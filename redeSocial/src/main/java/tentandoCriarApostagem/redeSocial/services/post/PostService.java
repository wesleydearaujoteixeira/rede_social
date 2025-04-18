package tentandoCriarApostagem.redeSocial.services.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;
import tentandoCriarApostagem.redeSocial.Entities.Post;
import tentandoCriarApostagem.redeSocial.Entities.Usuario;
import tentandoCriarApostagem.redeSocial.repository.CometarioRepository;
import tentandoCriarApostagem.redeSocial.repository.LikeRepository;
import tentandoCriarApostagem.redeSocial.repository.PostRepository;
import tentandoCriarApostagem.redeSocial.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CometarioRepository comentarioRepository;


    @Transactional
    public Post criarPost(Post post, Long usuarioId, MultipartFile imagem) throws IOException {
        // Buscar o usuário que está criando o post
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Salvar a imagem se houver
        if (imagem != null && !imagem.isEmpty()) {
            String pastaUpload = "uploads/";
            String nomeArquivo = UUID.randomUUID() + "_" + imagem.getOriginalFilename();
            Path caminhoCompleto = Paths.get(pastaUpload, nomeArquivo);

            // Criação do diretório, caso não exista
            Files.createDirectories(caminhoCompleto.getParent());
            // Salvando o arquivo na pasta
            Files.write(caminhoCompleto, imagem.getBytes());

            // Atribuindo o caminho da imagem ao post
            post.setImagemUrl("http://localhost:8080" + "/uploads/" + nomeArquivo);
        }

        // Associando o usuário ao post
        post.setUsuario(usuario);

        // Salvando o post no banco de dados
        return postRepository.save(post);
    }


    public List<Post> getPostServices (Long usuarioId, Long postId) {

        Optional<Post> getUsersPost = postRepository.findById(postId);

        if(getUsersPost.isPresent()){

            Post postGet = getUsersPost.get();

            if(postGet.getUsuario().getId().equals(usuarioId)){
                return List.of(postGet);
            }


        }

        return List.of();


    }

    public List<Post> getALL () {
            return postRepository.findAll();
    }

    public Optional<Post> atualizarPost(Post postagemAtual, Long postId, Long usuarioId, MultipartFile novaImagem) throws IOException {
        Optional<Post> optionalPostExisting = postRepository.findById(postId);

        if (optionalPostExisting.isPresent()) {
            Post postExistente = optionalPostExisting.get();

            // Verifica se o post pertence ao usuário
            if (postExistente.getUsuario().getId().equals(usuarioId)) {
                postExistente.setTitulo(postagemAtual.getTitulo());
                postExistente.setConteudo(postagemAtual.getConteudo());

                // Se imagem nova foi enviada
                if (novaImagem != null && !novaImagem.isEmpty()) {
                    String pastaUpload = "uploads/";
                    String nomeArquivo = UUID.randomUUID() + "_" + novaImagem.getOriginalFilename();
                    Path caminhoCompleto = Paths.get(pastaUpload, nomeArquivo);

                    Files.createDirectories(caminhoCompleto.getParent());
                    Files.write(caminhoCompleto, novaImagem.getBytes());

                    postExistente.setImagemUrl("http://localhost:8080" + "/uploads/" + nomeArquivo);
                }

                return Optional.of(postRepository.save(postExistente));
            }
        }

        return Optional.empty();
    }


    public boolean deletePost (Long postId, Long usuarioId) {

        Optional<Post> postExisting = postRepository.findById(postId);

        if(postExisting.isPresent()){
            Post postDelete = postExisting.get();

            if(postDelete.getUsuario().getId().equals(usuarioId)){
                postRepository.deleteById(postId);
                return true;
            }

        }

        return false;



    }




}