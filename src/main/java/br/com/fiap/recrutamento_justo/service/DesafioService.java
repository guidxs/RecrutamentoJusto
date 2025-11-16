package br.com.fiap.recrutamento_justo.service;

import br.com.fiap.recrutamento_justo.dto.DesafioDTO;
import br.com.fiap.recrutamento_justo.exception.BusinessException;
import br.com.fiap.recrutamento_justo.exception.ResourceNotFoundException;
import br.com.fiap.recrutamento_justo.model.Aplicacao;
import br.com.fiap.recrutamento_justo.model.Desafio;
import br.com.fiap.recrutamento_justo.model.StatusAplicacao;
import br.com.fiap.recrutamento_justo.model.Usuario;
import br.com.fiap.recrutamento_justo.repository.AplicacaoRepository;
import br.com.fiap.recrutamento_justo.repository.DesafioRepository;
import br.com.fiap.recrutamento_justo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Serviço com regras de negócio para Desafios técnicos.
 */
@Service
public class DesafioService {

    private final DesafioRepository desafioRepository;
    private final AplicacaoRepository aplicacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public DesafioService(DesafioRepository desafioRepository,
                         AplicacaoRepository aplicacaoRepository,
                         UsuarioRepository usuarioRepository) {
        this.desafioRepository = desafioRepository;
        this.aplicacaoRepository = aplicacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public DesafioDTO criar(Long aplicacaoId, String descricaoDesafio, LocalDateTime prazoEntrega) {
        Aplicacao aplicacao = aplicacaoRepository.findById(aplicacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Aplicação não encontrada"));

        // Verifica se já existe desafio para esta aplicação
        if (desafioRepository.existsByAplicacaoId(aplicacaoId)) {
            throw new BusinessException("Já existe um desafio para esta aplicação");
        }

        Desafio desafio = new Desafio();
        desafio.setAplicacao(aplicacao);
        desafio.setDescricaoDesafio(descricaoDesafio);
        desafio.setPrazoEntrega(prazoEntrega);

        // Atualiza status da aplicação
        aplicacao.setStatus(StatusAplicacao.DESAFIO_ENVIADO);
        aplicacao.setDataUltimaAtualizacao(LocalDateTime.now());
        aplicacaoRepository.save(aplicacao);

        desafio = desafioRepository.save(desafio);
        return toDTO(desafio);
    }

    @Transactional(readOnly = true)
    public DesafioDTO buscarPorId(Long id) {
        Desafio desafio = desafioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Desafio não encontrado"));
        return toDTO(desafio);
    }

    @Transactional(readOnly = true)
    public DesafioDTO buscarPorAplicacao(Long aplicacaoId) {
        Desafio desafio = desafioRepository.findByAplicacaoId(aplicacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Desafio não encontrado para esta aplicação"));
        return toDTO(desafio);
    }

    @Transactional
    public DesafioDTO responderDesafio(Long id, String solucao) {
        Desafio desafio = desafioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Desafio não encontrado"));

        if (desafio.getDataResposta() != null) {
            throw new BusinessException("Este desafio já foi respondido");
        }

        desafio.setSolucaoCandidato(solucao);
        desafio.setDataResposta(LocalDateTime.now());

        // Atualiza status da aplicação
        Aplicacao aplicacao = desafio.getAplicacao();
        aplicacao.setStatus(StatusAplicacao.DESAFIO_CONCLUIDO);
        aplicacao.setDataUltimaAtualizacao(LocalDateTime.now());
        aplicacaoRepository.save(aplicacao);

        desafio = desafioRepository.save(desafio);
        return toDTO(desafio);
    }

    @Transactional
    public DesafioDTO avaliarDesafio(Long id, Integer nota, String feedback, Long avaliadoPorId) {
        Desafio desafio = desafioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Desafio não encontrado"));

        if (desafio.getDataResposta() == null) {
            throw new BusinessException("Não é possível avaliar um desafio que ainda não foi respondido");
        }

        Usuario avaliador = usuarioRepository.findById(avaliadoPorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário avaliador não encontrado"));

        desafio.setNotaDesafio(nota);
        desafio.setFeedbackAvaliacao(feedback);
        desafio.setAvaliadoPor(avaliador);

        desafio = desafioRepository.save(desafio);
        return toDTO(desafio);
    }

    @Transactional
    public void deletar(Long id) {
        if (!desafioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Desafio não encontrado");
        }
        desafioRepository.deleteById(id);
    }

    private DesafioDTO toDTO(Desafio desafio) {
        return new DesafioDTO(
                desafio.getId(),
                desafio.getAplicacao().getId(),
                desafio.getDescricaoDesafio(),
                desafio.getSolucaoCandidato(),
                desafio.getDataEnvio(),
                desafio.getDataResposta(),
                desafio.getPrazoEntrega(),
                desafio.getNotaDesafio(),
                desafio.getFeedbackAvaliacao()
        );
    }
}

