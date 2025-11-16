package br.com.fiap.recrutamento_justo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que representa um desafio técnico enviado para um candidato.
 */
@Entity
@Table(name = "desafios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Desafio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "aplicacao_id", nullable = false, unique = true)
    private Aplicacao aplicacao;

    @Column(nullable = false, length = 2000)
    private String descricaoDesafio;

    @Column(length = 5000)
    private String solucaoCandidato;

    @Column(nullable = false)
    private LocalDateTime dataEnvio = LocalDateTime.now();

    private LocalDateTime dataResposta;

    private LocalDateTime prazoEntrega;

    // Avaliação do desafio (0-100)
    private Integer notaDesafio;

    @Column(length = 1000)
    private String feedbackAvaliacao;

    @ManyToOne
    @JoinColumn(name = "avaliado_por_id")
    private Usuario avaliadoPor;
}

