package ifpb.dac.pos.oauth;

import java.util.StringTokenizer;
import javax.json.JsonObject;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 17/08/2018, 10:50:12
 */
public class Token {

    private final String value;
    private final String secret;
    private final String headerAuthorized;

    private Token(String value, String secret, String header) {
        this.value = value;
        this.secret = secret;
        this.headerAuthorized = header;
    }

    public Token(String token, String secret) {
        this(token, secret, "");
    }

    public static Token from(String token) {
        StringTokenizer tokenizer = new StringTokenizer(token, "&");
        return new Token(
                tokenizer.nextToken().split("=")[1],
                tokenizer.nextToken().split("=")[1]
        );
        //oauth_token=90332417-YWgBgJkfrMNizwRi5vXMih4n3ikarxj9ZO3uRVocn
        //&oauth_token_secret=wjdFh2OTy6EXaAuoGI9B3V8V3G571Bb1jWwb39sXevgQu
        //&user_id=90332417
        //&screen_name=ricardojob
//        StringTokenizer tokenizer = new StringTokenizer(json, "&");
    }

    public static Token from(JsonObject json) {
        return new Token(
                json.getString("access_token"),
                ""
        );
    }

    public Token header(String header) {
        return new Token(
                this.value,
                this.secret,
                header
        );
    }

    public String value() {
        return value;
    }

    public String secret() {
        return secret;
    }

    public String authorized() {
        return headerAuthorized;
    }

}
