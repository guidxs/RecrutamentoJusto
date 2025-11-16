package br.com.fiap.recrutamento_justo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma aplicação de candidato para uma vaga.
 */
@Entity
@Table(name = "aplicacoes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"candidato_id", "vaga_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aplicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;

    @ManyToOne
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAplicacao status = StatusAplicacao.APLICADO;

    @Column(nullable = false)
    private LocalDateTime dataAplicacao = LocalDateTime.now();

    private LocalDateTime dataUltimaAtualizacao = LocalDateTime.now();

    // Score gerado pela IA (0-100)
    private Integer scoreIA;

    @Column(length = 1000)
    private String analiseIA;

    // Controle de liberação de dados sensíveis
    @Column(nullable = false)
    private Boolean dadosSensiveisLiberados = false;

    private LocalDateTime dataLiberacaoDados;

    @ManyToOne
    @JoinColumn(name = "liberado_por_id")
    private Usuario liberadoPor;
}

