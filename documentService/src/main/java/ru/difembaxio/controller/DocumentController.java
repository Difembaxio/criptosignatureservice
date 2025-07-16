package ru.difembaxio.controller;

import jakarta.validation.Valid;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.difembaxio.dto.DocumentDto;
import ru.difembaxio.model.Document;
import ru.difembaxio.model.Signer;
import ru.difembaxio.service.DocumentService;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/create/{signerName}")
    public DocumentDto createDocument(@Valid @PathVariable String signerName,
        @Valid @RequestBody DocumentDto documentDto){
        return documentService.crateDocument(signerName,documentDto);
    }

    @GetMapping("/signer/{documentId}")
    public Optional<Signer> getSignerByDocumentId(@Valid @PathVariable Long documentId){
        return documentService.findSignerByDocuments_Id(documentId);
    }

    @PostMapping("/sign/{documentId}/{signerName}")
    public String signDocument(@PathVariable Long documentId, @PathVariable String signerName) {
        byte[] signature = documentService.signDocument(documentId, signerName);
        return Base64.getEncoder().encodeToString(signature);
    }
    @GetMapping("/list/{signerId}")
    public List<Document> getDocumentsBySigner(@Valid @PathVariable Long signerId){
        return documentService.getDocumentsBySigner(signerId);
    }

    @DeleteMapping("/delete/{signerId}")
    public void deleteDocumentsBySigner(@Valid @PathVariable Long signerId){
        documentService.deleteDocumentBySigner(signerId);
    }
}
