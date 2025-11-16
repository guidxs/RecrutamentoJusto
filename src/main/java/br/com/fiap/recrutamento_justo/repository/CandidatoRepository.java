package br.com.fiap.recrutamento_justo.repository;

import br.com.fiap.recrutamento_justo.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para operações de persistência de Candidato.
 */
@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    Optional<Candidato> findByUsuarioId(Long usuarioId);
    Boolean existsByUsuarioId(Long usuarioId);
}

