package br.com.fiap.recrutamento_justo.service;

import br.com.fiap.recrutamento_justo.dto.CandidatoDTO;
import br.com.fiap.recrutamento_justo.exception.BusinessException;
import br.com.fiap.recrutamento_justo.exception.ResourceNotFoundException;
import br.com.fiap.recrutamento_justo.model.*;
import br.com.fiap.recrutamento_justo.repository.CandidatoRepository;
import br.com.fiap.recrutamento_justo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidatoServiceTest {

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CandidatoService candidatoService;

    private Usuario usuario;
    private Candidato candidato;
    private CandidatoDTO candidatoDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("teste");
        usuario.setEmail("teste@email.com");
        usuario.setRole(UserRole.CANDIDATO);

        DadosSensiveisVO dadosSensiveis = new DadosSensiveisVO(
                "João Silva", 28, "Masculino", "FIAP", null, "11999999999"
        );

        EnderecoVO endereco = new EnderecoVO(
                "01310-100", "São Paulo", "SP", "Centro", "Av Paulista", "1000", null
        );

        candidato = new Candidato();
        candidato.setId(1L);
        candidato.setUsuario(usuario);
        candidato.setDadosSensiveis(dadosSensiveis);
        candidato.setEndereco(endereco);
        candidato.setResumoProfissional("Desenvolvedor Full Stack");
        candidato.setHabilidades(new ArrayList<>());
        candidato.setAreaInteresse(AreaAtuacao.DESENVOLVIMENTO_FULLSTACK);
        candidato.setAnosCodificacao(5);

        candidatoDTO = new CandidatoDTO(
                null, 1L, dadosSensiveis, endereco, "Desenvolvedor Full Stack",
                new ArrayList<>(), AreaAtuacao.DESENVOLVIMENTO_FULLSTACK, null, 5, false
        );
    }

    @Test
    void deveCriarCandidatoComSucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(candidatoRepository.existsByUsuarioId(1L)).thenReturn(false);
        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

        CandidatoDTO resultado = candidatoService.criar(candidatoDTO, 1L);

        assertNotNull(resultado);
        assertEquals("Desenvolvedor Full Stack", resultado.resumoProfissional());
        verify(candidatoRepository, times(1)).save(any(Candidato.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            candidatoService.criar(candidatoDTO, 1L);
        });
    }

    @Test
    void deveLancarExcecaoQuandoCandidatoJaExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(candidatoRepository.existsByUsuarioId(1L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> {
            candidatoService.criar(candidatoDTO, 1L);
        });
    }

    @Test
    void deveBuscarCandidatoPorId() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));

        var resultado = candidatoService.buscarPorId(1L, false);

        assertNotNull(resultado);
        assertNull(resultado.dadosSensiveis()); // Dados sensíveis ocultos
    }

    @Test
    void deveMostrarDadosSensiveisQuandoPermitido() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));

        var resultado = candidatoService.buscarPorId(1L, true);

        assertNotNull(resultado);
        assertNotNull(resultado.dadosSensiveis()); // Dados sensíveis visíveis
        assertEquals("João Silva", resultado.dadosSensiveis().getNomeCompleto());
    }

    @Test
    void deveLiberarDadosSensiveis() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));
        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

        candidatoService.liberarDadosSensiveis(1L);

        verify(candidatoRepository, times(1)).save(any(Candidato.class));
    }

    @Test
    void deveDeletarCandidato() {
        when(candidatoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(candidatoRepository).deleteById(1L);

        candidatoService.deletar(1L);

        verify(candidatoRepository, times(1)).deleteById(1L);
    }
}

