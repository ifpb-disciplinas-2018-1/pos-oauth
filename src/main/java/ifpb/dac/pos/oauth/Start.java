package ifpb.dac.pos.oauth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ricardo Job
 */
@WebServlet(name = "Start", urlPatterns = {"/start"})
public class Start extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String client_id = request.getParameter("client_id");
        String client_secret = request.getParameter("client_secret");

//        OAuth oauth = new Dropbox(
//                client_id,
//                client_secret,
//                "http://localhost:8080/oauth/token"
//        );
        OAuth oauth = new Github(
                client_id,
                client_secret,
                "http://localhost:8080/oauth/token"
        );

        request.getSession().setAttribute("oauth", oauth);
//        request.getSession().setAttribute("redirect", "files");
        request.getSession().setAttribute("redirect", "user");
        response.sendRedirect(oauth.urlAutorizeFormated());
    }

}
