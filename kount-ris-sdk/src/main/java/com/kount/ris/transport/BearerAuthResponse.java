package com.kount.ris.transport;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Objects;

public class BearerAuthResponse {

    @JsonProperty("access_token")
    protected String accessToken = "";

    @JsonProperty("token_type")
    protected String tokenType = "";

    @JsonProperty("expires_in")
    protected String expiresIn = "";

    @JsonProperty("scope")
    protected String scope = "";

    protected OffsetDateTime createdAt = OffsetDateTime.MIN;

    protected OffsetDateTime expiresAt = OffsetDateTime.MIN;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public OffsetDateTime getExpiresAt() {
        if (!createdAt.equals(expiresAt)) {
            expiresAt = createdAt.plusSeconds(Integer.parseInt(expiresIn) - 60); // allow 60 seconds for latency
        }

        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }


    public BearerAuthResponse() {

    }


    public BearerAuthResponse(String accessToken, String tokenType, String expiresIn, String scope)
    {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.expiresAt = OffsetDateTime.now().plusSeconds(Long.parseLong(expiresIn));
    }
}
