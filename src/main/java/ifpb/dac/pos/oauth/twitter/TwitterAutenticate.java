package ifpb.dac.pos.oauth.twitter;

import ifpb.dac.pos.oauth.Pair;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 07/02/2018, 10:42:51
 */
public class TwitterAutenticate {

    private final String consumerKey = "2URxXsnyMBfn71XTtRs8A";
    private final String consumerSecret = "CM8WbuGDFPIQGhkLhFEVQMyCK6sZFq10uXM4IzHQQ";
    private final String token;
    private final String verifier;

    public TwitterAutenticate(String token, String verifier) {
        this.token = token;
        this.verifier = verifier;
    }

    public String headerAuthorization(String method, String twitterEndpoint) {
        return headerAuthorization(method, twitterEndpoint, new ArrayList<>());
    }

    public String headerAuthorization(String method, String twitterEndpoint, List<Pair> params) {
        String oauthTimestamp = Long.toString(System.currentTimeMillis() / 1000);
        String oauthNonce = "pos12desdfedrfedwsderd" + oauthTimestamp;

        String parameters = paramToString(oauthNonce, oauthTimestamp, params);

        String oauthSignature = signatureBaseToString(method, twitterEndpoint, parameters);

        String authorizationHeader = headerToString(oauthNonce, oauthTimestamp, oauthSignature);

        return authorizationHeader;
    }

    private String headerToString(
            String oauthNonce,
            String oauthTimestamp,
            String oauthSignature) {
        String oauthToken = ("".equals(token.trim())) ? "" : "\", oauth_token=\"" + token;
        String authorizationHeader = "OAuth "
                + "oauth_consumer_key=\"" + consumerKey
                + "\", oauth_nonce=\"" + oauthNonce
                + "\", oauth_signature=\"" + encode(oauthSignature)
                + "\", oauth_signature_method=\"HMAC-SHA1"
                + "\", oauth_timestamp=\"" + oauthTimestamp
                + oauthToken
                + "\", oauth_version=\"1.0\"";
        return authorizationHeader;
    }

    private String signatureBaseToString(
            String method,
            String twitterEndpoint,
            String parameters) {
        String signatureBase = method + "&" + encode(twitterEndpoint) + "&" + encode(parameters);
        String oauthVerifier = ("".equals(verifier.trim())) ? "" : encode(verifier);
        String composite = encode(consumerSecret) + "&" + oauthVerifier;
        String oauthSignature = "";
        try {
            oauthSignature = computeSignature(signatureBase, composite);
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            // TODO throw new RuntimeExpection();
            e.printStackTrace();
        }
        return oauthSignature;
    }

    private String paramToString(
            String oauthNonce,
            String oauthTimestamp,
            List<Pair> params) {
        String oauthSignatureMethod = "HMAC-SHA1";
        List<Pair> allParams = new ArrayList<>();
        allParams.add(Pair.create("oauth_consumer_key", consumerKey));
        allParams.add(Pair.create("oauth_nonce", oauthNonce));
        allParams.add(Pair.create("oauth_signature_method", oauthSignatureMethod));
        allParams.add(Pair.create("oauth_timestamp", oauthTimestamp));
        allParams.add(Pair.create("oauth_version", "1.0"));
        if (!"".equals(token.trim())) {
            allParams.add(Pair.create("oauth_token", token));
        }
        allParams.addAll(params);
        String parameters = allParams.stream()
                .sorted((p1, p2) -> p1.key().compareTo(p2.key()))
                .map(toPair())
                .collect(Collectors.joining("&"));
        return parameters;
    }

    protected String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] keyBytes = keyString.getBytes();
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        byte[] text = baseString.getBytes();
        return new String(Base64.getEncoder().encode(mac.doFinal(text))).trim();
    }

    public String encode(String value) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
//            throw new AuthenticatorException(ignore);
        }
        StringBuilder buf = new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%'
                    && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7'
                    && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }

    private Function<Pair, String> toPair() {
        return (Pair t) -> {
            return new StringBuilder(encode(t.key()))
                    .append("=")
                    .append(encode(t.value().toString()))
                    .toString();
        };
    }
}
