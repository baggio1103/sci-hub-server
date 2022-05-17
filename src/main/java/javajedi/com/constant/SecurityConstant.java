package javajedi.com.constant;

public class SecurityConstant {

    public static final long EXPIRATION_TIME = 432_000_000; //5 days expressed in milliseconds

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String JWT_TOKEN_HEADER = "Jwt-Token";

    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";

    public static final String STELLAR_NET_LLC = "Stellar Net, LLC";

    public static final String STELLAR_NET_ADMINISTRATION = "Stellar Net Administration";

    public static final String AUTHORITIES = "authorities";

    public static final String FORBIDDEN_MESSAGE = "You need to login to access this page";

    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";

    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";

    public static final String[] PUBLIC_URLS = {"/user/login", "/user/register", "/user/reset-password/**",
            "/user/image/**", "/publications/**"};

}
