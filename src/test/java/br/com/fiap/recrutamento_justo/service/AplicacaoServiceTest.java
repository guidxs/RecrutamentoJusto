package br.com.fiap.recrutamento_justo.service;

import br.com.fiap.recrutamento_justo.dto.AplicacaoDTO;
import br.com.fiap.recrutamento_justo.exception.BusinessException;
import br.com.fiap.recrutamento_justo.model.*;
import br.com.fiap.recrutamento_justo.repository.AplicacaoRepository;
import br.com.fiap.recrutamento_justo.repository.CandidatoRepository;
import br.com.fiap.recrutamento_justo.repository.VagaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AplicacaoServiceTest {

    @Mock
    private AplicacaoRepository aplicacaoRepository;

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private VagaRepository vagaRepository;

    @InjectMocks
    private AplicacaoService aplicacaoService;

    private Candidato candidato;
    private Vaga vaga;
    private Aplicacao aplicacao;

    @BeforeEach
    void setUp() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setRole(UserRole.CANDIDATO);

        candidato = new Candidato();
        candidato.setId(1L);
        candidato.setUsuario(usuario);
        candidato.setAreaInteresse(AreaAtuacao.DESENVOLVIMENTO_FULLSTACK);
        List<String> habilidadesCandidato = List.of("Java", "Spring Boot", "React");
        candidato.setHabilidades(habilidadesCandidato);
        candidato.setAnosCodificacao(5);

        Usuario criador = new Usuario();
        criador.setId(2L);
        criador.setRole(UserRole.RH);

        vaga = new Vaga();
        vaga.setId(1L);
        vaga.setTitulo("Desenvolvedor Full Stack");
        vaga.setAreaAtuacao(AreaAtuacao.DESENVOLVIMENTO_FULLSTACK);
        vaga.setNivelSenioridade(NivelSenioridade.PLENO);
        vaga.setAtiva(true);
        vaga.setCriadoPor(criador);
        List<String> habilidadesVaga = List.of("Java", "Spring Boot", "React", "Docker");
        vaga.setHabilidadesRequeridas(habilidadesVaga);

        aplicacao = new Aplicacao();
        aplicacao.setId(1L);
        aplicacao.setCandidato(candidato);
        aplicacao.setVaga(vaga);
        aplicacao.setStatus(StatusAplicacao.EM_ANALISE);
    }

    @Test
    void deveAplicarParaVagaComSucesso() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));
        when(aplicacaoRepository.existsByCandidatoIdAndVagaId(1L, 1L)).thenReturn(false);
        when(aplicacaoRepository.save(any(Aplicacao.class))).thenReturn(aplicacao);

        AplicacaoDTO resultado = aplicacaoService.aplicar(1L, 1L);

        assertNotNull(resultado);
        assertEquals(StatusAplicacao.EM_ANALISE, resultado.status());
        assertNotNull(resultado.scoreIA());
        assertTrue(resultado.scoreIA() > 0);
        verify(aplicacaoRepository, times(1)).save(any(Aplicacao.class));
    }

    @Test
    void deveLancarExcecaoQuandoVagaInativa() {
        vaga.setAtiva(false);
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));

        assertThrows(BusinessException.class, () -> {
            aplicacaoService.aplicar(1L, 1L);
        });
    }

    @Test
    void deveLancarExcecaoQuandoCandidatoJaAplicou() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));
        when(aplicacaoRepository.existsByCandidatoIdAndVagaId(1L, 1L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> {
            aplicacaoService.aplicar(1L, 1L);
        });
    }

    @Test
    void deveAtualizarStatusELiberarDadosSensiveis() {
        when(aplicacaoRepository.findById(1L)).thenReturn(Optional.of(aplicacao));
        when(aplicacaoRepository.save(any(Aplicacao.class))).thenReturn(aplicacao);
        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

        AplicacaoDTO resultado = aplicacaoService.atualizarStatus(1L, StatusAplicacao.PRE_SELECIONADO);

        assertNotNull(resultado);
        verify(candidatoRepository, times(1)).save(any(Candidato.class));
    }

    @Test
    void deveCalcularScoreIACorretamente() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));
        when(aplicacaoRepository.existsByCandidatoIdAndVagaId(1L, 1L)).thenReturn(false);
        when(aplicacaoRepository.save(any(Aplicacao.class))).thenAnswer(invocation -> {
            Aplicacao app = invocation.getArgument(0);
            // Score esperado: 30 (área) + 37.5 (3 de 4 habilidades) + 20 (experiência pleno) = 87.5
            assertTrue(app.getScoreIA() >= 80);
            return app;
        });

        aplicacaoService.aplicar(1L, 1L);

        verify(aplicacaoRepository, times(1)).save(any(Aplicacao.class));
    }
}

