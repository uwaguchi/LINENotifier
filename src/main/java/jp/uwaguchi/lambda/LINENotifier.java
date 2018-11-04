package jp.uwaguchi.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

public class LINENotifier implements RequestHandler<String, String> {
    private static final String ep = "https://notify-api.line.me/api/notify";

    @Override
    public String handleRequest(String type, Context context){
        notify("テストメッセージだよ");
        return "ok";
    }

    private void notify(String message){
        try {
            URI uri = new URI(ep);
            String token = System.getenv("ENV_TOKEN");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("message", message);

            RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());
            RequestEntity req = RequestEntity
                    .post(uri)
                    .header("Authorization", "Bearer " + token)
                    .contentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, Charset.forName("utf-8")))
                    .body(params);

            ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        } catch(URISyntaxException e) {
        }

        return;
    }

    public static void main(String[] args){
        LINENotifier no = new LINENotifier();
        no.notify("いまからお父さんが帰るわよん");
        return;
    }
}
