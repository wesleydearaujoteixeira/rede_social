package tentandoCriarApostagem.redeSocial.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tentandoCriarApostagem.redeSocial.Entities.Usuario;
import tentandoCriarApostagem.redeSocial.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Verifique se os dados estão sendo preenchidos corretamente
        System.out.println("Usuário encontrado: " + usuario.getNome() + ", Email: " + usuario.getEmail() + " " + usuario.getId());

        return usuario;
    }
}
