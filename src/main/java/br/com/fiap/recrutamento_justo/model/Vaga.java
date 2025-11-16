package br.com.fiap.recrutamento_justo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa uma vaga de emprego.
 */
@Entity
@Table(name = "vagas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória")
    @Column(length = 3000, nullable = false)
    private String descricao;

    @NotNull(message = "Área de atuação é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AreaAtuacao areaAtuacao;

    @NotNull(message = "Nível de senioridade é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelSenioridade nivelSenioridade;

    @ElementCollection
    @CollectionTable(name = "vaga_habilidades_requeridas", joinColumns = @JoinColumn(name = "vaga_id"))
    @Column(name = "habilidade")
    private List<String> habilidadesRequeridas = new ArrayList<>();

    private BigDecimal salarioMin;
    private BigDecimal salarioMax;

    @Column(nullable = false)
    private String empresa;

    private String localizacao;

    @Column(nullable = false)
    private Boolean remoto = false;

    @ManyToOne
    @JoinColumn(name = "criado_por_id", nullable = false)
    private Usuario criadoPor;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    private LocalDateTime dataExpiracao;

    @Column(nullable = false)
    private Boolean ativa = true;
}

