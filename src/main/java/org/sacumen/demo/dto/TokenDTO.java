package org.sacumen.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties
public class TokenDTO {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("instance_url")
    private String instanceUrl;

    @JsonProperty("id")
    private String id;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("issued_at")
    private String issuedAt;

    @JsonProperty("signature")
    private String signature;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    public void setInstanceUrl(String instanceUrl) {
        this.instanceUrl = instanceUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "accessToken='" + accessToken + '\'' +
                ", instanceUrl='" + instanceUrl + '\'' +
                ", id='" + id + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", issuedAt='" + issuedAt + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TokenDTO tokenDTO = (TokenDTO) o;

        return new EqualsBuilder()
                .append(accessToken, tokenDTO.accessToken)
                .append(instanceUrl, tokenDTO.instanceUrl)
                .append(id, tokenDTO.id)
                .append(tokenType, tokenDTO.tokenType)
                .append(issuedAt, tokenDTO.issuedAt)
                .append(signature, tokenDTO.signature)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(accessToken)
                .append(instanceUrl)
                .append(id)
                .append(tokenType)
                .append(issuedAt)
                .append(signature)
                .toHashCode();
    }
}
