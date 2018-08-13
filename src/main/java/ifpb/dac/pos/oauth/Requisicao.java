package ifpb.dac.pos.oauth;

import ifpb.dac.pos.oauth.twitter.TwitterAutenticate;
import java.util.HashMap;
import java.util.Map;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 13/08/2018, 15:21:36
 */
public class Requisicao {

    private final OAuth oAuth;

    public Requisicao(OAuth oAuth) {
        this.oAuth = oAuth;
    }

    public String accessToken(String authorization, Pair verifier) {
        Client client = ClientBuilder.newClient();
        WebTarget root = client.target(this.oAuth.urlAcessToken());
//
//        TwitterAutenticate a = new TwitterAutenticate(oauth_token, oauth_verifier);
//        String authorization = a.headerAuthorization("POST", "https://api.twitter.com/oauth/access_token");

        Form form = new Form(verifier.key(), verifier.value().toString());
        
        this.oAuth.forms().forEach((pair) -> {
            form.param(pair.key(), pair.value().toString());
        });

        String json = root.request()
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .post(Entity.form(form))
                .readEntity(String.class);

        return json;
    }

    public String requestToken(String authorization, String uri) {
        Client newBuilder = ClientBuilder.newBuilder().build();
        WebTarget target = newBuilder.target(uri);
        Invocation.Builder post = target.request();
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
