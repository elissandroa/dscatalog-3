package br.com.elissandro.DsCatalog.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class TokenUtil {

	private String clientId = "myclientid";

	private String clientSecret = "myclientsecret";

	public String obtainAccessToken(MockMvc mockMvc, String username, String password) throws Exception {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("client-id", clientId);
		params.add("client-secret", clientSecret);
		params.add("username", username);
		params.add("password", password);

		String auth = clientId + ":" + clientSecret;
		String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + encodedAuth);

		ResultActions result = mockMvc
				.perform(post("/oauth2/token").headers(headers).params(params).accept("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));
				
		String resultString = result.andReturn().getResponse().getContentAsString();

		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(resultString).get("access_token").toString();
	}
}