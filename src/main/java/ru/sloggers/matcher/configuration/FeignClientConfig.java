package ru.sloggers.matcher.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import ru.sloggers.matcher.services.client.RecognitionClient;

@Configuration
@EnableFeignClients(clients = RecognitionClient.class)
public class FeignClientConfig {
}
