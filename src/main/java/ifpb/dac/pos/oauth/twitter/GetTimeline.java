package ifpb.dac.pos.oauth.twitter;

import ifpb.dac.pos.oauth.Token;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ricardo Job
 */
@WebServlet(
        name = "GetTimeline",
        urlPatterns = {"/timeline"}
)
public class GetTimeline extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Client client = ClientBuilder.newClient();
        Token token = (Token) request.getSession().getAttribute("token");
//        String oauth_token = (String) request.getSession().getAttribute("oauth_token");
//        String oauth_verifier = (String) request.getSession().getAttribute("oauth_verifier");
//        System.out.println("oauth_verifier = " + oauth_token);
//        System.out.println("oauth_verifier = " + oauth_verifier);
        TwitterAutenticate autenticate = new TwitterAutenticate(
                token.value(),
                token.secret()
        );
//        List<Pair> pairs = Arrays.asList(Pair.create("status", "oi"));
//        String value = autenticate.
//                headerAuthorization(
//                        "GET",
//                        "https://api.twitter.com/1.1/statuses/user_timeline.json"
//                //                        pairs
//                );
//        System.out.println("value = " + value);
//        WebTarget timeline = client.target("https://api.twitter.com/1.1/statuses/user_timeline.json");
//        String json = timeline
//                //        JsonObject json = timeline
//                .request()
//                .accept(MediaType.APPLICATION_JSON)
//                .header("Authorization", value)
//                //                .get(JsonObject.class);
//                .get(String.class);
//                .post(
//                        Entity.form(
//                                new Form("status", "oi")
//                        ),
//                        String.class
//                );
//response.getWriter().println(
//                json
//        );

//TwitterAutenticate aut = new TwitterAutenticate2(credentials.value(), credentials.verifier());
//        String headerAuthorization = authenticator.in(endpoint).authenticate();
        String headerAuthorization = autenticate.headerAuthorization(
                "GET",
                "https://api.twitter.com/1.1/statuses/user_timeline.json"
        );
        WebTarget timelineTarget = client.target(
                "https://api.twitter.com/1.1/statuses/user_timeline.json"
        );
        //        List<Pair> pairs = Arrays.asList(
//                Pair.create("count", "2"),
//                Pair.create("include_rts", "true")
//        );
        JsonArray time = timelineTarget.request()
                //                .queryParam("count", "2")
                //                .queryParam("include_rts", "true")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", headerAuthorization)
                .get(JsonArray.class);
//        String readEntity = time.readEntity(String.class);

//        response.getWriter().println(
//                time
//        );
        List<JsonObject> valuesAs = time.getValuesAs(JsonObject.class);
        response.setContentType("text/html");
        response.getWriter().println("<h2> Listando os tuites </h2>");
        valuesAs.stream().forEach(
                j -> {
                    try {
                        response.getWriter().println(
                                "<p> " + j.getString("created_at") + " : "
                                + j.getString("text") + " </p>"
                        );
                    } catch (IOException ex) {
                        Logger.getLogger(GetTimeline.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        );
        response.getWriter().flush();
    }

}
