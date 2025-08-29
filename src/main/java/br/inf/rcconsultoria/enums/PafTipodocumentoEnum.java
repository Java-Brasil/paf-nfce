package br.inf.rcconsultoria.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PafTipodocumentoEnum {

    NFCE("1"),
    NFE("2"),
    NAO_TRIBUTAVEL("3");

    private final String codigo;

}
