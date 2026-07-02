package br.edu.ufape.sguEditaisService.features.tipoedital.dto;

import br.edu.ufape.sguEditaisService.features.tipoedital.TipoEdital;

public record TipoEditalResponse(
    Long id,
    String nome,
    String descricao,
    boolean ativo,
    String moduloOrigem
) {
    public static TipoEditalResponse from(TipoEdital tipo) {
        return new TipoEditalResponse(
                tipo.getId(),
                tipo.getNome(),
                tipo.getDescricao(),
                tipo.isAtivo(),
                tipo.getModuloOrigem()
        );
    }
}
