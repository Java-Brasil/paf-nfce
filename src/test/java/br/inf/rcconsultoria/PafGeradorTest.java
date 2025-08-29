package br.inf.rcconsultoria;

import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.inf.rcconsultoria.enums.PafNumeroArquivo;
import br.inf.rcconsultoria.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PafGeradorTest {

    @Test
    void deveGerarDavTxtEAssinarXml() throws Exception {
        // Certificado A1 (.pfx) para teste
        Path caminhoPfx = Paths.get("src", "test", "resources", "NaoUsar_CNPJ.pfx");
        byte[] pfxBytes = Files.readAllBytes(caminhoPfx);
        Certificado certificado = CertificadoService.certificadoPfxBytes(pfxBytes, "123456");

        // Configuração PAF
        // Monta instância Paf
        Paf paf = new Paf();
        paf.setNumeroArquivo(PafNumeroArquivo.IV);
        paf.setSistema("TESTE-PAF");
        paf.setBancoDados("TESTE-BD");
        paf.setNomeArquivo("DAV");

        // U1 obrigatório
        paf.setRegistroU1(PafU1.builder()
                .cnpj("12345678000195")
                .inscricaoEstadual("123456789")
                .inscricaoMunicipal("987654321")
                .razaoSocial("EMPRESA DE TESTE LTDA")
                .build());

        // D2
        paf.getRegistrosD2().add(PafD2.builder()
                .cnpj("12345678000195")
                .dataEmissao(LocalDate.now())
                .numeroDav("0001234567")
                .nomeCliente("JOÃO DA SILVA")
                .cpfCnpj("12345678901")
                .placaVeiculo("ABC1D23")
                .build());

        // D3
        paf.getRegistrosD3().add(PafD3.builder()
                .cnpj("12345678000195")
                .dataEmissao(LocalDate.now())
                .numeroDav("0001234567")
                .numeroItem(1)
                .codigoProduto("PROD001")
                .descricao("Produto Teste")
                .unidade("UN")
                .quantidade(new BigDecimal("2.5"))
                .valorUnitario(new BigDecimal("15.90"))
                .build());

        // D4
        paf.getRegistrosD4().add(PafD4.builder()
                .cnpj("12345678000195")
                .dataEmissao(LocalDate.now())
                .numeroDav("0001234567")
                .dataCancelamento(LocalDate.now())
                .build());

        // Gera arquivos
        PafGerador gerador = new PafGerador(paf, certificado);
        // Pasta destino temporária para teste
        Path pastaDestino = Files.createTempDirectory("paf-teste");
        gerador.setPastaDestino(pastaDestino);
        gerador.gerar();

        // Verifica se arquivos existem
        assertTrue(Files.exists(pastaDestino), "Pasta de destino não criada");
        assertTrue(Files.list(pastaDestino)
                .anyMatch(p -> p.getFileName().toString().endsWith(".xml")), "Arquivo XML não gerado");
        assertTrue(Files.list(pastaDestino)
                .anyMatch(p -> p.getFileName().toString().endsWith(".txt")), "Arquivo TXT não gerado");
    }
}

