package ru.difembaxio.dto;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.difembaxio.model.Document;
import ru.difembaxio.model.Signer;

@Component
@RequiredArgsConstructor
public class ModelMapperDocumentsDto {

    private final ModelMapper modelMapper;

    public Signer toSigner(SignerDto signerDto) {
        return modelMapper.map(signerDto, Signer.class);
    }

    public DocumentDto toDto(Document document){
        return modelMapper.map(document, DocumentDto.class);
    }
    public Document toDocument (DocumentDto documentDto){
        return modelMapper.map(documentDto, Document.class);
    }
}