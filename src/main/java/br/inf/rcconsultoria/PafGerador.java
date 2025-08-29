package br.inf.rcconsultoria;

import br.com.swconsultoria.certificado.Certificado;
import br.inf.rcconsultoria.model.Paf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PafGerador {

    private List<Paf> pafs;
    private Certificado certificado;

    private Path pastaDestino = Paths.get(System.getProperty("user.dir") + File.separator + "paf");

    public PafGerador(Paf paf, Certificado certificado) {
        this.pafs = Collections.singletonList(paf);
        this.certificado = certificado;
    }

    public PafGerador(List<Paf> pafs, Certificado certificado) {
        this.pafs = pafs;
        this.certificado = certificado;
    }

    public void gerar() throws Exception {
        validador();
        for (Paf paf : pafs) {
            PafGeradorIndividual pafGeradorIndividual = new PafGeradorIndividual(paf, pastaDestino);
            pafGeradorIndividual.gerarArquivo();
        }
        gerarXmlComposto();
    }

    public void validador() throws PafException {
        if (pafs == null) throw new PafException("Lista de PAF's não informada.");

        for (Paf paf : pafs) {
            if (paf.getRegistroU1() == null) throw new PafException("Registro U1 é obrigatório.");
            if (paf.getNumeroArquivo() == null) throw new PafException("Número do arquivo não informado.");

            switch (paf.getNumeroArquivo()) {
                case I:
                    if (paf.getRegistrosA2().isEmpty() &&
                            paf.getRegistrosP2().isEmpty() &&
                            paf.getRegistrosE2().isEmpty() &&
                            paf.getRegistrosJ1().isEmpty() &&
                            paf.getRegistrosJ2().isEmpty()) {
                        throw new PafException("Para número de arquivo I (MOVIMENTO.TXT), é necessário ao menos um registro adicional (A2, P2, E2, J1, J2).");
                    }
                    break;
                case II:
                    if (paf.getRegistrosE2().isEmpty()) {
                        throw new PafException("Para número de arquivo II (ESTOQUE.TXT), é necessário ao menos um registro E2.");
                    }
                    break;
                case IV:
                    if (paf.getRegistrosD2().isEmpty()) {
                        throw new IllegalStateException("Para número de arquivo IV (DAV.TXT), é necessário ao menos um registro D2.");
                    }
                    break;
                // Adicione os demais casos (III, IV) conforme expandir
                default:
                    throw new PafException("Número de arquivo não suportado: " + paf.getNumeroArquivo());
            }

            if (pastaDestino == null) {
                throw new PafException("Pasta de destino não definida.");
            }

            if (!Files.exists(pastaDestino)) {
                try {
                    Files.createDirectories(pastaDestino);
                } catch (IOException e) {
                    throw new PafException("Erro ao criar pasta de destino: " + pastaDestino, e);
                }
            }
        }
    }

    public void gerarXmlComposto() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document document = db.newDocument();
        Element menuFiscal = document.createElementNS("http://www.sef.sc.gov.br/nfce", "menuFiscal");
        document.appendChild(menuFiscal);

        for (Paf paf : pafs) {
            String conteudo = new PafGeradorIndividual(paf, pastaDestino).gerarString();
            String base64 = Base64.getEncoder().encodeToString(conteudo.getBytes(StandardCharsets.ISO_8859_1));

            Element arquivo = document.createElement("arquivo");
            LocalDateTime agora = LocalDateTime.now();
            String data = agora.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            String hora = agora.format(DateTimeFormatter.ofPattern("HHmmss"));

            arquivo.setAttribute("nroArquivo", paf.getNumeroArquivo().getCodigo());
            arquivo.setAttribute("data", data);
            arquivo.setAttribute("hora", hora);
            if (paf.getBancoDados() != null && !paf.getBancoDados().isEmpty()) {
                arquivo.setAttribute("arqBD", paf.getBancoDados());
            }
            if (paf.getSistema() != null && !paf.getSistema().isEmpty()) {
                arquivo.setAttribute("arqSist", paf.getSistema());
            }

            arquivo.appendChild(document.createCDATASection(base64));
            menuFiscal.appendChild(arquivo);
        }

        assinarEExportar(document);
    }

    private void assinarEExportar(Document document) throws Exception {
        // Converte o Document para String
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(document), new StreamResult(new OutputStreamWriter(output, StandardCharsets.UTF_8)));
        String xmlString = output.toString(StandardCharsets.UTF_8.name());

        // Assina usando a nova classe utilitária
        String xmlAssinado = XmlAssinador.assinarXmlRaiz(xmlString, certificado);

        // Salva o XML assinado no destino
        Path destino = pastaDestino.resolve("menuFiscal.xml");
        Files.write(destino, xmlAssinado.getBytes(StandardCharsets.UTF_8));
    }

}
