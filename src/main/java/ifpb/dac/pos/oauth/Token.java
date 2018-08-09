/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.dac.pos.oauth;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
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

//        github(code, response);
        dropbox(code, request, response);
    }

    private void github(String code, HttpServletResponse response) throws IOException {
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

    private void dropbox(String code, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Dropbox dropbox = (Dropbox) request.getSession().getAttribute("oauth");
        Client client = ClientBuilder.newClient();
        WebTarget root = client
                .target(dropbox.getUrl_acess_token());

//        String client_id = (String) request.getSession().getAttribute("client_id");
//        String client_id = (String) request.getSession().getAttribute("client_id");
        Form form = new Form("code", code)
                .param("grant_type", "authorization_code")
                .param("client_secret", dropbox.getClient_secret())
                .param("client_id", dropbox.getClient_id())
                .param("redirect_uri", dropbox.getRedirect_uri());

        JsonObject json = root.request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.form(form))
                .readEntity(JsonObject.class);
//        response.getWriter().println(
//                json
//        );
//        https://api.dropboxapi.com/2/files/list_folder
        String token = "Bearer " + json.getString("access_token");

        request.getSession().setAttribute("token", token);
        request.getSession().removeAttribute("oauth");

        request.getRequestDispatcher("files").forward(request, response);

//        WebTarget listFolder = client.target("https://api.dropboxapi.com/2/files/list_folder");
//        String param = "{\n"
//                + "    \"path\": \"\",\n"
//                + "    \"recursive\": false,\n"
//                + "    \"include_media_info\": false,\n"
//                + "    \"include_deleted\": false,\n"
//                + "    \"include_has_explicit_shared_members\": false,\n"
//                + "    \"include_mounted_folders\": true\n"
//                + "}";
//        JsonObject object = Json
//                .createReader(new StringReader(param))
//                .readObject();
//
//        JsonObject post = listFolder.request()
//                .accept(MediaType.APPLICATION_JSON)
//                .header("Authorization", token)
//                .post(Entity.json(object))
//                .readEntity(JsonObject.class);
//
//        JsonArray jsonArray = post.getJsonArray("entries");
//
//        List<JsonObject> valuesAs = jsonArray.getValuesAs(JsonObject.class);
//
//        valuesAs.stream().forEach(
//                j -> {
//                    try {
//                        response.getWriter().println(
//                                j.getString("path_display")
//                        );
//                    } catch (IOException ex) {
//                        Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//        );
//        response.getWriter().println(
//                jsonArray.getJsonObject(0).getString("path_display")
//        );
//{
//    "path": "",
//    "recursive": false,
//    "include_media_info": false,
//    "include_deleted": false,
//    "include_has_explicit_shared_members": false,
//    "include_mounted_folders": true
//}
    }

}
