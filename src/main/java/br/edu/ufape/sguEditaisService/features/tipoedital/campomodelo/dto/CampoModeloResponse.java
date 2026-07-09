package br.edu.ufape.sguEditaisService.features.tipoedital.campomodelo.dto;

import br.edu.ufape.sguEditaisService.features.tipoedital.campomodelo.CampoModelo;
import br.edu.ufape.sguEditaisService.models.enums.TipoCampo;

public record CampoModeloResponse(
        Long id,
        String titulo,
        TipoCampo tipoCampo,
        boolean obrigatorio,
        String configuracoes,
        Long tipoEditalId
) {
    public static CampoModeloResponse from(CampoModelo campo) {
        return new CampoModeloResponse(
                campo.getId(),
                campo.getTitulo(),
                campo.getTipoCampo(),
                campo.isObrigatorio(),
                campo.getConfiguracoes(),
                campo.getTipoEdital().getId()
        );
    }
}