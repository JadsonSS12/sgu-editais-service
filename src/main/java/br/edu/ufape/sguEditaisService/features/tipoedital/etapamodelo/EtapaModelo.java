package br.edu.ufape.sguEditaisService.features.tipoedital.etapamodelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.edu.ufape.sguEditaisService.features.tipoedital.TipoEdital;
import br.edu.ufape.sguEditaisService.features.tipoedital.campomodelo.CampoModelo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EtapaModelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    private String descricao;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer ordem;

    // NOVO: Datas trazidas da extinta entidade DataEtapa
    @Column(nullable = false)
    private LocalDateTime dataInicio;
    @Column(nullable = false)
    private LocalDateTime dataFim;

    // NOVO: O coração da flexibilidade. Mapeia perfeitamente para JSONB no PostgreSQL.
    // Aqui você guarda: {"perfisAvaliadores": ["ROLE_X"], "valorTaxa": 50.00, "isencaoPermitida": true}
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String configuracoes;

    // Se pertencer ao Molde (TipoEdital)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_edital_id", nullable = false)
    private TipoEdital tipoEdital;

    @OneToMany(mappedBy = "etapaModelo", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CampoModelo> camposModelo = new ArrayList<>();

    public static EtapaModelo criar(
        String nome,
        String descricao,
        Integer ordem,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        String configuracoes
    ) {
        EtapaModelo etapa = new EtapaModelo();
        etapa.nome = nome;
        etapa.descricao = descricao;
        etapa.ordem = ordem;
        etapa.dataInicio = dataInicio;
        etapa.dataFim = dataFim;
        etapa.configuracoes = configuracoes;
        return etapa;
    }

    public void atualizar(
        String nome,
        String descricao,
        Integer ordem,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        String configuracoes
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.ordem = ordem;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.configuracoes = configuracoes;
    }

    public void vincularAoTipo(TipoEdital tipoEdital)
    {
        this.tipoEdital = tipoEdital;
    }

    public void desvincularAoTipo()
    {
        this.tipoEdital = null;
    }

    public void adicionarCampo(CampoModelo campo) {
        this.camposModelo.add(campo);
        campo.vincularAEtapa(this);
    }

    public void removerCampo(CampoModelo campo) {
        this.camposModelo.remove(campo);
        campo.desvincularDono();
    }
}