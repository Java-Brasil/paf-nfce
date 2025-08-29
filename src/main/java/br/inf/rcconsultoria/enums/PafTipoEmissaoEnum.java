package br.inf.rcconsultoria.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PafTipoEmissaoEnum {
    NORMAL("1", "Emissão normal"),
    CONTINGENCIA_FS_IA("2", "Contingência FS-IA"),
    CONTINGENCIA_SCAN("3", "Contingência SCAN"),
    DPEC("4", "DPEC"),
    FS_DA("5", "Contingência FS-DA"),
    SVC_AN("6", "SVC-AN"),
    SVC_RS("7", "SVC-RS"),
    OFFLINE("9", "Contingência Offline NFC-e");

    private final String codigo;
    private final String descricao;
}

