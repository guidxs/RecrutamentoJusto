package br.com.fiap.recrutamento_justo.controller;

import br.com.fiap.recrutamento_justo.dto.LoginDTO;
import br.com.fiap.recrutamento_justo.dto.RegistroDTO;
import br.com.fiap.recrutamento_justo.dto.TokenDTO;
import br.com.fiap.recrutamento_justo.exception.BusinessException;
import br.com.fiap.recrutamento_justo.model.Usuario;
import br.com.fiap.recrutamento_justo.repository.UsuarioRepository;
import br.com.fiap.recrutamento_justo.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para autenticação e registro de usuários.
 * Endpoints públicos que não requerem autenticação JWT.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager,
                         UsuarioRepository usuarioRepository,
                         PasswordEncoder passwordEncoder,
                         TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(
                    loginDTO.login(), loginDTO.senha());
            var auth = authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((Usuario) auth.getPrincipal());
            return ResponseEntity.ok(new TokenDTO(token, tokenService.getExpiration()));
        } catch (BadCredentialsException e) {
            throw new BusinessException("Login ou senha inválidos");
        } catch (AuthenticationException e) {
            throw new BusinessException("Erro na autenticação: " + e.getMessage());
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registro(@RequestBody @Valid RegistroDTO registroDTO) {
        if (usuarioRepository.existsByLogin(registroDTO.login())) {
            throw new BusinessException("Login já está em uso");
        }
        if (usuarioRepository.existsByEmail(registroDTO.email())) {
            throw new BusinessException("Email já está em uso");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(registroDTO.login());
        usuario.setSenha(passwordEncoder.encode(registroDTO.senha()));
        usuario.setEmail(registroDTO.email());
        usuario.setRole(registroDTO.role());
        usuario.setAtivo(true);

        usuarioRepository.save(usuario);
        return ResponseEntity.status(201).body("Usuário registrado com sucesso");
    }
}
