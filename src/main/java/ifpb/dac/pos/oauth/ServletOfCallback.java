/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.dac.pos.oauth;

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
import javax.ws.rs.core.Response;

/**
 *
 * @author Ricardo Job
 */
@WebServlet(name = "Token", urlPatterns = {"/token"})
public class ServletOfCallback extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        oauth(code, request, response);
//        github(code, request, response);
//        dropbox(code, request, response);
    }

    private void oauth(String code, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OAuth oauth = (OAuth) request.getSession().getAttribute("oauth");
        Client client = ClientBuilder.newClient();
        WebTarget root = client
                .target(oauth.urlAcessToken());

        Form form = new Form("code", code);
        oauth.forms().forEach((pair) -> {
            form.param(pair.key(), pair.value().toString());
        });

        JsonObject json = root.request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.form(form))
                .readEntity(JsonObject.class);
        Entity<Form> entity = Entity.form(form);
        String token = oauth.prefixHeader(json.getString("access_token"));
        request.getSession().setAttribute("token", token);
        request.getSession().removeAttribute("oauth");
        String redirect = (String) request.getSession().getAttribute("redirect");
        request.getRequestDispatcher(redirect).forward(request, response);
    }

    private void github(String code, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        OAuth github = (OAuth) request.getSession().getAttribute("oauth");

        Client client = ClientBuilder.newClient();
        WebTarget root = client
                .target(github.urlAcessToken());

        Form form = new Form("client_id", github.getClient_id())
                .param("client_secret", github.getClient_secret())
                .param("code", code)
                .param("state", "repo");

        Entity<Form> entity = Entity.form(form);
        Response post = root
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(entity);

        JsonObject json = post.readEntity(JsonObject.class);

        String token = "token " + json.getString("access_token");
        request.getSession().setAttribute("token", token);
        request.getSession().removeAttribute("oauth");
        request.getRequestDispatcher("user").forward(request, response);

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
