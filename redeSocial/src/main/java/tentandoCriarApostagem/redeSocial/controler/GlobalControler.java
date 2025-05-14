package tentandoCriarApostagem.redeSocial.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tentandoCriarApostagem.redeSocial.Entities.*;
import tentandoCriarApostagem.redeSocial.repository.NotificacaoRepository;
import tentandoCriarApostagem.redeSocial.repository.PostRepository;
import tentandoCriarApostagem.redeSocial.security.JwtService;
import tentandoCriarApostagem.redeSocial.services.comentario.ComentarioService;
import tentandoCriarApostagem.redeSocial.services.follow.FollowServices;
import tentandoCriarApostagem.redeSocial.services.likes.LikeService;
import tentandoCriarApostagem.redeSocial.services.post.PostService;
import tentandoCriarApostagem.redeSocial.services.resposta.RespostaService;
import tentandoCriarApostagem.redeSocial.services.usuario.LoginResponseDTO;
import tentandoCriarApostagem.redeSocial.services.usuario.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/redes")
@CrossOrigin(origins = "social-azure-seven.vercel.app")
public class GlobalControler {

    @Autowired
    private PostService postService;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private RespostaService respostaService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FollowServices followService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAuser (@PathVariable Long id) {
        Usuario user = userService.getUsuarioPorId(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<String> criarUsuarioComImagemSimples(
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("senha") String senha,
            @RequestParam("imagem") MultipartFile imagem) {

        try {

            userService.criarUsuarioComImagemSimples(nome, email, senha, imagem);
            return ResponseEntity.ok("Usuário criado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    // login

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario user) {
        // Validação simples de entrada
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getSenha() == null || user.getSenha().isEmpty()) {
            return ResponseEntity.badRequest().body("Email e senha são obrigatórios.");
        }

        try {
            // Tenta autenticar com as credenciais fornecidas
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            Usuario authenticatedUser = (Usuario) auth.getPrincipal();

            // Gera o token JWT se a autenticação for bem-sucedida
            String token = jwtService.generateToken((UserDetails) auth.getPrincipal());

            // Retorna o token JWT no corpo da resposta

            LoginResponseDTO data = new LoginResponseDTO(
                    token,
                    authenticatedUser.getId(),
                    authenticatedUser.getNome(),
                    authenticatedUser.getEmail(),
                    authenticatedUser.getImagemPerfilUrl());

            return ResponseEntity.ok(data);
        } catch (AuthenticationException e) {
            // Exceção de autenticação - usuário ou senha inválidos
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos.");
        } catch (Exception e) {
            // Captura de outras exceções
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }



    @PatchMapping("/update/{id}")
    public ResponseEntity<String> atualizarUsuario(
            @PathVariable Long id,
            @RequestParam  String nome,
            @RequestParam  String bio,
            @RequestParam  String link,
            @RequestParam (required = false) MultipartFile imagem,
            @RequestParam (required = false) MultipartFile perfilBackground
            ) {
        try {


            userService.atualizarUsuarioComImagemSimples(id, nome, bio, link,  imagem, perfilBackground);


            return ResponseEntity.ok("Usuário atualizado com sucesso.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao atualizar imagem: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("usuarios")
    public List<Usuario> getALLusers () {
        return userService.getUsers();
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deletando(@PathVariable Long id) {
        boolean user = userService.deleteUser(id);

        if(user) {
            return ResponseEntity.ok("Usuário deletado com sucesso " + id);
        }else{
            return ResponseEntity.badRequest().body("Usuário não encontrado ");
        }


    }

    @GetMapping("/search/{query}")
    public ResponseEntity<List<Post>> pesquisarPostagens(@PathVariable String query) {
        List<Post> posts = postService.buscarPostsPorConteudo(query);
        return ResponseEntity.ok(posts);
    }



    // Endpoint para criar um novo post
    @PostMapping("/post/create")
    public ResponseEntity<Post> criarPostComImagem(
            @RequestParam("titulo") String titulo,
            @RequestParam("conteudo") String texto,
            @RequestParam("usuarioId") Long usuarioId,
            @RequestParam("imagem") MultipartFile imagem) throws IOException {

        // Criação do post com a imagem
        Post post = new Post();
        post.setTitulo(titulo);
        post.setConteudo(texto);

        // Criando o post e associando ao usuário
        Post postCriado = postService.criarPost(post, usuarioId, imagem);

        return ResponseEntity.ok(postCriado);
    }


    @PutMapping("/post/atualizar/{postId}")
    public ResponseEntity<?> atualizarPost(
            @PathVariable Long postId,
            @RequestParam("titulo") String titulo,
            @RequestParam("conteudo") String conteudo,
            @RequestParam("usuarioId") Long usuarioId,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) {

        try {
            Post postParaAtualizar = new Post();
            postParaAtualizar.setTitulo(titulo);
            postParaAtualizar.setConteudo(conteudo);

            Optional<Post> postAtualizado = postService.atualizarPost(postParaAtualizar, postId, usuarioId, imagem);

            return postAtualizado
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.badRequest().build());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar o post: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/post/{postId}/{usuarioId}")
    public ResponseEntity<?> deletePostagem (@PathVariable Long postId, @PathVariable Long usuarioId) {
        boolean existingPost = postService.deletePost(postId, usuarioId);

        if(existingPost){
            return ResponseEntity.ok("Deletando postagem com o id " + postId);
        }
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Postagem não encontrada");

    }


    @GetMapping("/get/{postId}")
    public ResponseEntity<Post> getPost(
            @PathVariable Long postId
    ) {
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/all")
    public List<Post> todosPosts () {
        return postService.getALL();

    }

    @GetMapping("/posts/usuario/{userId}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }


    @PostMapping("/comentario/{postId}/{usuarioId}")
    public ResponseEntity<?> comentar(@RequestBody Comentario comentarioRes,  @PathVariable Long postId, @PathVariable Long usuarioId){

        Comentario comentario = comentarioService.criarComentario(comentarioRes, postId, usuarioId);

        if(comentario != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Comentário adicionado com sucesso");

        }
        return ResponseEntity.badRequest().build();

    }

    @GetMapping("/comentario/{postId}")

    public List<ComentarioDTO> getComentarios (@PathVariable Long postId) {
        var comentario = comentarioService.getComentariosByPost(postId);

        if(comentario != null ){
            return comentario;
        }

        return List.of();


    }


    @PutMapping("/updated/comment/{comentarioId}/{usuarioId}")
    public ResponseEntity<?> updateComentario(
            @RequestBody Comentario comentario,
            @PathVariable Long comentarioId,
            @PathVariable Long usuarioId) {

        Optional<Comentario> comentarioAtualizado = comentarioService.updateComentario(comentario, comentarioId, usuarioId);

        if (comentarioAtualizado.isPresent()) {
            return ResponseEntity.ok(comentarioAtualizado.get());
        } else {
            return ResponseEntity.badRequest().body("Comentário não encontrado ou usuário não autorizado.");
        }
    }


    @DeleteMapping("/delete/comentario/{comentId}/{usuarioId}")

    public ResponseEntity<?> deletandoComentario (@PathVariable Long comentId, @PathVariable Long usuarioId) {

        boolean existingPost = comentarioService.deleteComentario(comentId, usuarioId);

        if(existingPost){
            return ResponseEntity.ok("Deletado com sucesso o comentario " + comentId);
        }

        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" Problema na exclusão do comentário ");


    }


    // Likes
    @PostMapping("/like/{postId}/{usuarioId}")
    public ResponseEntity<?> likeApost(@PathVariable Long postId, @PathVariable Long usuarioId){
        boolean like = likeService.darLike(postId, usuarioId);

        if(like){
            return ResponseEntity.status(HttpStatus.CREATED).body(" Like dado com sucesso!!! ");
        }else{
            return  ResponseEntity.status(HttpStatus.OK).body("Like removido.");
        }


    }


    // Respostas
    @PostMapping("/respostas/{usuarioId}/{comentarioId}")
    public ResponseEntity<Resposta> criarResposta(
            @RequestBody Resposta resposta,
            @PathVariable Long usuarioId,
            @PathVariable Long comentarioId) {

        Resposta respostaCriada = respostaService.criarResposta(resposta, usuarioId, comentarioId);
        return new ResponseEntity<>(respostaCriada, HttpStatus.CREATED);
    }


    @GetMapping("/respostas/{respostasId}")
    public ResponseEntity<List<RespostasDTO>> listarRespostasPorComentario(@PathVariable Long respostasId) {
        List<RespostasDTO> respostas = respostaService.listarRespostasPorComentario(respostasId);
        return ResponseEntity.ok(respostas);
    }

    @PostMapping("/seguir/{seguidorId}/{seguidoId}")
    public ResponseEntity<Void> seguir(@PathVariable Long seguidorId, @PathVariable Long seguidoId) {
        followService.seguirUsuario(seguidorId, seguidoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unfollow/{seguidorId}/{seguidoId}")
    public ResponseEntity<Void> deixarDeSeguir(@PathVariable Long seguidorId, @PathVariable Long seguidoId) {
        followService.deixarDeSeguir(seguidorId, seguidoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/seguindo/{usuarioId}")
    public ResponseEntity<List<UsuarioResumoDTO>> listarSeguindo(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(followService.listarSeguindo(usuarioId));
    }

    @GetMapping("/seguidores/{usuarioId}")
    public ResponseEntity<List<UsuarioResumoDTO>> listarSeguidores(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(followService.listarSeguidores(usuarioId));
    }

    @GetMapping("/sugestoes/{userId}")
    public ResponseEntity<List<UsuarioResumoDTO>> listarNaoSeguidos(@PathVariable Long userId) {
        List<UsuarioResumoDTO> naoSeguidos = followService.listarUsuariosNaoSeguidos(userId);
        return ResponseEntity.ok(naoSeguidos);
    }

    @GetMapping("/notificando/usuario/{id}")
    public List<Notificacao> getNotificacoesNaoLidas(@PathVariable Long id) {
        return notificacaoRepository.findByDestinatarioIdAndLidaFalse(id);
    }

    @PostMapping("/marcarlida/{id}")
    public void marcarComoLida(@PathVariable Long id) {
        notificacaoRepository.findById(id).ifPresent(n -> {
            n.setLida(true);
            notificacaoRepository.save(n);
        });
    }




}
