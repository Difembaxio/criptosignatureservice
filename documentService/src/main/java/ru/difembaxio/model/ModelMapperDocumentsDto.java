package ru.difembaxio.model;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.difembaxio.dto.SignerDto;

@Component
@RequiredArgsConstructor
public class ModelMapperDocumentsDto {

    private final ModelMapper modelMapper;

    public Signer toSigner(SignerDto signerDto) {
        return modelMapper.map(signerDto, Signer.class);
    }
}