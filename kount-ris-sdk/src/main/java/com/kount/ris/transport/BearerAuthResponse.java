package com.kount.ris.transport;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class BearerAuthResponse {

    @JsonProperty("access_token")
    protected String accessToken = "";

    @JsonProperty("token_type")
    protected String tokenType = "";

    @JsonProperty("expires_in")
    protected int expiresIn = 0;

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

    private boolean hasOffsetBeenAccountedFor = false;

    private int latencyBufferSeconds = 120;

    public OffsetDateTime getExpiresAt() {
        if (expiresIn != 0 && !hasOffsetBeenAccountedFor) {
            hasOffsetBeenAccountedFor = true;
            expiresAt = createdAt.plusSeconds(expiresIn - latencyBufferSeconds); // allow for latency
        }

        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        hasOffsetBeenAccountedFor = false;
        this.expiresAt = expiresAt;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getLatencyBufferSeconds() {
        return latencyBufferSeconds;
    }

    public void setLatencyBufferSeconds(int latencyBufferSeconds) {
        this.latencyBufferSeconds = latencyBufferSeconds;
    }


    public BearerAuthResponse() {
        createdAt = OffsetDateTime.now();
    }


    public BearerAuthResponse(String accessToken, String tokenType, int expiresIn, String scope)
    {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.createdAt = OffsetDateTime.now();
        this.expiresAt = OffsetDateTime.now().plusSeconds(expiresIn);
    }
}
