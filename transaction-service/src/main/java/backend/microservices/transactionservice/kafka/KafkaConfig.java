package backend.microservices.transactionservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic transactionTopic() {
        return new NewTopic("transaction-event", 10, (short) 1);
    }
}
