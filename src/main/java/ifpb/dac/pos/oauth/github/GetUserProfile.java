package ifpb.dac.pos.oauth.github;

import java.io.IOException;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author Ricardo Job
 */
@WebServlet(
        name = "GetUserProfile",
        urlPatterns = {"/user"}
)
public class GetUserProfile extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Client client = ClientBuilder.newClient();
        String token = (String) request.getSession().getAttribute("token");

        WebTarget user = client.target("https://api.github.com/user");
        JsonObject jsonUser = user
                .request()
                .header("Authorization", token)
                .get(JsonObject.class);

        response.getWriter().println(
                jsonUser
        );
    }

}
