package ru.difembaxio.service;

import java.util.List;
import java.util.Optional;
import ru.difembaxio.dto.DocumentDto;
import ru.difembaxio.model.Document;
import ru.difembaxio.model.Signer;

public interface DocumentService {

    DocumentDto crateDocument(String signerName, DocumentDto documentDto);
    Optional<Signer> findSignerByDocuments_Id(Long documentId);

    String getSignerPrivateKey(String signerName);
    public byte[] signDocument(Long documentId, String signerName);
    List<DocumentDto> getDocumentsBySigner(Long signerId);
    boolean verifySignature(byte[] content, byte[] signature, String base64PublicKey);

    void deleteDocumentBySigner(Long signerId);
}
