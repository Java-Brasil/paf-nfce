package br.inf.rcconsultoria.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PafIndicadorEstoque {
    ENTRADA('+'),
    SAIDA('-');

    private final char codigo;
}
