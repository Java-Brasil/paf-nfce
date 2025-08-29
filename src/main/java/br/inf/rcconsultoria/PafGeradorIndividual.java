package br.inf.rcconsultoria;

import br.inf.rcconsultoria.model.Paf;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class PafGeradorIndividual {

    private final Paf paf;
    private final Path pastaDestino;

    public PafGeradorIndividual(Paf paf, Path pastaDestino) {
        this.paf = paf;
        this.pastaDestino = pastaDestino;
    }

    public void gerarArquivo() throws IOException, PafException {
        String nomeGerado = gerarNomeComDataHora(Optional.ofNullable(paf.getNomeArquivo()).orElse(paf.getNumeroArquivo().getNomeArquivoPadrao()));
        Path caminho = pastaDestino.resolve(nomeGerado);
        try (Writer writer = new OutputStreamWriter(Files.newOutputStream(caminho), StandardCharsets.ISO_8859_1)) {
            writer.write(gerarString());
        }
    }

    public String gerarString() throws PafException {
        return gerarStringBuilder().toString();
    }

    public StringBuilder gerarStringBuilder() throws PafException {
        StringBuilder sb = new StringBuilder();
        sb.append(paf.getRegistroU1().toLinha()).append("\r\n");

        switch (paf.getNumeroArquivo()) {
            case I:
                paf.getRegistrosA2().forEach(r -> sb.append(r.toLinha()).append("\r\n"));
                paf.getRegistrosP2().forEach(r -> sb.append(r.toLinha()).append("\r\n"));
                paf.getRegistrosE2().forEach(r -> sb.append(r.toLinha()).append("\r\n"));
                paf.getRegistrosJ1().forEach(r -> sb.append(r.toLinha()).append("\r\n"));
                paf.getRegistrosJ2().forEach(r -> sb.append(r.toLinha()).append("\r\n"));
                break;
            case II:
                paf.getRegistrosE2().forEach(r -> sb.append(r.toLinha()).append("\r\n"));
                break;
            case IV:
                paf.getRegistrosD2().forEach(r -> sb.append(r.toLinha()).append("\r\n"));
                paf.getRegistrosD3().forEach(r -> sb.append(r.toLinha()).append("\r\n"));
                paf.getRegistrosD4().forEach(r -> sb.append(r.toLinha()).append("\r\n"));
                break;
            default:
                throw new PafException("Número de arquivo não suportado: " + paf.getNumeroArquivo());
        }

        return sb;
    }

    private String gerarNomeComDataHora(String nomeBase) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return nomeBase.replace("." + "txt", "") + "_" + timestamp + "." + "txt";
    }
}

