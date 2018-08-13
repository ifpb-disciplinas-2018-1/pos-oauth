package ifpb.dac.pos.oauth.web;

import ifpb.dac.pos.oauth.OAuth;
import ifpb.dac.pos.oauth.Pair;
import ifpb.dac.pos.oauth.Requisicao;
import ifpb.dac.pos.oauth.twitter.Twitter;
import ifpb.dac.pos.oauth.twitter.TwitterAutenticate;
import java.io.IOException;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ricardo Job
 */
//http://localhost:8080/oauth/twitter?oauth_token=TW-GawAAAAAAFiEmAAABZTOsF44&oauth_verifier=CNosY2kXHWvMcDeSbaDJHqNWjHu5vLJd
@WebServlet(name = "CallbackOfTwitter", urlPatterns = {"/twitter"})
public class CallbackOfTwitter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String oauth_token = request.getParameter("oauth_token");
        String oauth_verifier = request.getParameter("oauth_verifier");

        OAuth oauth = (OAuth) request.getSession().getAttribute("oauth");

//        Twitter twitter = new Twitter(
//                "2URxXsnyMBfn71XTtRs8A",
//                "CM8WbuGDFPIQGhkLhFEVQMyCK6sZFq10uXM4IzHQQ",
//                "http://localhost:8080/oauth/token"
//        );
        Requisicao rq = new Requisicao(
                oauth
        );

        TwitterAutenticate a = new TwitterAutenticate(oauth_token, oauth_verifier);
        String authorization = a.headerAuthorization("POST", oauth.urlAcessToken());

        String json = rq.accessToken(
                authorization,
                Pair.create("oauth_verifier", oauth_verifier)
        );
        response.getWriter().println(
                json
        );

//        String token = oauth.prefixHeader(jsons.getString("access_token"));
//        request.getSession().setAttribute("token", token);
//        request.getSession().removeAttribute("oauth");
//        String redirect = (String) request.getSession().getAttribute("redirect");
//        request.getRequestDispatcher(redirect).forward(request, response);
    }

    public void hulk(HttpServletRequest request, HttpServletResponse response) {
        String oauth_token = request.getParameter("oauth_token");
        String oauth_verifier = request.getParameter("oauth_verifier");

        OAuth oauth = (OAuth) request.getSession().getAttribute("oauth");

        Requisicao rq = new Requisicao(
                oauth
        );

        TwitterAutenticate a = new TwitterAutenticate(oauth_token, oauth_verifier);
        String authorization = a.headerAuthorization("POST", oauth.urlAcessToken());

        String json = rq.accessToken(
                authorization,
                Pair.create("oauth_verifier", oauth_verifier)
        );
        response.getWriter().println(
                json
        );
    }

    private void dropbox(String code, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        OAuth dropbox = (OAuth) request.getSession().getAttribute("oauth");

        Client client = ClientBuilder.newClient();
        WebTarget root = client
                .target(dropbox.urlAcessToken());

        Form form = new Form("code", code)
                .param("grant_type", "authorization_code")
                .param("client_secret", dropbox.getClient_secret())
                .param("client_id", dropbox.getClient_id())
                .param("redirect_uri", dropbox.getRedirect_uri());

        JsonObject json = root.request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.form(form))
                .readEntity(JsonObject.class);
        Entity<Form> entity = Entity.form(form);

        String token = "Bearer " + json.getString("access_token");

        request.getSession().setAttribute("token", token);
        request.getSession().removeAttribute("oauth");
        request.getRequestDispatcher("files").forward(request, response);

    }

}
