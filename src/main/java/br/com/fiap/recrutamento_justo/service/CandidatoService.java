package br.com.fiap.recrutamento_justo.service;

import br.com.fiap.recrutamento_justo.dto.CandidatoDTO;
import br.com.fiap.recrutamento_justo.dto.CandidatoResponseDTO;
import br.com.fiap.recrutamento_justo.exception.BusinessException;
import br.com.fiap.recrutamento_justo.exception.ResourceNotFoundException;
import br.com.fiap.recrutamento_justo.model.Candidato;
import br.com.fiap.recrutamento_justo.model.Usuario;
import br.com.fiap.recrutamento_justo.repository.CandidatoRepository;
import br.com.fiap.recrutamento_justo.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço com regras de negócio para Candidatos.
 * Responsável por gerenciar perfis de candidatos e controle de dados sensíveis.
 */
@Service
public class CandidatoService {

    private final CandidatoRepository candidatoRepository;
    private final UsuarioRepository usuarioRepository;

    public CandidatoService(CandidatoRepository candidatoRepository, UsuarioRepository usuarioRepository) {
        this.candidatoRepository = candidatoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public CandidatoDTO criar(CandidatoDTO dto, Long usuarioId) {
        // Verifica se usuário existe
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Verifica se já existe candidato para este usuário
        if (candidatoRepository.existsByUsuarioId(usuarioId)) {
            throw new BusinessException("Já existe um perfil de candidato para este usuário");
        }

        Candidato candidato = new Candidato();
        candidato.setUsuario(usuario);
        candidato.setDadosSensiveis(dto.dadosSensiveis());
        candidato.setEndereco(dto.endereco());
        candidato.setResumoProfissional(dto.resumoProfissional());
        candidato.setHabilidades(dto.habilidades());
        candidato.setAreaInteresse(dto.areaInteresse());
        candidato.setExperienciasRelevantes(dto.experienciasRelevantes());
        candidato.setAnosCodificacao(dto.anosCodificacao());

        candidato = candidatoRepository.save(candidato);
        return toDTO(candidato);
    }

    @Transactional(readOnly = true)
    public CandidatoResponseDTO buscarPorId(Long id, boolean mostrarDadosSensiveis) {
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato não encontrado"));
        return toResponseDTO(candidato, mostrarDadosSensiveis || candidato.getDadosSensiveisLiberados());
    }

    @Transactional(readOnly = true)
    public CandidatoResponseDTO buscarPorUsuarioId(Long usuarioId, boolean mostrarDadosSensiveis) {
        Candidato candidato = candidatoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato não encontrado para este usuário"));
        return toResponseDTO(candidato, mostrarDadosSensiveis || candidato.getDadosSensiveisLiberados());
    }

    @Transactional(readOnly = true)
    public Page<CandidatoResponseDTO> listarTodos(Pageable pageable, boolean mostrarDadosSensiveis) {
        return candidatoRepository.findAll(pageable)
                .map(c -> toResponseDTO(c, mostrarDadosSensiveis));
    }

    @Transactional
    public CandidatoDTO atualizar(Long id, CandidatoDTO dto) {
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato não encontrado"));

        if (dto.dadosSensiveis() != null) {
            candidato.setDadosSensiveis(dto.dadosSensiveis());
        }
        if (dto.endereco() != null) {
            candidato.setEndereco(dto.endereco());
        }
        if (dto.resumoProfissional() != null) {
            candidato.setResumoProfissional(dto.resumoProfissional());
        }
        if (dto.habilidades() != null) {
            candidato.setHabilidades(dto.habilidades());
        }
        if (dto.areaInteresse() != null) {
            candidato.setAreaInteresse(dto.areaInteresse());
        }
        if (dto.experienciasRelevantes() != null) {
            candidato.setExperienciasRelevantes(dto.experienciasRelevantes());
        }
        if (dto.anosCodificacao() != null) {
            candidato.setAnosCodificacao(dto.anosCodificacao());
        }

        candidato = candidatoRepository.save(candidato);
        return toDTO(candidato);
    }

    @Transactional
    public void deletar(Long id) {
        if (!candidatoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Candidato não encontrado");
        }
        candidatoRepository.deleteById(id);
    }

    @Transactional
    public void liberarDadosSensiveis(Long candidatoId) {
        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato não encontrado"));
        candidato.setDadosSensiveisLiberados(true);
        candidatoRepository.save(candidato);
    }

    private CandidatoDTO toDTO(Candidato candidato) {
        return new CandidatoDTO(
                candidato.getId(),
                candidato.getUsuario().getId(),
                candidato.getDadosSensiveis(),
                candidato.getEndereco(),
                candidato.getResumoProfissional(),
                candidato.getHabilidades(),
                candidato.getAreaInteresse(),
                candidato.getExperienciasRelevantes(),
                candidato.getAnosCodificacao(),
                candidato.getDadosSensiveisLiberados()
        );
    }

    private CandidatoResponseDTO toResponseDTO(Candidato candidato, boolean mostrarDadosSensiveis) {
        return new CandidatoResponseDTO(
                candidato.getId(),
                candidato.getResumoProfissional(),
                candidato.getHabilidades(),
                candidato.getAreaInteresse(),
                candidato.getExperienciasRelevantes(),
                candidato.getAnosCodificacao(),
                mostrarDadosSensiveis ? candidato.getDadosSensiveis() : null,
                mostrarDadosSensiveis ? candidato.getEndereco() : null,
                candidato.getDadosSensiveisLiberados()
        );
    }
}
