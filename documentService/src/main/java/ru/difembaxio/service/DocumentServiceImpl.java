package ru.difembaxio.service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.difembaxio.dto.DocumentDto;
import ru.difembaxio.dto.ModelMapperDocumentsDto;
import ru.difembaxio.exception.DocumentNotFoundException;
import ru.difembaxio.exception.SignerNotFoundException;
import ru.difembaxio.exception.SignerPrivateKeyNotFoundException;
import ru.difembaxio.model.Document;
import ru.difembaxio.model.Signer;
import ru.difembaxio.repository.DocumentRepository;
import ru.difembaxio.repository.SignerRepository;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final SignerRepository signerRepository;
    private final DocumentRepository documentRepository;
    private final ModelMapperDocumentsDto mapperDocumentsDto;

    @Override
    public DocumentDto crateDocument(String signerName, DocumentDto documentDto) {
        Signer signerFromDb = signerRepository.findByUsernameIgnoreCase(signerName)
            .orElseThrow(() -> new SignerNotFoundException
                (String.format("Подписан с именем %s не найден", signerName)));
        Document documentFromDb = mapperDocumentsDto.toDocument(documentDto);
        documentFromDb.setAlgorithm("SHA256withRSA");
        documentFromDb.setCreatedAt(LocalDateTime.now());
        documentFromDb.setSigner(signerFromDb);
        return mapperDocumentsDto.toDto(documentRepository.save(documentFromDb));
    }

    @Override
    public Optional<Signer> findSignerByDocuments_Id(Long documentId) {
        return documentRepository.findAll().stream()
            .filter(document -> document.getId().equals(documentId))
            .map(Document::getSigner)
            .findFirst();
    }

    @Override
    public String getSignerPrivateKey(String signerName) {
        return signerRepository.findByUsernameIgnoreCase(signerName)
            .map(Signer::getPrivate_key)
            .orElseThrow(() -> new SignerPrivateKeyNotFoundException
                ("Секретный ключ пользователя " + signerName + " не найден"));
    }

    @Override
    public byte[] signDocument(Long documentId, String signerName) {
        String encryptedPrivateKey = getSignerPrivateKey(signerName);

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(encryptedPrivateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(
                    String.format("Документ с ID %d не найден", documentId)));
            if (!document.getSigner().getUsername().equalsIgnoreCase(signerName)) {
                throw new IllegalArgumentException("Документ не принадлежит указанному подписанту");
            }
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(document.getContent());
            byte[] signedBytes = signature.sign();
            String base64PublicKey = document.getSigner().getPublic_key();
            boolean isValid = verifySignature(document.getContent(), signedBytes, base64PublicKey);
            if (!isValid) {
                throw new RuntimeException("Подпись недействительна, сохранение отменено");
            }
            document.setSignature(signedBytes);
            documentRepository.save(document);
            return signedBytes;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при подписи документа: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Document> getDocumentsBySigner(Long signerId) {
        return documentRepository.getDocumentListBySigner(signerId);
    }

    @Override
    public boolean verifySignature(byte[] content, byte[] signature, String base64PublicKey) {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(base64PublicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(content);

            return sig.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при проверке подписи: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteDocumentBySigner(Long signerId) {
        Document documentFromDb = documentRepository.getDocumentsBySignerId(signerId);
        documentRepository.delete(documentFromDb);
    }
}
