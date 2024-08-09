package backend.microservices.account.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBalanceRequest {
    private String email;
    private String accountNumber;
}
