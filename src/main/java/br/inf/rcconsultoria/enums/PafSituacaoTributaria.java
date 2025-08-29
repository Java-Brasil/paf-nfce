package br.inf.rcconsultoria.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PafSituacaoTributaria {
    I("Isento"),
    N("Não Tributado"),
    F("Substituição Tributária"),
    T("Tributado pelo ICMS"),
    S("Tributado pelo ISSQN");

    private final String descricao;

    public String getCodigo() {
        return this.name();
    }
}

