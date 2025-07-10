package ru.difembaxio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.difembaxio.dto.SignerDto;
import ru.difembaxio.model.ModelMapperDocumentsDto;
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
    public void handleMessage(SignerDto signerDto) {
        Signer newSigner = modelMapperDocumentsDto.toSigner(signerDto);
        signerRepository.save(newSigner);
       log.info(String.format("Получен из очереди %s подписант документов пользователь %s", QUEUE1, signerDto.getUsername()));
    }

}
