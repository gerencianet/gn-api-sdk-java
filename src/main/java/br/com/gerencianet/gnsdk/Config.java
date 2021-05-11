package br.com.gerencianet.gnsdk;

import org.json.JSONObject;

/**
 * This class is used to create an Object with all needed configurations used in
 * Gerencianet API. This configurations include the Endpoints and URLs of
 * Gerencianet API, and credential data of Gerencianet client.
 * 
 * @author Filipe Mata
 *
 */
public class Config {
	private final static String version = "1.0.2";
	private JSONObject conf = new JSONObject();
	private JSONObject endpoints = new JSONObject();
	private JSONObject urls = new JSONObject();

	public Config(JSONObject options, JSONObject config) throws Exception {

		if (config.has("ENDPOINTS")) {
			this.endpoints = (JSONObject) config.get("ENDPOINTS");
			if (setPix(options)) {
				if (this.endpoints.has("PIX")) {
					this.endpoints = (JSONObject) this.endpoints.get("PIX");
				}
			} else {
				if (this.endpoints.has("DEFAULT")) {
					this.endpoints = (JSONObject) this.endpoints.get("DEFAULT");
				}
			}
		} else
			throw new Exception("Problems to get ENDPOINTS in file config.json");

		if (config.has("URL")) {
			this.urls = (JSONObject) config.get("URL");
			if (setPix(options)) {
				if (this.urls.has("PIX")) {
					this.urls = (JSONObject) this.urls.get("PIX");
				}
			} else if (this.urls.has("DEFAULT")) {
				this.urls = (JSONObject) this.urls.get("DEFAULT");
			}
		} else
			throw new Exception("Problems to get URLs in file config.json");

		this.setConf(options);
	}

	public JSONObject getEndpoints() {
		return endpoints;
	}

	public JSONObject getUrls() {
		return urls;
	}

	public void setConf(JSONObject options) {
		boolean sandbox = false;
		boolean debug = false;

		if (options.has("sandbox"))
			sandbox = options.getBoolean("sandbox");
		if (options.has("debug"))
			debug = options.getBoolean("debug");

		this.conf.put("sandbox", sandbox);
		this.conf.put("debug", debug);

		if (options.has("client_id"))
			this.conf.put("clientId", options.getString("client_id"));
		if (options.has("client_secret"))
			this.conf.put("clientSecret", options.getString("client_secret"));
		if (options.has("pix_cert"))
			this.conf.put("certificadoPix", options.getString("pix_cert"));
		if (options.has("partner_token"))
			this.conf.put("partnerToken", options.getString("partner_token"));
		if (options.has("url")) {
			this.conf.put("baseUri", options.getString("url"));
		}

		else {
			String baseUri = this.urls.getString("production");
			if (this.conf.getBoolean("sandbox") == true)
				baseUri = this.urls.getString("sandbox");

			this.conf.put("baseUri", baseUri);
		}

		if (options.has("x-skip-mtls-checking")) {
			this.conf.put("headers", options.getString("x-skip-mtls-checking"));
		}
	}

	public JSONObject getOptions() {
		return this.conf;
	}

	public static String getVersion() {
		return Config.version;
	}

	public boolean setPix(JSONObject options) {
		return options.has("pix_cert");
	}

}