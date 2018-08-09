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
@WebServlet(name = "Start", urlPatterns = {"/start"})
public class Start extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String client_id = request.getParameter("client_id");
        String client_secret = request.getParameter("client_secret");

//        request.getSession().setAttribute("client_id", client_id);
//        request.getSession().setAttribute("client_secret", client_secret);
        Dropbox dropbox = new Dropbox(
                client_id,
                client_secret,
                "http://localhost:8080/oauth/token"
        );

        request.getSession().setAttribute("oauth", dropbox);
        
//        String uri = "https://github.com/login/oauth/authorize?scope=repo&client_id=" + client_id;
        String format = String.format("client_id=%s&response_type=code&redirect_uri=http://localhost:8080/oauth/token", client_id);
        String uri = "https://www.dropbox.com/oauth2/authorize?" + format;
        response.sendRedirect(uri);
    }

}
