package br.com.fiap.recrutamento_justo.repository;

import br.com.fiap.recrutamento_justo.model.Aplicacao;
import br.com.fiap.recrutamento_justo.model.StatusAplicacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de persistência de Aplicacao.
 */
@Repository
public interface AplicacaoRepository extends JpaRepository<Aplicacao, Long> {
    Optional<Aplicacao> findByCandidatoIdAndVagaId(Long candidatoId, Long vagaId);
    Page<Aplicacao> findByCandidatoId(Long candidatoId, Pageable pageable);
    Page<Aplicacao> findByVagaId(Long vagaId, Pageable pageable);
    List<Aplicacao> findByVagaIdOrderByScoreIADesc(Long vagaId);
    Page<Aplicacao> findByStatus(StatusAplicacao status, Pageable pageable);
    Boolean existsByCandidatoIdAndVagaId(Long candidatoId, Long vagaId);
}

