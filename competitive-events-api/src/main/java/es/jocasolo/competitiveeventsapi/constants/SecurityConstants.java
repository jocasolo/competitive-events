package es.jocasolo.competitiveeventsapi.constants;

public class SecurityConstants {
	
	private SecurityConstants() {}
	
	// Spring security
	public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
	public static final String TOKEN_TYPE = "Bearer";
	
	// JWT
	public static final String SUPER_SECRET_KEY = "D3v3l0pP455w0rd";
	public static final long TOKEN_EXPIRATION_TIME = 1800000L; // 30 minutes
	public static final long REFRESH_TOKEN_EXPIRATION_TIME = 2592000000L; // 30 days
	public static final String ISSUER_INFO = "test";

}
