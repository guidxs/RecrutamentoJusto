package br.com.fiap.recrutamento_justo.repository;

import br.com.fiap.recrutamento_justo.model.Desafio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para operações de persistência de Desafio.
 */
@Repository
public interface DesafioRepository extends JpaRepository<Desafio, Long> {
    Optional<Desafio> findByAplicacaoId(Long aplicacaoId);
    Boolean existsByAplicacaoId(Long aplicacaoId);
}

