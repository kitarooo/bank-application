package backend.microservices.transactionservice.kafka;

import backend.microservices.kafka.event.AccountCreatedRequest;
import backend.microservices.kafka.event.UpdateBalanceRequest;
import backend.microservices.transactionservice.kafka.event.TransactionDescendingBalance;
import backend.microservices.transactionservice.kafka.event.TransactionUpdateBalance;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class KafkaConfig {

    @Bean
    public NewTopic accountTopic() {
        return new NewTopic("account-event", 10, (short) 1);
    }

    @Bean
    public DefaultJackson2JavaTypeMapper typeMapper() {
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("AccountCreatedRequest", AccountCreatedRequest.class);
        mappings.put("TransactionUpdateBalance", TransactionUpdateBalance.class);
        mappings.put("TransactionDescendingBalance", TransactionDescendingBalance.class);
        mappings.put("UpdateBalanceRequest", UpdateBalanceRequest.class);
        typeMapper.setIdClassMapping(mappings);
        return typeMapper;
    }

    @Bean
    public JsonDeserializer<Object> jsonDeserializer() {
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
        deserializer.setTypeMapper(typeMapper());
        deserializer.addTrustedPackages("backend.microservices.kafka.event");
        return deserializer;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        return objectMapper;
    }
    @Bean
    public StringJsonMessageConverter converter() {
        return new StringJsonMessageConverter(objectMapper());
    }
}
