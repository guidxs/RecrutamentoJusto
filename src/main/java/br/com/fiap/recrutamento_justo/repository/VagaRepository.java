package br.com.fiap.recrutamento_justo.repository;

import br.com.fiap.recrutamento_justo.model.AreaAtuacao;
import br.com.fiap.recrutamento_justo.model.Vaga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para operações de persistência de Vaga.
 */
@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {
    Page<Vaga> findByAtivaTrue(Pageable pageable);
    Page<Vaga> findByAreaAtuacaoAndAtivaTrue(AreaAtuacao areaAtuacao, Pageable pageable);
    Page<Vaga> findByCriadoPorId(Long criadoPorId, Pageable pageable);
}

