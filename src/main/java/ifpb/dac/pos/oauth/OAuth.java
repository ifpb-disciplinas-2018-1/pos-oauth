package ifpb.dac.pos.oauth;

import java.util.List;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 13/08/2018, 10:18:26
 */
public abstract class OAuth {

    protected final String client_id;
    protected final String client_secret;
    protected final String redirect_uri;

    public OAuth(String client_id, String client_secret, String redirect_uri) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.redirect_uri = redirect_uri;
    }

    public abstract String urlAutorizeFormated();
    public abstract String urlAcessToken();
    public abstract String prefixHeader(String token);
    public abstract List<Pair> forms();

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

}
