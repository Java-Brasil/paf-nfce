package br.inf.rcconsultoria.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PafOrigemProduto {
    PROPRIO("P", "Produção própria"),
    TERCEIROS("T", "Produção de terceiros");

    private final String codigo;
    private final String descricao;
}

