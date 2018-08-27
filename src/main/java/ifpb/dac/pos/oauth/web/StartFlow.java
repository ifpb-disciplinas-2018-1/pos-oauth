package ifpb.dac.pos.oauth.web;

import ifpb.dac.pos.oauth.OAuth;
import ifpb.dac.pos.oauth.github.Github;
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
@WebServlet(name = "Start", urlPatterns = {"/autenticate"})
public class StartFlow extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String client_id = request.getParameter("client_id");
        String client_secret = request.getParameter("client_secret");
        String redirect_uri = request.getParameter("redirect_uri");
//
//        OAuth oauth = new Dropbox(
//                client_id,
//                client_secret,
//                redirect_uri
//        );
        OAuth oauth = new Github(
                client_id,
                client_secret,
                redirect_uri
        );
//        Twitter oauth = new Twitter(
//                client_id,
//                client_secret,
//                redirect_uri
//        );

        request.getSession().setAttribute("oauth", oauth);
//        request.getSession().setAttribute("redirect", "files");
        request.getSession().setAttribute("redirect", "user");
//        request.getSession().setAttribute("redirect", "timeline");
        response.sendRedirect(oauth.urlAuthorizeFormated());
    }

}
