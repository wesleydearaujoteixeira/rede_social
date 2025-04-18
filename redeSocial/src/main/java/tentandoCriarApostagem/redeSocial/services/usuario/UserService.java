package tentandoCriarApostagem.redeSocial.services.usuario;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tentandoCriarApostagem.redeSocial.Entities.Usuario;
import tentandoCriarApostagem.redeSocial.Entities.UsuarioDTO;
import tentandoCriarApostagem.redeSocial.repository.UserRepository;
import tentandoCriarApostagem.redeSocial.security.JwtService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {


    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserRepository usuarioRepository;
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    public String login(Usuario user) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return jwtService.generateToken(userDetails);

        } catch (AuthenticationException e) {
            throw new RuntimeException("Usuário ou senha inválidos.");
        }
    }



    @Transactional
    public void criarUsuarioComImagemSimples(String nome, String email, String senha, MultipartFile imagem) throws IOException {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        if (imagem != null && !imagem.isEmpty()) {
            // Upload para o Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(imagem.getBytes(), ObjectUtils.asMap("folder", "usuarios/"));

            // A URL segura fornecida pelo Cloudinary
            String imagemUrl = (String) uploadResult.get("secure_url");
            usuario.setImagemPerfilUrl(imagemUrl); // Armazena a URL da imagem no banco
        }

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void atualizarUsuarioComImagemSimples(Long id, String nome, String email, String senha, MultipartFile novaImagem) throws IOException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado com o ID: " + id);
        }

        Usuario usuario = usuarioOptional.get();

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        if (novaImagem != null && !novaImagem.isEmpty()) {
            String pastaUpload = "uploads/";
            String nomeArquivo = UUID.randomUUID() + "_" + novaImagem.getOriginalFilename();
            Path caminhoCompleto = Paths.get(pastaUpload, nomeArquivo);

            // Cria diretório, se necessário
            Files.createDirectories(caminhoCompleto.getParent());
            // Salva nova imagem
            Files.write(caminhoCompleto, novaImagem.getBytes());

            // Atualiza a URL da imagem no usuário
            usuario.setImagemPerfilUrl("http://localhost:8080/uploads/" + nomeArquivo);
        }

        usuarioRepository.save(usuario);
    }


    public List<Usuario> getUsers(){
        return usuarioRepository.findAll();
    }

    public boolean deleteUser(Long id){

        Optional<Usuario> UserHas = usuarioRepository.findById(id);

        if(UserHas.isPresent()){
            usuarioRepository.deleteById(id);
            return true;
        }else{
            return false;
        }

    }



}
