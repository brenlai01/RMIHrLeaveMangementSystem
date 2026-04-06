package security;

public final class SslPropertyUtil {

    private SslPropertyUtil() {
    }

    public static String requireProperty(String name) {
        String value = System.getProperty(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required JVM property: -D" + name + "=<value>");
        }
        return value;
    }
}
