package br.inf.rcconsultoria;

import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import lombok.experimental.UtilityClass;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class XmlAssinador {

    public String assinarXmlRaiz(String xml, Certificado certificado) throws Exception {
        Document document = carregarXml(xml);

        // Fábricas
        XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance("DOM");
        KeyStore ks = CertificadoService.getKeyStore(certificado);

        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(certificado.getNome(), new KeyStore.PasswordProtection(certificado.getSenha().toCharArray()));

        PrivateKey privateKey = pkEntry.getPrivateKey();
        X509Certificate x509Cert = CertificadoService.getCertificate(certificado, ks);

        // KeyInfo
        KeyInfoFactory keyInfoFactory = sigFactory.getKeyInfoFactory();
        X509Data x509Data = keyInfoFactory.newX509Data(Collections.singletonList(x509Cert));
        KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));

        // Transforms
        List<Transform> transforms = Arrays.asList(
                sigFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null),
                sigFactory.newTransform(CanonicalizationMethod.INCLUSIVE, (TransformParameterSpec) null)
        );

        // Referência vazia = assina toda a raiz
        Reference reference = sigFactory.newReference(
                "",
                sigFactory.newDigestMethod(DigestMethod.SHA1, null),
                transforms, null, null
        );

        SignedInfo signedInfo = sigFactory.newSignedInfo(
                sigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                sigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                Collections.singletonList(reference)
        );

        // Assina na raiz
        DOMSignContext signContext = new DOMSignContext(privateKey, document.getDocumentElement());
        XMLSignature signature = sigFactory.newXMLSignature(signedInfo, keyInfo);
        signature.sign(signContext);

        return converterParaString(document);
    }

    private Document carregarXml(String xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(new InputSource(new StringReader(xml)));
    }

    private String converterParaString(Document doc) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            transformer.transform(new DOMSource(doc), new StreamResult(output));
            return output.toString(StandardCharsets.UTF_8.name());
        }
    }
}
