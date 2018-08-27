package ifpb.dac.pos.oauth.twitter;

import ifpb.dac.pos.oauth.Requisicao;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 07/02/2018, 11:38:55
 */
public class ClientTwitter {

    public static void main(String[] args) throws IOException {
                staticMethod();
//        withRequest();

    }

    private static void withRequest() throws IOException {
        Twitter twitter = new Twitter(
                "2URxXsnyMBfn71XTtRs8A",
                "CM8WbuGDFPIQGhkLhFEVQMyCK6sZFq10uXM4IzHQQ",
                "http://localhost:8080/oauth/token"
        );
        Requisicao rq = new Requisicao(
                twitter
        );
        TwitterAutenticate a = new TwitterAutenticate("", "");
        String authorization = a.headerAuthorization("POST", twitter.getUrl_request_token());

        String requestToken = rq.requestToken(authorization, twitter.getUrl_request_token());
        System.out.println("request = " + requestToken);
        Desktop d = Desktop.getDesktop();
        d.browse(URI.create("https://api.twitter.com/oauth/authenticate?" + requestToken));
    }

    private static void staticMethod() throws IOException {
        //Token e verifier vazios
        TwitterAutenticate a = new TwitterAutenticate("", "");
        //Gerar o header
        String authorization = a.headerAuthorization("POST", "https://api.twitter.com/oauth/request_token");
//        System.out.println("authorization = " + authorization);

        String oauth_token = request(authorization, "https://api.twitter.com/oauth/request_token");

        Desktop d = Desktop.getDesktop();
        d.browse(URI.create("https://api.twitter.com/oauth/authenticate?" + oauth_token));
    }

    private static String request(String authorization, String uri) {
        Client newBuilder = ClientBuilder.newBuilder().build();
        WebTarget target = newBuilder.target(uri);
        Builder post = target.request();
        if (authorization != null && !"".equals(authorization.trim())) {
            post = post.header("Authorization", authorization);
        }
        String oauth_token = post
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(""))
                .readEntity(String.class);
        System.out.println("readObject = " + oauth_token);
        return oauth_token;
    }

}

//public String authenticate(Map<String, String> map) {
//        List<Pair> pairs = map.entrySet()
//                .stream()
//                .map((Map.Entry<String, String> t) -> Pair.create(t.getKey(), t.getValue()))
//                .collect(Collectors.toList());
//        return authenticate(pairs);
//}
