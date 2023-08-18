package io.bayrktlihn.payment.client;

import io.bayrktlihn.soap.ws.client.ExecuteVposRequest;
import io.bayrktlihn.soap.ws.client.ExecuteVposRequestResponse;
import io.bayrktlihn.soap.ws.client.VposRequest;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class VakifBankClient extends WebServiceGatewaySupport {

    public ExecuteVposRequestResponse executeVposRequest(VposRequest vposRequest) {
        WebServiceTemplate webServiceTemplate = getWebServiceTemplate();

        ExecuteVposRequest executeVposRequest = new ExecuteVposRequest();
        executeVposRequest.setVposRequest(vposRequest);

        return (ExecuteVposRequestResponse) webServiceTemplate.marshalSendAndReceive(executeVposRequest, new SoapActionCallback("PayFlexVPosWebService/ExecuteVposRequest"));
    }


}
