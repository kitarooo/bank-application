package backend.microservices.transactionservice.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import main.proto.AccountServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class GrpcClientConfig {

    @Value("${grpc-client.host}")
    private String grpcHost;

    @Value("${grpc-client.port}")
    private int grpcPort;

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
    }

    @Bean
    public AccountServiceGrpc.AccountServiceBlockingStub calculatorServiceBlockingStub(final ManagedChannel managedChannelCalculator) {
        return AccountServiceGrpc.newBlockingStub(managedChannelCalculator);
    }

}
