package ru.difembaxio.util;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.difembaxio.dto.SignerDto;
import ru.difembaxio.dto.ModelMapperDocumentsDto;
import ru.difembaxio.model.Signer;
import ru.difembaxio.repository.SignerRepository;

@RequiredArgsConstructor
@Slf4j
@EnableRabbit
@Service
public class RabbitMqListener {

    private final ModelMapperDocumentsDto modelMapperDocumentsDto;
    private final SignerRepository signerRepository;
    private static final String QUEUE1 = "queue1";
    @RabbitListener(queues = QUEUE1)
    public void handleMessage(SignerDto signerDto) throws NoSuchAlgorithmException, NoSuchProviderException {
        Signer newSigner = modelMapperDocumentsDto.toSigner(signerDto);
        KeyPair keyPair = KeyService.generateRsaKeyPair();
        String publicKey = KeyService.encodeKeyToBase64(keyPair.getPublic());
        String privateKey = KeyService.encodeKeyToBase64(keyPair.getPrivate());
        newSigner.setPublic_key(publicKey);
        newSigner.setPrivate_key(privateKey);
        signerRepository.save(newSigner);
       log.info(String.format("Получен из очереди %s подписант документов пользователь %s", QUEUE1, signerDto.getUsername()));
    }

}
