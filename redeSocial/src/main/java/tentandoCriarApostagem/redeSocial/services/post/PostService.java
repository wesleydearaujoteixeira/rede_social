package tentandoCriarApostagem.redeSocial.services.post;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;
import tentandoCriarApostagem.redeSocial.Entities.Post;
import tentandoCriarApostagem.redeSocial.Entities.Usuario;
import tentandoCriarApostagem.redeSocial.repository.CometarioRepository;
import tentandoCriarApostagem.redeSocial.repository.LikeRepository;
import tentandoCriarApostagem.redeSocial.repository.PostRepository;
import tentandoCriarApostagem.redeSocial.repository.UserRepository;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import java.io.IOException;
import java.nio.file.Files;


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

    @Autowired
    Cloudinary cloudinary;


    @Transactional
    public Post criarPost(Post post, Long usuarioId, MultipartFile imagem) throws IOException {
        // Buscar o usuário que está criando o post
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Salvar a imagem se houver
        if (imagem != null && !imagem.isEmpty()) {
            File arquivoTemporario = Files.createTempFile("upload", imagem.getOriginalFilename()).toFile();
            imagem.transferTo(arquivoTemporario);

            Map resultado = cloudinary.uploader().upload(arquivoTemporario, ObjectUtils.emptyMap());

            // Obter a URL da imagem hospedada
            String urlImagemCloudinary = (String) resultado.get("secure_url");

            // Setar a URL da imagem no banco
            post.setImagemUrl(urlImagemCloudinary);


            // Apagar o arquivo temporário
            arquivoTemporario.delete();
        }

        // Associando o usuário ao post
        post.setUsuario(usuario);

        // Salvando o post no banco de dados
        return postRepository.save(post);
    }

    public Post getPostById(Long postId) {
        // 1) Buscar o post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado com id: " + postId));
        return post;
    }


    public List<Post> getALL () {
            return postRepository.findAll();
    }

    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUsuarioId(userId);
    }


    public Optional<Post> atualizarPost(Post postagemAtual, Long postId, Long usuarioId, MultipartFile novaImagem) throws IOException {
        Optional<Post> optionalPostExisting = postRepository.findById(postId);

        if (optionalPostExisting.isPresent()) {
            Post postExistente = optionalPostExisting.get();

            // Verifica se o post pertence ao usuário
            if (!postExistente.getUsuario().getId().equals(usuarioId)) {
                throw new SecurityException("Você não tem permissão para atualizar este post.");
            }

            // Atualiza apenas se não for null
            if (postagemAtual.getTitulo() != null) {
                postExistente.setTitulo(postagemAtual.getTitulo());
            }

            if (postagemAtual.getConteudo() != null) {
                postExistente.setConteudo(postagemAtual.getConteudo());
            }

            // Atualiza imagem se houver uma nova
            if (novaImagem != null && !novaImagem.isEmpty()) {
                File arquivoTemporario = Files.createTempFile("upload", novaImagem.getOriginalFilename()).toFile();
                novaImagem.transferTo(arquivoTemporario);

                Map resultado = cloudinary.uploader().upload(arquivoTemporario, ObjectUtils.emptyMap());

                String urlImagemCloudinary = (String) resultado.get("secure_url");
                postExistente.setImagemUrl(urlImagemCloudinary);

                arquivoTemporario.delete();
            }

            return Optional.of(postRepository.save(postExistente));
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

    public List<Post> buscarPostsPorConteudo(String termo) {
        return postRepository.buscarPorConteudo(termo);
    }



}