package br.com.fiap.recrutamento_justo.repository;

import br.com.fiap.recrutamento_justo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para operações de persistência de Usuario.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByLogin(String login);
    Optional<Usuario> findByEmail(String email);
    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);
}

