package io.bayrktlihn.payment.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class SoapInterceptor implements ClientInterceptor {
    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        log.info("### SOAP REQUEST START###");
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            messageContext.getRequest().writeTo(buffer);
            String payload = buffer.toString(StandardCharsets.UTF_8.name());
            log.info(payload);
            log.info("### SOAP REQUEST END###");

        } catch (IOException e) {
            throw new WebServiceClientException("Can not write the SOAP request into the out stream", e) {
                private static final long serialVersionUID = 6231909088178053520L;
            };
        }
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        log.info("### SOAP RESPONSE START ###");
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            messageContext.getResponse().writeTo(buffer);
            String payload = buffer.toString(StandardCharsets.UTF_8.name());
            log.info(payload);
            log.info("### SOAP RESPONSE END ###");
        } catch (IOException e) {
            throw new WebServiceClientException("Can not write the SOAP response into the out stream", e) {
                private static final long serialVersionUID = 1267886257853815252L;
            };
        }

        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        log.info("### SOAP FAULT ###");
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            messageContext.getResponse().writeTo(buffer);
            String payload = buffer.toString(StandardCharsets.UTF_8.name());
            log.info(payload);
        } catch (IOException e) {
            throw new WebServiceClientException("Can not write the SOAP fault into the out stream", e) {
                private static final long serialVersionUID = -1634526981814972724L;
            };
        }

        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {
        log.info("### SOAP afterCompletion ###");
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            messageContext.getResponse().writeTo(buffer);
            String payload = buffer.toString(StandardCharsets.UTF_8.name());
            log.info(payload);
        } catch (IOException e) {
            throw new WebServiceClientException("Can not write the SOAP fault into the out stream", e) {
                private static final long serialVersionUID = 8124330154555966065L;
            };
        }
    }
}