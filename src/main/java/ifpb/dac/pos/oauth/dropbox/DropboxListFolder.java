package ifpb.dac.pos.oauth.dropbox;

import ifpb.dac.pos.oauth.Token;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
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
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ricardo Job
 */
@WebServlet(
        name = "DropboxListFolder",
        urlPatterns = {"/files"}
)
public class DropboxListFolder extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Client client = ClientBuilder.newClient();
        WebTarget listFolder = client.target("https://api.dropboxapi.com/2/files/list_folder");

        Token token = (Token) request.getSession().getAttribute("token");

        String param = "{\n"
                + "    \"path\": \"\",\n"
                + "    \"recursive\": false,\n"
                + "    \"include_media_info\": false,\n"
                + "    \"include_deleted\": false,\n"
                + "    \"include_has_explicit_shared_members\": false,\n"
                + "    \"include_mounted_folders\": true\n"
                + "}";
        JsonObject object = Json
                .createReader(new StringReader(param))
                .readObject();

        JsonObject post = listFolder.request()
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token.authorized())
                .post(Entity.json(object))
                .readEntity(JsonObject.class);

        JsonArray jsonArray = post.getJsonArray("entries");

        List<JsonObject> valuesAs = jsonArray.getValuesAs(JsonObject.class);
        response.getWriter().println("<h2>Listando o diretorio</h2>");
        valuesAs.stream().forEach(
                j -> {
                    try {
                        response.getWriter().println(
                                "<p>" + j.getString("path_display") + "</p>"
                        );
                    } catch (IOException ex) {
                        Logger.getLogger(DropboxListFolder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        );
    }

}
