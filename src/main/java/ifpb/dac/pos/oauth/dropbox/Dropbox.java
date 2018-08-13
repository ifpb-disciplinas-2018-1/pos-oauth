package ifpb.dac.pos.oauth.dropbox;

import ifpb.dac.pos.oauth.OAuth;
import ifpb.dac.pos.oauth.Pair;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Form;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 09/08/2018, 11:26:33
 */
public class Dropbox extends OAuth {

    private final String url_autorize = "https://www.dropbox.com/oauth2/authorize";
    private final String url_acess_token = "https://api.dropboxapi.com/oauth2/token";

    public Dropbox(String client_id, String client_secret, String redirect_uri) {
        super(client_id, client_secret, redirect_uri);
    }

    @Override
    public String urlAuthorizeFormated() {
        String uri = String.format(
                "%s?client_id=%s&response_type=code&redirect_uri=%s",
                url_autorize,
                client_id,
                redirect_uri
        );
        return uri;
    }

    @Override
    public String urlAcessToken() {
        return url_acess_token;
    }

//    public String getUrl_autorize() {
//        return url_autorize;
//    }
    @Override
    public String prefixHeader(String token) {
        return "Bearer " + token;
    }

    @Override
    public List<Pair> forms() {
        List<Pair> list = new ArrayList<>();
        list.add(Pair.create("grant_type", "authorization_code"));
        list.add(Pair.create("client_secret", getClient_secret()));
        list.add(Pair.create("client_id", getClient_id()));
        list.add(Pair.create("redirect_uri", getRedirect_uri()));
        return list;
    }
}
