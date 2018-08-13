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

        github(code, request, response);
//        dropbox(code, request, response);
    }

    private void github(String code, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //POST https://github.com/login/oauth/access_token
        //client_id 09f559456922d0d2c783
        //client_secret 7cd100ecdc8bcc32049ede82509ef12158e70140
        //code code
        //state  repo
        Github github = (Github) request.getSession().getAttribute("oauth");

        Client client = ClientBuilder.newClient();
        WebTarget root = client
                .target(github.getUrl_acess_token());

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
        Dropbox dropbox = (Dropbox) request.getSession().getAttribute("oauth");

        Client client = ClientBuilder.newClient();
        WebTarget root = client
                .target(dropbox.getUrl_acess_token());

        Form form = new Form("code", code)
                .param("grant_type", "authorization_code")
                .param("client_secret", dropbox.getClient_secret())
                .param("client_id", dropbox.getClient_id())
                .param("redirect_uri", dropbox.getRedirect_uri());

        JsonObject json = root.request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.form(form))
                .readEntity(JsonObject.class);

        String token = "Bearer " + json.getString("access_token");

        request.getSession().setAttribute("token", token);
        request.getSession().removeAttribute("oauth");

        request.getRequestDispatcher("files").forward(request, response);

    }

}
