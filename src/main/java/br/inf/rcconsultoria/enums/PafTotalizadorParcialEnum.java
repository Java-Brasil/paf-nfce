package br.inf.rcconsultoria.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PafTotalizadorParcialEnum {

    T("Tnnnn", "Tributado ICMS", "Valores de operações tributadas pelo ICMS, onde “nnnn” representa a alíquota efetiva do imposto com duas casas decimais. Exemplo: T1800 (alíquota de 18,00% de ICMS)"),
    S("Snnnn", "Tributado ISSQN", "Valores de operações tributadas pelo ISSQN, onde “nnnn” representa a alíquota efetiva do imposto com duas casas decimais. Exemplo: S0500 (alíquota de 5,00% de ISSQN)"),
    F("F", "Substituição Tributária – ICMS", "Valores de operações sujeitas ao ICMS, tributadas por Substituição Tributária."),
    I("I", "Isento – ICMS", "Valores de operações Isentas do ICMS."),
    N("N", "Não-incidência – ICMS", "Valores de operações com Não Incidência do ICMS."),
    FS("FS", "Substituição Tributária – ISSQN", "Valores de operações sujeitas ao ISSQN, tributadas por Substituição Tributária."),
    IS("IS", "Isento – ISSQN", "Valores de operações Isentas do ISSQN."),
    NS("NS", "Não-incidência – ISSQN", "Valores de operações com Não Incidência do ISSQN."),
    DT("DT", "Desconto – ICMS", "Valores relativos a descontos incidentes sobre operações sujeitas ao ICMS"),
    DS("DS", "Desconto – ISSQN", "Valores relativos a descontos incidentes sobre operações sujeitas ao ISSQN"),
    AT("AT", "Acréscimo – ICMS", "Valores relativos a acréscimos incidentes sobre operações sujeitas ao ICMS");

    private final String codigo;
    private final String nome;
    private final String conteudo;

    public static PafTotalizadorParcialEnum fromCodigo(String codigo) {
        for (PafTotalizadorParcialEnum e : values()) {
            if (e.codigo.equalsIgnoreCase(codigo)) return e;
        }
        throw new IllegalArgumentException("Código de totalizador parcial inválido: " + codigo);
    }
}
