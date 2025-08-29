package br.inf.rcconsultoria.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Registro U1 - Identificação da empresa usuária do PAF-NFC-e.
 * Contém dados cadastrais da empresa, como CNPJ, razão social,
 * IE (inscrição estadual) e IM (inscrição municipal).
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PafU1 implements PafRegistro {

    private String cnpj;
    private String inscricaoEstadual;
    private String inscricaoMunicipal;
    private String razaoSocial;

    @Override
    public String toLinha() {
        return String.format("U1%-14s%-14s%-14s%-50s",
                padLeft(cnpj),
                padRight(inscricaoEstadual, 14),
                padRight(inscricaoMunicipal, 14),
                padRight(razaoSocial, 50)
        );
    }

    @SuppressWarnings("squid:S3457")
    private String padRight(String texto, int tamanho) {
        return String.format("%-" + tamanho + "s", texto);
    }

    @SuppressWarnings("squid:S3457")
    private String padLeft(String texto) {
        return String.format("%" + 14 + "s", texto).replace(' ', '0');
    }
}
