package backend.microservices.account.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic accountTopic() {
        return new NewTopic("account-event", 2, (short) 1);
    }
}
