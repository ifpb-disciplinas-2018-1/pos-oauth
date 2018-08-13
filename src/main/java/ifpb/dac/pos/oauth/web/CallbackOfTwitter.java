/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.dac.pos.oauth.web;

import ifpb.dac.pos.oauth.OAuth;
import ifpb.dac.pos.oauth.TwitterAutenticate;
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
@WebServlet(name = "CallbackOfTwitter", urlPatterns = {"/twitter"})
public class CallbackOfTwitter extends HttpServlet {
//http://localhost:8080/oauth/twitter?
    //oauth_token=TW-GawAAAAAAFiEmAAABZTOsF44
//&oauth_verifier=CNosY2kXHWvMcDeSbaDJHqNWjHu5vLJd

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String oauth_token = request.getParameter("oauth_token");
        String oauth_verifier = request.getParameter("oauth_verifier");

        Client client = ClientBuilder.newClient();
        WebTarget root = client
                .target("https://api.twitter.com/oauth/access_token");

        TwitterAutenticate a = new TwitterAutenticate(oauth_token, oauth_verifier);
        String authorization = a.headerAuthorization("POST", "https://api.twitter.com/oauth/access_token");

        Form form = new Form("oauth_verifier", oauth_verifier);

        String json = root.request()
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .post(Entity.form(form))
                .readEntity(String.class);

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
