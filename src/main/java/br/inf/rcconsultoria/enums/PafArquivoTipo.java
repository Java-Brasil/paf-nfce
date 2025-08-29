package br.inf.rcconsultoria.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public enum PafArquivoTipo {

    MOVIMENTO("I", "MOVIMENTO.TXT"),
    ESTOQUE("J", "ESTOQUE.TXT"),
    ENCERRAMENTO("K", "ENCERRAMENTO.TXT"),
    DAV("L", "DAV.TXT"),
    VERSAO("M", "VERSAO.TXT");

    private final String codigo;      // Valor usado no XML <arquivo nroArquivo="...">
    private final String nomeArquivo; // Nome padrão do arquivo gerado
}
