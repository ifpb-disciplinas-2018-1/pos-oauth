/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.dac.pos.oauth;

import java.io.IOException;
import static java.lang.Math.E;
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
public class Token extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");

        //POST https://github.com/login/oauth/access_token
        //client_id 09f559456922d0d2c783
        //client_secret 7cd100ecdc8bcc32049ede82509ef12158e70140
        //code code
        //state  repo
        Client client = ClientBuilder.newClient();
        WebTarget root = client
                .target("https://github.com/login/oauth/access_token");

        Form form = new Form("client_id", "09f559456922d0d2c783")
                .param("client_secret", "7cd100ecdc8bcc32049ede82509ef12158e70140")
                .param("code", code)
                .param("state", "repo");

        Entity<Form> entity = Entity.form(form);
        Response post = root
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(entity);

//        String json = post.readEntity(String.class);
//        response.getWriter().println(json);
        JsonObject json = post.readEntity(JsonObject.class);
//        response.getWriter().println(
//                json.getString("access_token")
//        );

        //https://api.github.com/user
        WebTarget user = client.target("https://api.github.com/user");
        String token = "token " + json.getString("access_token");
        JsonObject jsonUser = user
                .request()
                .header("Authorization", token)
                .get(JsonObject.class);

        response.getWriter().println(
                //                jsonUser.getString("login")
                jsonUser
        );
    }

}
