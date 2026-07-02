package br.edu.ufape.sguEditaisService.features.tipoedital;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufape.sguEditaisService.features.tipoedital.campomodelo.CampoModelo;
import br.edu.ufape.sguEditaisService.features.tipoedital.etapamodelo.EtapaModelo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    // NOVO: Isolamento de domínio. Define qual serviço é "dono" deste template (ex: "sgu-prae-service")
    @NotBlank
    @Column(nullable = false)
    private String moduloOrigem;

    // As etapas canônicas que todo edital desse tipo DEVE ter
    @OneToMany(mappedBy = "tipoEdital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EtapaModelo> etapasModelo = new ArrayList<>();

    // O formulário padrão para esse tipo de edital
    @OneToMany(mappedBy = "tipoEdital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampoModelo> camposModelo = new ArrayList<>();

    public static TipoEdital criar(String nome, String descricao, String moduloOrigem)
    {
        TipoEdital tipo = new TipoEdital();
        tipo.nome = nome;
        tipo.descricao = descricao;
        tipo.moduloOrigem = moduloOrigem;

        return tipo;
    }

    public void atualizar(String nome, String descricao, String moduloOrigem)
    {
        this.nome = nome;
        this.descricao = descricao;
        this.moduloOrigem = moduloOrigem;
    }

    public void adicionarEtapa(EtapaModelo etapa)
    {
        etapasModelo.add(etapa);
        etapa.vincularAoTipo(this);
    }

    public void removerEtapa(EtapaModelo etapa)
    {
        etapasModelo.remove(etapa);
        etapa.desvincularAoTipo(this);
    }

    public void adicionarCampoGeral(CampoModelo campo) {
        camposModelo.add(campo);
        campo.vincularTipoEdital(this);
    }

    public void removerCampoGeral(CampoModelo campo) {
        camposModelo.remove(campo);
        campo.desvincularTipoEdital();
    }

    public void ativar()
    {
        this.ativo = true;
    }

    public void desativar()
    {
        this.ativo = false;
    }
}