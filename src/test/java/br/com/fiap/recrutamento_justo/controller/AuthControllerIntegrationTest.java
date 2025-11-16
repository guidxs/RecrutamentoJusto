package br.com.fiap.recrutamento_justo.controller;

import br.com.fiap.recrutamento_justo.dto.LoginDTO;
import br.com.fiap.recrutamento_justo.dto.RegistroDTO;
import br.com.fiap.recrutamento_justo.model.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRegistrarNovoUsuarioComSucesso() throws Exception {
        RegistroDTO registroDTO = new RegistroDTO(
                "novo_usuario",
                "senha123",
                "novo@email.com",
                UserRole.CANDIDATO
        );

        mockMvc.perform(post("/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuário registrado com sucesso"));
    }

    @Test
    void deveFalharAoRegistrarLoginDuplicado() throws Exception {
        // Primeiro registro
        RegistroDTO registroDTO1 = new RegistroDTO(
                "usuario_duplicado",
                "senha123",
                "email1@email.com",
                UserRole.CANDIDATO
        );

        mockMvc.perform(post("/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroDTO1)))
                .andExpect(status().isCreated());

        // Segundo registro com mesmo login
        RegistroDTO registroDTO2 = new RegistroDTO(
                "usuario_duplicado",
                "senha456",
                "email2@email.com",
                UserRole.CANDIDATO
        );

        mockMvc.perform(post("/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDTO2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Login já está em uso"));
    }

    @Test
    void deveFazerLoginComSucesso() throws Exception {
        // Primeiro registrar
        RegistroDTO registroDTO = new RegistroDTO(
                "usuario_login",
                "senha123",
                "login@email.com",
                UserRole.CANDIDATO
        );

        mockMvc.perform(post("/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated());

        // Depois fazer login
        LoginDTO loginDTO = new LoginDTO("usuario_login", "senha123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.tipo").value("Bearer"));
    }

    @Test
    void deveFalharLoginComSenhaIncorreta() throws Exception {
        // Registrar usuário
        RegistroDTO registroDTO = new RegistroDTO(
                "usuario_senha_errada",
                "senha_correta",
                "senha_errada@email.com",
                UserRole.CANDIDATO
        );

        mockMvc.perform(post("/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated());

        // Tentar login com senha errada
        LoginDTO loginDTO = new LoginDTO("usuario_senha_errada", "senha_errada");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }
}

