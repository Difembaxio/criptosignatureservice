package ru.difembaxio.dto;


import java.time.LocalDateTime;
import lombok.Data;
import ru.difembaxio.model.Signer;

@Data
public class DocumentDto {
    private String title;
    private byte[] content;
    private byte[] signature;
    private String algorithm;
    private LocalDateTime createdAt;
    private Signer signer;
}
