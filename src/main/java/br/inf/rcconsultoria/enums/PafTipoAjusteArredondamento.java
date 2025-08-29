package br.inf.rcconsultoria.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PafTipoAjusteArredondamento {
    ARREDONDAMENTO("A", "Arredondamento"),
    TRUNCAMENTO("T", "Truncamento");

    private final String codigo;
    private final String descricao;
}
