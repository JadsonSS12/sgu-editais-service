package br.edu.ufape.sguEditaisService.features.tipoedital;

import br.edu.ufape.sguEditaisService.exceptions.business.RegraNegocioException;
import br.edu.ufape.sguEditaisService.features.tipoedital.campomodelo.CampoModelo;
import br.edu.ufape.sguEditaisService.features.tipoedital.etapamodelo.EtapaModelo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE tipo_edital SET ativo = false WHERE id = ?")
@SQLRestriction("ativo = true")
public class TipoEdital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private boolean ativo = true;

    @NotBlank
    @Column(nullable = false)
    private String moduloOrigem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoModelo estado = EstadoModelo.RASCUNHO;

    @OneToMany(mappedBy = "tipoEdital", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<EtapaModelo> etapasModelo = new ArrayList<>();

    @OneToMany(mappedBy = "tipoEdital", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CampoModelo> camposModelo = new ArrayList<>();

    public static TipoEdital criar(String nome, String descricao, String moduloOrigem) {
        TipoEdital tipo = new TipoEdital();
        tipo.nome = nome;
        tipo.descricao = descricao;
        tipo.moduloOrigem = moduloOrigem;
        tipo.ativo = true;
        tipo.estado = EstadoModelo.RASCUNHO;
        return tipo;
    }

    public void checarPermissaoEdicao() {
        if (this.estado != EstadoModelo.RASCUNHO) {
            throw new RegraNegocioException("Modelos finalizados não podem ser alterados.");
        }
    }

    public void finalizar() {
        if (this.etapasModelo.isEmpty()) {
            throw new RegraNegocioException("Não é possível finalizar um modelo de edital sem nenhuma etapa.");
        }
        this.estado = EstadoModelo.FINALIZADO;
    }

    // ==========================================
    // MUTAÇÕES DE ETAPA
    // ==========================================
    public void adicionarEtapa(EtapaModelo etapa) {
        checarPermissaoEdicao();
        etapasModelo.add(etapa);
        etapa.vincularAoTipo(this);
    }

    public void removerEtapa(EtapaModelo etapa) {
        checarPermissaoEdicao();
        this.etapasModelo.remove(etapa);
        etapa.desvincularAoTipo();
    }

    // ==========================================
    // MUTAÇÕES DE CAMPO GERAL (MODELO)
    // ==========================================
    public void adicionarCampoModelo(CampoModelo campo) {
        checarPermissaoEdicao();
        camposModelo.add(campo);
        campo.vincularAoTipo(this);
    }

    public void removerCampoModelo(CampoModelo campo) {
        checarPermissaoEdicao();
        this.camposModelo.remove(campo);
        campo.desvincular();
    }
}