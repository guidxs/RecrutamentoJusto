package br.com.fiap.recrutamento_justo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para página inicial e health check.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("aplicacao", "RecrutamentoJusto API");
        response.put("versao", "1.0.0");
        response.put("status", "Online");
        response.put("descricao", "Plataforma de recrutamento justo impulsionada por IA");
        response.put("documentacao", "Consulte o arquivo API_TESTES.md no repositório");
        response.put("endpoints", Map.of(
                "login", "/auth/login",
                "registro", "/auth/registro",
                "health", "/health",
                "vagas", "/vagas",
                "candidatos", "/candidatos",
                "aplicacoes", "/aplicacoes"
        ));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "RecrutamentoJusto");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
