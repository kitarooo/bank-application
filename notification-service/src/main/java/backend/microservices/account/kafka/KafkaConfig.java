package backend.microservices.account.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Bean
    public DefaultJackson2JavaTypeMapper typeMapper() {
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("AccountCreatedRequest", backend.microservices.account.kafka.event.AccountCreatedRequest.class);
        mappings.put("UpdateBalanceRequest", backend.microservices.account.kafka.event.UpdateBalanceRequest.class);
        typeMapper.setIdClassMapping(mappings);
        return typeMapper;
    }

    @Bean
    public JsonDeserializer<Object> jsonDeserializer() {
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
        deserializer.setTypeMapper(typeMapper());
        deserializer.addTrustedPackages("backend.microservices.account.kafka.event");
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