package ru.difembaxio;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.difembaxio.dto.SignerDto;
import ru.difembaxio.service.RabbitMqListener;

@RestController
@RequestMapping("/signer")
@RequiredArgsConstructor
public class SignerController {

    private final RabbitMqListener rabbitMqListener;


    @PostMapping
    public void saveSigner(@RequestBody SignerDto signerDto){
        rabbitMqListener.handleMessage(signerDto);
    }

}
