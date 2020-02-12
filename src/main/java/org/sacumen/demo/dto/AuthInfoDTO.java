package org.sacumen.demo.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AuthInfoDTO {

    private String grant_type;
    private String client_id;
    private String client_secret;
    private String username;
    private String password;

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AuthInfoDTO that = (AuthInfoDTO) o;

        return new EqualsBuilder()
                .append(grant_type, that.grant_type)
                .append(client_id, that.client_id)
                .append(client_secret, that.client_secret)
                .append(username, that.username)
                .append(password, that.password)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(grant_type)
                .append(client_id)
                .append(client_secret)
                .append(username)
                .append(password)
                .toHashCode();
    }
}
