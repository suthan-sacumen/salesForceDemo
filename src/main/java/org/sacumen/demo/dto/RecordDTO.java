package org.sacumen.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecordDTO {

    private AttributeDTO attributes;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("EventType")
    private String eventType;

    public AttributeDTO getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributeDTO attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "RecordDTO{" +
                "attributes=" + attributes +
                ", id='" + id + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
