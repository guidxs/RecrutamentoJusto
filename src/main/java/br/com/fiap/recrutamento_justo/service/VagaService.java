package br.com.fiap.recrutamento_justo.service;

import br.com.fiap.recrutamento_justo.dto.VagaDTO;
import br.com.fiap.recrutamento_justo.exception.ResourceNotFoundException;
import br.com.fiap.recrutamento_justo.model.Usuario;
import br.com.fiap.recrutamento_justo.model.Vaga;
import br.com.fiap.recrutamento_justo.repository.UsuarioRepository;
import br.com.fiap.recrutamento_justo.repository.VagaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço com regras de negócio para Vagas.
 */
@Service
public class VagaService {

    private final VagaRepository vagaRepository;
    private final UsuarioRepository usuarioRepository;

    public VagaService(VagaRepository vagaRepository, UsuarioRepository usuarioRepository) {
        this.vagaRepository = vagaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public VagaDTO criar(VagaDTO dto, Long criadoPorId) {
        Usuario criador = usuarioRepository.findById(criadoPorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Vaga vaga = new Vaga();
        vaga.setTitulo(dto.titulo());
        vaga.setDescricao(dto.descricao());
        vaga.setAreaAtuacao(dto.areaAtuacao());
        vaga.setNivelSenioridade(dto.nivelSenioridade());
        vaga.setHabilidadesRequeridas(dto.habilidadesRequeridas());
        vaga.setSalarioMin(dto.salarioMin());
        vaga.setSalarioMax(dto.salarioMax());
        vaga.setEmpresa(dto.empresa());
        vaga.setLocalizacao(dto.localizacao());
        vaga.setRemoto(dto.remoto() != null ? dto.remoto() : false);
        vaga.setCriadoPor(criador);
        vaga.setDataExpiracao(dto.dataExpiracao());

        vaga = vagaRepository.save(vaga);
        return toDTO(vaga);
    }

    @Transactional(readOnly = true)
    public VagaDTO buscarPorId(Long id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada"));
        return toDTO(vaga);
    }

    @Transactional(readOnly = true)
    public Page<VagaDTO> listarTodas(Pageable pageable) {
        return vagaRepository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<VagaDTO> listarAtivas(Pageable pageable) {
        return vagaRepository.findByAtivaTrue(pageable).map(this::toDTO);
    }

    @Transactional
    public VagaDTO atualizar(Long id, VagaDTO dto) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada"));

        if (dto.titulo() != null) vaga.setTitulo(dto.titulo());
        if (dto.descricao() != null) vaga.setDescricao(dto.descricao());
        if (dto.areaAtuacao() != null) vaga.setAreaAtuacao(dto.areaAtuacao());
        if (dto.nivelSenioridade() != null) vaga.setNivelSenioridade(dto.nivelSenioridade());
        if (dto.habilidadesRequeridas() != null) vaga.setHabilidadesRequeridas(dto.habilidadesRequeridas());
        if (dto.salarioMin() != null) vaga.setSalarioMin(dto.salarioMin());
        if (dto.salarioMax() != null) vaga.setSalarioMax(dto.salarioMax());
        if (dto.empresa() != null) vaga.setEmpresa(dto.empresa());
        if (dto.localizacao() != null) vaga.setLocalizacao(dto.localizacao());
        if (dto.remoto() != null) vaga.setRemoto(dto.remoto());
        if (dto.dataExpiracao() != null) vaga.setDataExpiracao(dto.dataExpiracao());
        if (dto.ativa() != null) vaga.setAtiva(dto.ativa());

        vaga = vagaRepository.save(vaga);
        return toDTO(vaga);
    }

    @Transactional
    public void deletar(Long id) {
        if (!vagaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vaga não encontrada");
        }
        vagaRepository.deleteById(id);
    }

    @Transactional
    public void desativar(Long id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada"));
        vaga.setAtiva(false);
        vagaRepository.save(vaga);
    }

    private VagaDTO toDTO(Vaga vaga) {
        return new VagaDTO(
                vaga.getId(),
                vaga.getTitulo(),
                vaga.getDescricao(),
                vaga.getAreaAtuacao(),
                vaga.getNivelSenioridade(),
                vaga.getHabilidadesRequeridas(),
                vaga.getSalarioMin(),
                vaga.getSalarioMax(),
                vaga.getEmpresa(),
                vaga.getLocalizacao(),
                vaga.getRemoto(),
                vaga.getDataExpiracao(),
                vaga.getAtiva()
        );
    }
}

