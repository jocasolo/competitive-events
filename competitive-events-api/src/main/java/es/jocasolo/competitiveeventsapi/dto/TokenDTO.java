package es.jocasolo.competitiveeventsapi.dto;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class TokenDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@SerializedName("access_token")
	private String accessToken;

	@Getter
	@Setter
	@SerializedName("token_type")
	private String tokenType;

	@Getter
	@Setter
	@SerializedName("expires_in")
	private long expiresIn;

	@Getter
	@Setter
	@SerializedName("refresh_token")
	private String refreshToken;

	@Override
	public String toString() {
		return "TokenDTO [accessToken=" + accessToken + "]";
	}

}
