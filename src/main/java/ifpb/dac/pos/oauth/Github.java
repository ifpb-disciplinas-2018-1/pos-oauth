package ifpb.dac.pos.oauth;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Form;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 13/08/2018, 09:59:14
 */
public class Github extends OAuth {

    private final String url_autorize = "https://github.com/login/oauth/authorize";
    private final String url_acess_token = "https://github.com/login/oauth/access_token";

    public Github(String client_id, String client_secret, String redirect_uri) {
        super(client_id, client_secret, redirect_uri);
    }

    @Override
    public String urlAutorizeFormated() {
        String uri = String.format(
                "%s?client_id=%s&scope=repo&redirect_uri=%s",
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

    @Override
    public String prefixHeader(String token) {
        return "token " + token;
    }

    @Override
    public List<Pair> forms() {
        List<Pair> list = new ArrayList<>();
        list.add(Pair.create("state", "repo"));
        list.add(Pair.create("client_secret", getClient_secret()));
        list.add(Pair.create("client_id", getClient_id()));
        return list;
    }

}
