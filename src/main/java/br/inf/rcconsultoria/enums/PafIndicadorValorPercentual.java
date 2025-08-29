package br.inf.rcconsultoria.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PafIndicadorValorPercentual {
    VALOR("V", "Valor monetário"),
    PERCENTUAL("P", "Percentual");

    private final String codigo;
    private final String descricao;
}

