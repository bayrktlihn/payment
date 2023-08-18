package io.bayrktlihn.payment.config;

import io.bayrktlihn.payment.client.SoapInterceptor;
import io.bayrktlihn.payment.client.VakifBankClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Configuration
public class VakifBankClientConfig {

    @Bean
    Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("io.bayrktlihn.soap.ws.client");
        return marshaller;
    }

    @Bean
    VakifBankClient soapClient(Jaxb2Marshaller marshaller) {
        VakifBankClient soapClient = new VakifBankClient();
        soapClient.setMarshaller(marshaller);
        soapClient.setUnmarshaller(marshaller);
        soapClient.setDefaultUri("https://onlineodemetest.vakifbank.com.tr:4443/VposService/TransactionServices.asmx");
        soapClient.setInterceptors(new ClientInterceptor[]{new SoapInterceptor()});
        return soapClient;
    }
}
