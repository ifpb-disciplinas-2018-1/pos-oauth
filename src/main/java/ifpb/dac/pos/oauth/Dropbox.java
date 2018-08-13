package ifpb.dac.pos.oauth;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 09/08/2018, 11:26:33
 */
public class Dropbox {

    private final String url_autorize = "https://www.dropbox.com/oauth2/authorize";
    private final String url_acess_token = "https://api.dropboxapi.com/oauth2/token";

    private final String client_id;
    private final String client_secret;
    private final String redirect_uri;

    public Dropbox(String client_id, String client_secret, String redirect_uri) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.redirect_uri = redirect_uri;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public String getUrl_acess_token() {
        return url_acess_token;
    }

    public String getUrl_autorize() {
        return url_autorize;
    }

    public String urlAutorizeFormated() {
        String uri = String.format(
                "%s?client_id=%s&response_type=code&redirect_uri=%s",
                url_autorize,
                client_id,
                redirect_uri
        );
        return uri;
    }
}
