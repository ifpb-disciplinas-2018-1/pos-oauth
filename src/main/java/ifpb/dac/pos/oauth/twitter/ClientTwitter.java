package ifpb.dac.pos.oauth.twitter;

import ifpb.dac.pos.oauth.TwitterAutenticate;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 07/02/2018, 11:38:55
 */
public class ClientTwitter {

    public static void main(String[] args) throws IOException {
        TwitterAutenticate a = new TwitterAutenticate("", "");
        String authorization = a.headerAuthorization("POST", "https://api.twitter.com/oauth/request_token");
        System.out.println("authorization = " + authorization);

        Client newBuilder = ClientBuilder.newBuilder().build();
        WebTarget target = newBuilder.target("https://api.twitter.com/oauth/request_token");

        Response post = target.request().accept(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .post(Entity.json(""));

        String oauth_token = post.readEntity(String.class);
        System.out.println("readObject = " + oauth_token);

        Desktop d = Desktop.getDesktop();
        d.browse(URI.create("https://api.twitter.com/oauth/authenticate?" + oauth_token));
    }

}

//public String authenticate(Map<String, String> map) {
//        List<Pair> pairs = map.entrySet()
//                .stream()
//                .map((Map.Entry<String, String> t) -> Pair.create(t.getKey(), t.getValue()))
//                .collect(Collectors.toList());
//        return authenticate(pairs);
//}
