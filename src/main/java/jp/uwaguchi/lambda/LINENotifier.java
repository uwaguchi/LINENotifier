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
import java.util.Map;
import java.util.Random;

public class LINENotifier implements RequestHandler<Map<String,Object>, String> {
    private static final String ep = "https://notify-api.line.me/api/notify";

    @Override
    public String handleRequest(Map<String,Object> input, Context context){
        notify(getNotifyMessage());
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

    private String getNotifyMessage(){
        String subjects[] = {
                "お父さんは",
                "パパは"
        };
        String predicates[] = {
                "今から帰ります！",
                "今からかえります！",
                "今から帰りますー",
                "今からかえりますー",
                "今でましたー",
                "いまでました～",
                "いまからかえるよー"
        };

        Random rnd = new Random();
        String s = subjects[rnd.nextInt(subjects.length)];
        String p = predicates[rnd.nextInt(predicates.length)];

        return s + p;
    }
    public static void main(String[] args){
        LINENotifier no = new LINENotifier();
        String s = no.getNotifyMessage();
        no.notify(s);
        return;
    }
}
