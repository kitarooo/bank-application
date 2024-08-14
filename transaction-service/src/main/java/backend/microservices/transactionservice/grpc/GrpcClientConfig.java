package backend.microservices.transactionservice.grpc;

import brave.Tracer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GrpcClientConfig {

    /*@Value("${grpc-client.host}")
    private String grpcHost;

    @Value("${grpc-client.port}")
    private int grpcPort;*/

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9999)
                .usePlaintext()
                .idleTimeout(10, TimeUnit.MINUTES)
                .build();
    }

}
