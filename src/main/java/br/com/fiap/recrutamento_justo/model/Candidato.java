package br.com.fiap.recrutamento_justo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um candidato no sistema.
 * Contém dados sensíveis (ocultados inicialmente) e dados públicos (habilidades).
 */
@Entity
@Table(name = "candidatos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    // Dados sensíveis - ocultados inicialmente
    @Embedded
    private DadosSensiveisVO dadosSensiveis;

    @Embedded
    private EnderecoVO endereco;

    // Dados públicos para análise inicial
    @Column(length = 2000)
    private String resumoProfissional;

    @ElementCollection
    @CollectionTable(name = "candidato_habilidades", joinColumns = @JoinColumn(name = "candidato_id"))
    @Column(name = "habilidade")
    private List<String> habilidades = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private AreaAtuacao areaInteresse;

    @Column(length = 1000)
    private String experienciasRelevantes;

    private Integer anosCodificacao;

    @Column(nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean dadosSensiveisLiberados = false;
}

