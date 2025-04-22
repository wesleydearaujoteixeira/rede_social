package tentandoCriarApostagem.redeSocial.services.usuario;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tentandoCriarApostagem.redeSocial.Entities.Usuario;
import tentandoCriarApostagem.redeSocial.Entities.UsuarioDTO;
import tentandoCriarApostagem.redeSocial.repository.UserRepository;
import tentandoCriarApostagem.redeSocial.security.JwtService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;



@Service
public class UserService {


    @Autowired
    private UserRepository usuarioRepository;
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    Cloudinary cloudinary;

    @Autowired
    PasswordEncoder passwordEncoder;



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




    public void criarUsuarioComImagemSimples(String nome, String email, String senha, MultipartFile imagem) throws IOException {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);

        String senhaCriptografada = passwordEncoder.encode(senha);

        usuario.setSenha(senhaCriptografada);

        if (imagem != null && !imagem.isEmpty()) {
            // Salvar localmente
            String pastaUpload = "uploads/";
            String nomeArquivo = UUID.randomUUID() + "_" + imagem.getOriginalFilename();
            Path caminhoCompleto = Paths.get(pastaUpload, nomeArquivo);

            Files.createDirectories(caminhoCompleto.getParent());
            Files.write(caminhoCompleto, imagem.getBytes());

            // Upload para Cloudinary
            File arquivoTemporario = Files.createTempFile("upload", imagem.getOriginalFilename()).toFile();
            imagem.transferTo(arquivoTemporario);

            Map resultado = cloudinary.uploader().upload(arquivoTemporario, ObjectUtils.emptyMap());


            // Obter a URL da imagem hospedada
            String urlImagemCloudinary = (String) resultado.get("secure_url");

            // Setar a URL da imagem no banco
            usuario.setImagemPerfilUrl(urlImagemCloudinary);


            // Apagar o arquivo temporário
            arquivoTemporario.delete();
        }

        usuarioRepository.save(usuario);

    }

    public Usuario getUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: "));
    }




    @Transactional
    public void atualizarUsuarioComImagemSimples(Long id, String nome, String email, String senha, MultipartFile novaImagem) throws IOException {


        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado com o ID: " + id);
        }


        Usuario usuario = usuarioOptional.get();

        // verificação do usuário logado
        if (!email.trim().equalsIgnoreCase(usuario.getEmail().trim()) || !passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Email ou senha inválidos!");
        }

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        if (novaImagem != null && !novaImagem.isEmpty()) {
                // Upload para Cloudinary diretamente do InputStream
                File arquivoTemporario = Files.createTempFile("upload", novaImagem.getOriginalFilename()).toFile();
                novaImagem.transferTo(arquivoTemporario);

                Map resultado = cloudinary.uploader().upload(arquivoTemporario, ObjectUtils.emptyMap());

                // Obter a URL da imagem hospedada
                String urlImagemCloudinary = (String) resultado.get("secure_url");

                // Setar a URL da imagem no banco
                usuario.setImagemPerfilUrl(urlImagemCloudinary);

                // Apagar o arquivo temporário
                arquivoTemporario.delete();


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
