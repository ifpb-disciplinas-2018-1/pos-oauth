package ifpb.dac.pos.oauth;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 08/02/2018, 17:18:19
 */
public class Pair {

    private final String key;
    private final Object value;

    private Pair(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static Pair create(String key, Object value) {
        return new Pair(key, value);
    }

    public String key() {
        return key;
    }

    public Object value() {
        return value;
    }
}
