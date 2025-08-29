package br.inf.rcconsultoria.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PafNumeroArquivo {

    I("I", "MOVIMENTO"),
    II("II", "ESTOQUE"),
    III("III", "ENCERRAMENTO"), // ENCERRAMENTO DO MOVIMENTO - U1, registros Z, redução Z, etc.¹ somente legados
    IV("IV", "DAV"); // U1, D2, D3, D4 (relativos a DAVs)

    private final String codigo;
    private final String nomeArquivoPadrao;
}

