package br.inf.rcconsultoria.model;

import br.inf.rcconsultoria.enums.PafOrigemProduto;
import br.inf.rcconsultoria.enums.PafSituacaoTributaria;
import br.inf.rcconsultoria.enums.PafTipoAjusteArredondamento;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Registro P2 - Formas de pagamento utilizadas.
 * Detalha os pagamentos realizados por tipo (dinheiro, cartão, etc.),
 * incluindo valor total por meio de pagamento e número de parcelas.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PafP2 implements PafRegistro {

    private String cnpj; // 14 dígitos, sem máscara
    private String codigoProduto; // 14 caracteres
    private String cest; // 7 caracteres
    private String ncm; // 8 caracteres
    private String descricao; // 50 caracteres
    private String unidade; // 6 caracteres
    private PafTipoAjusteArredondamento tipoAjuste; // IAT - 1 caractere
    private PafOrigemProduto origemProduto; // IPPT - 1 caractere
    private PafSituacaoTributaria situacaoTributaria; // 1 caractere
    private BigDecimal aliquota; // 4 dígitos (2 casas decimais)
    private BigDecimal valorUnitario; // 14 dígitos (2 casas decimais)

    @Override
    public String toLinha() {
        return "P2" +
                padLeft(cnpj, 14) +
                padRight(codigoProduto, 14) +
                padRight(cest, 7) +
                padRight(ncm, 8) +
                padRight(descricao, 50) +
                padRight(unidade, 6) +
                tipoAjuste.getCodigo() +
                origemProduto.getCodigo() +
                situacaoTributaria.getCodigo() +
                formatAliquota() +
                padLeft(formatValorCentavos(valorUnitario), 14);
    }

    private String formatAliquota() {
        if (situacaoTributaria == PafSituacaoTributaria.T || situacaoTributaria == PafSituacaoTributaria.S) {
            return padLeft(aliquota.setScale(2, RoundingMode.HALF_DOWN).movePointRight(2).toBigInteger().toString(), 4);
        }
        return "0000";
    }

    private String formatValorCentavos(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_DOWN).movePointRight(2).toBigInteger().toString();
    }

    @SuppressWarnings("squid:S3457")
    private String padRight(String texto, int tamanho) {
        if (texto == null) texto = "";
        return String.format("%-" + tamanho + "s", texto);
    }

    @SuppressWarnings("squid:S3457")
    private String padLeft(String texto, int tamanho) {
        if (texto == null) texto = "";
        return String.format("%" + tamanho + "s", texto).replace(' ', '0');
    }
}


