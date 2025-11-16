package br.com.fiap.recrutamento_justo.service;

import br.com.fiap.recrutamento_justo.dto.AplicacaoDTO;
import br.com.fiap.recrutamento_justo.exception.BusinessException;
import br.com.fiap.recrutamento_justo.exception.ResourceNotFoundException;
import br.com.fiap.recrutamento_justo.model.Aplicacao;
import br.com.fiap.recrutamento_justo.model.Candidato;
import br.com.fiap.recrutamento_justo.model.StatusAplicacao;
import br.com.fiap.recrutamento_justo.model.Vaga;
import br.com.fiap.recrutamento_justo.repository.AplicacaoRepository;
import br.com.fiap.recrutamento_justo.repository.CandidatoRepository;
import br.com.fiap.recrutamento_justo.repository.VagaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço com regras de negócio para Aplicações.
 * Contém lógica de scoring por IA e controle de liberação de dados sensíveis.
 */
@Service
public class AplicacaoService {

    private final AplicacaoRepository aplicacaoRepository;
    private final CandidatoRepository candidatoRepository;
    private final VagaRepository vagaRepository;

    public AplicacaoService(AplicacaoRepository aplicacaoRepository,
                           CandidatoRepository candidatoRepository,
                           VagaRepository vagaRepository) {
        this.aplicacaoRepository = aplicacaoRepository;
        this.candidatoRepository = candidatoRepository;
        this.vagaRepository = vagaRepository;
    }

    @Transactional
    public AplicacaoDTO aplicar(Long candidatoId, Long vagaId) {
        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato não encontrado"));

        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada"));

        // Verifica se vaga está ativa
        if (!vaga.getAtiva()) {
            throw new BusinessException("Esta vaga não está mais ativa");
        }

        // Verifica se candidato já aplicou para esta vaga
        if (aplicacaoRepository.existsByCandidatoIdAndVagaId(candidatoId, vagaId)) {
            throw new BusinessException("Você já aplicou para esta vaga");
        }

        Aplicacao aplicacao = new Aplicacao();
        aplicacao.setCandidato(candidato);
        aplicacao.setVaga(vaga);
        aplicacao.setStatus(StatusAplicacao.APLICADO);

        // Simula análise por IA - calcula score baseado em match de habilidades
        Integer score = calcularScoreIA(candidato, vaga);
        aplicacao.setScoreIA(score);
        aplicacao.setAnaliseIA(gerarAnaliseIA(candidato, vaga, score));
        aplicacao.setStatus(StatusAplicacao.EM_ANALISE);

        aplicacao = aplicacaoRepository.save(aplicacao);
        return toDTO(aplicacao);
    }

    @Transactional(readOnly = true)
    public AplicacaoDTO buscarPorId(Long id) {
        Aplicacao aplicacao = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aplicação não encontrada"));
        return toDTO(aplicacao);
    }

    @Transactional(readOnly = true)
    public Page<AplicacaoDTO> listarPorCandidato(Long candidatoId, Pageable pageable) {
        return aplicacaoRepository.findByCandidatoId(candidatoId, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<AplicacaoDTO> listarPorVaga(Long vagaId, Pageable pageable) {
        return aplicacaoRepository.findByVagaId(vagaId, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<AplicacaoDTO> listarRankingPorVaga(Long vagaId) {
        return aplicacaoRepository.findByVagaIdOrderByScoreIADesc(vagaId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AplicacaoDTO atualizarStatus(Long id, StatusAplicacao novoStatus) {
        Aplicacao aplicacao = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aplicação não encontrada"));

        aplicacao.setStatus(novoStatus);
        aplicacao.setDataUltimaAtualizacao(LocalDateTime.now());

        // Se pré-selecionado, libera dados sensíveis
        if (novoStatus == StatusAplicacao.PRE_SELECIONADO && !aplicacao.getDadosSensiveisLiberados()) {
            aplicacao.setDadosSensiveisLiberados(true);
            aplicacao.setDataLiberacaoDados(LocalDateTime.now());

            // Libera também no perfil do candidato
            Candidato candidato = aplicacao.getCandidato();
            candidato.setDadosSensiveisLiberados(true);
            candidatoRepository.save(candidato);
        }

        aplicacao = aplicacaoRepository.save(aplicacao);
        return toDTO(aplicacao);
    }

    @Transactional
    public void deletar(Long id) {
        if (!aplicacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aplicação não encontrada");
        }
        aplicacaoRepository.deleteById(id);
    }

    /**
     * Simula algoritmo de IA para calcular score de compatibilidade (0-100).
     * Baseia-se em match de habilidades, área de atuação e anos de experiência.
     */
    private Integer calcularScoreIA(Candidato candidato, Vaga vaga) {
        int score = 0;

        // Match de área (30 pontos)
        if (candidato.getAreaInteresse() == vaga.getAreaAtuacao()) {
            score += 30;
        }

        // Match de habilidades (50 pontos)
        if (candidato.getHabilidades() != null && vaga.getHabilidadesRequeridas() != null) {
            long matchHabilidades = candidato.getHabilidades().stream()
                    .filter(h -> vaga.getHabilidadesRequeridas().contains(h))
                    .count();
            int totalRequeridas = vaga.getHabilidadesRequeridas().size();
            if (totalRequeridas > 0) {
                score += (int) ((matchHabilidades * 50.0) / totalRequeridas);
            }
        }

        // Anos de experiência (20 pontos)
        if (candidato.getAnosCodificacao() != null) {
            switch (vaga.getNivelSenioridade()) {
                case ESTAGIO -> score += candidato.getAnosCodificacao() >= 0 ? 20 : 0;
                case JUNIOR -> score += candidato.getAnosCodificacao() >= 1 ? 20 : 10;
                case PLENO -> score += candidato.getAnosCodificacao() >= 3 ? 20 : 10;
                case SENIOR -> score += candidato.getAnosCodificacao() >= 5 ? 20 : 5;
                case ESPECIALISTA -> score += candidato.getAnosCodificacao() >= 8 ? 20 : 5;
            }
        }

        return Math.min(score, 100);
    }

    private String gerarAnaliseIA(Candidato candidato, Vaga vaga, Integer score) {
        StringBuilder analise = new StringBuilder();
        analise.append("Análise automatizada: ");

        if (score >= 80) {
            analise.append("Excelente match! Candidato altamente qualificado para a vaga.");
        } else if (score >= 60) {
            analise.append("Bom match. Candidato possui competências relevantes.");
        } else if (score >= 40) {
            analise.append("Match moderado. Candidato possui algumas competências necessárias.");
        } else {
            analise.append("Match limitado. Candidato possui poucas competências requeridas.");
        }

        return analise.toString();
    }

    private AplicacaoDTO toDTO(Aplicacao aplicacao) {
        return new AplicacaoDTO(
                aplicacao.getId(),
                aplicacao.getCandidato().getId(),
                aplicacao.getVaga().getId(),
                aplicacao.getStatus(),
                aplicacao.getDataAplicacao(),
                aplicacao.getScoreIA(),
                aplicacao.getAnaliseIA(),
                aplicacao.getDadosSensiveisLiberados()
        );
    }
}

