package br.edu.ufape.sguEditaisService.features.tipoedital.campomodelo;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.edu.ufape.sguEditaisService.features.tipoedital.TipoEdital;
import br.edu.ufape.sguEditaisService.models.enums.TipoCampo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @ManyToOne
    @JoinColumn(name = "tipoEditalId")
    private TipoEdital tipoEdital;


}