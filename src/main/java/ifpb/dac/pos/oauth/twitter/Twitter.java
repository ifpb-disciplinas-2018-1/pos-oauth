package ifpb.dac.pos.oauth.twitter;

import ifpb.dac.pos.oauth.OAuth;
import ifpb.dac.pos.oauth.Pair;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Form;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 13/08/2018, 09:59:14
 */
public class Twitter extends OAuth {

    private final String url_request_token = "https://api.twitter.com/oauth/request_token";
    private final String url_autorize = "https://api.twitter.com/oauth/authenticate";
    private final String url_acess_token = "https://api.twitter.com/oauth/access_token";

    public Twitter(String client_id, String client_secret, String redirect_uri) {
        super(client_id, client_secret, redirect_uri);
    }

    @Override
    public String urlAuthorizeFormated() {
        String uri = String.format(
                "%s?", url_autorize
        );
        return uri;
    }
    //oauth_token=CklkiwAAAAAAFiEmAAABZTRIAjU&oauth_token_secret=aZF4sheWfILpvqDPvKgdYIvr3KYM442j&oauth_callback_confirmed=true

    @Override
    public String urlAcessToken() {
        return url_acess_token;
    }

    @Override
    public String prefixHeader(String token) {
        return "token " + token;
    }

    public String getUrl_request_token() {
        return url_request_token;
    }

    @Override
    public List<Pair> forms() {
//        Form form = new Form("oauth_verifier", oauth_verifier);
        List<Pair> list = new ArrayList<>();
//        list.add(Pair.create("oauth_verifier", "repo"));
//        list.add(Pair.create("client_secret", getClient_secret()));
//        list.add(Pair.create("client_id", getClient_id()));
        return list;
    }

}
