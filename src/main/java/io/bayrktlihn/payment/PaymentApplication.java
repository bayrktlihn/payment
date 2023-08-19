package io.bayrktlihn.payment;

import io.bayrktlihn.payment.client.VakifBankClient;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class PaymentApplication implements CommandLineRunner {

    private final VakifBankClient vakifBankClient;
    @Autowired
    private List<Filter> filterList;

    public PaymentApplication(VakifBankClient vakifBankClient) {
        this.vakifBankClient = vakifBankClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (Filter filter : filterList) {
            System.out.println(filter);
        }
        //        VposRequest vposRequest = new VposRequest();
//        vposRequest.setPan("5454545454545454");
//        vposRequest.setCvv("855");
//        vposRequest.setExpiry("2305");
//        ExecuteVposRequestResponse executeVposRequestResponse = vakifBankClient.executeVposRequest(vposRequest);
//
//        VposResponse vposResponse = executeVposRequestResponse.getExecuteVposRequestResult();
//        String resultCode = vposResponse.getResultCode();
//
//        System.out.println(resultCode);

    }
}
