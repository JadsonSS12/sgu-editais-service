package br.edu.ufape.sguEditaisService.features.tipoedital.campomodelo;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.edu.ufape.sguEditaisService.features.tipoedital.TipoEdital;
import br.edu.ufape.sguEditaisService.features.tipoedital.etapamodelo.EtapaModelo;
import br.edu.ufape.sguEditaisService.models.enums.TipoCampo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampoModelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCampo tipoCampo;

    @Column(nullable = false)
    private boolean obrigatorio = true;

    // NOVO: Metadados de validação flexíveis mapeados para JSONB no PostgreSQL.
    // Ex: {"maxLength": 200}, {"tamanhoMaximoMB": 5, "extensoes": ["pdf", "jpg"]}, ou {"opcoes": ["Sim", "Não"]}
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String configuracoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_edital_id")
    private TipoEdital tipoEdital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etapa_modelo_id")
    private EtapaModelo etapaModelo;

    public static CampoModelo criar(
        String titulo,
        TipoCampo tipoCampo,
        boolean obrigatorio,
        String configuracoes
    ) {
        CampoModelo campo = new CampoModelo();
        campo.titulo = titulo;
        campo.tipoCampo = tipoCampo;
        campo.obrigatorio = obrigatorio;
        campo.configuracoes = configuracoes;
        return campo;
    }

    public void atualizar(
        String titulo,
        TipoCampo tipoCampo,
        boolean obrigatorio,
        String configuracoes
    ) {
        this.titulo = titulo;
        this.tipoCampo = tipoCampo;
        this.obrigatorio = obrigatorio;
        this.configuracoes = configuracoes;
    }

    public void vincularAoTipo(TipoEdital tipoEdital) {
        if (this.etapaModelo != null) {
            throw new IllegalStateException("Campo já pertence a uma etapa.");
        }

        this.tipoEdital = tipoEdital;
    }

    public void vincularAEtapa(EtapaModelo etapaModelo) {
        if (this.tipoEdital != null) {
            throw new IllegalStateException("Campo já pertence ao tipo de edital.");
        }

        this.etapaModelo = etapaModelo;
    }

    public void desvincularDono() {
        this.tipoEdital = null;
        this.etapaModelo = null;
    }
}