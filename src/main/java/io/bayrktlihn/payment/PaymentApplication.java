package io.bayrktlihn.payment;

import io.bayrktlihn.payment.client.VakifBankClient;
import io.bayrktlihn.soap.ws.client.ExecuteVposRequestResponse;
import io.bayrktlihn.soap.ws.client.VposRequest;
import io.bayrktlihn.soap.ws.client.VposResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class PaymentApplication implements CommandLineRunner {

    private final VakifBankClient vakifBankClient;

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        VposRequest vposRequest = new VposRequest();
        vposRequest.setPan("5454545454545454");
        vposRequest.setCvv("855");
        vposRequest.setExpiry("2305");
        ExecuteVposRequestResponse executeVposRequestResponse = vakifBankClient.executeVposRequest(vposRequest);

        VposResponse vposResponse = executeVposRequestResponse.getExecuteVposRequestResult();
        String resultCode = vposResponse.getResultCode();

        System.out.println(resultCode);

    }
}
