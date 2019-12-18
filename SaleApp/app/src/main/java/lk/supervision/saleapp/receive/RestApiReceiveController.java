package lk.supervision.saleapp.receive;

import android.util.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import lk.supervision.saleapp.constant.AppEnvironmentValues;
import lk.supervision.saleapp.model.MCardDetails;
import lk.supervision.saleapp.model.MSperson;
import lk.supervision.saleapp.model.MTransactionData;

/**
 * Created by kavish manjitha on 10/10/2017.
 */

public class RestApiReceiveController {

    public MCardDetails[] getCardDetails(String spId) {
        final String url = AppEnvironmentValues.SERVER_ADDRESS + "/api/v1/rainbow/card-detail/get-card-details/" + spId;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        MCardDetails[] response = restTemplate.getForObject(url, MCardDetails[].class);
        return response;
    }

    public MSperson[] getMSperson() {
        final String url = AppEnvironmentValues.SERVER_ADDRESS + "/api/v1/rainbow/sperson/get-active-users";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        MSperson[] response = restTemplate.getForObject(url, MSperson[].class);
        return response;
    }

    public Integer saveTransactionData(MTransactionData mTransactionData) {
        final String url = AppEnvironmentValues.SERVER_ADDRESS + "/api/v1/rainbow/mobile-data/save-mobile-transaction-data";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        Integer response = restTemplate.postForObject(url, mTransactionData, Integer.class);
        return response;
    }

    public String saveSpersondata(String spId, Integer lastPaymentSerial) {
        final String url = AppEnvironmentValues.SERVER_ADDRESS + "/api/v1/rainbow/sperson/save-sperson-last-payment-serial-sync/" + spId + "/" + lastPaymentSerial;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }


    public MCardDetails getManualCardSearch(String data, String type) {
        final String url = AppEnvironmentValues.SERVER_ADDRESS + "/api/v1/rainbow/card-detail/manual-card-details-search/" + data + "/" + type;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        MCardDetails responseData = restTemplate.getForObject(url, MCardDetails.class);
        return responseData;
    }

    public MCardDetails[] getManualCardDetailsBy(String data, String filter) {
        final String url = AppEnvironmentValues.SERVER_ADDRESS + "/api/v1/rainbow/card-detail/manual-card-details-search-by-customer-name/" + data + "/" + filter;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        MCardDetails[] response = restTemplate.getForObject(url, MCardDetails[].class);
        return response;
    }
}
