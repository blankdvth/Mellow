package com.roxiun.mellow.api.racon;

import java.time.Instant;
import java.time.ZonedDateTime;

public class RaconTag {

    private final String tagType;
    private final String source;
    private final String sourceLabel;
    private final String reason;
    private final Instant addedOn;
    private final Long addedBy;
    private final String addedByUsername;

    public RaconTag(
            String tagType,
            String source,
            String sourceLabel,
            String reason,
            Instant addedOn,
            Long addedBy,
            String addedByUsername
    ) {
        this.tagType = tagType;
        this.source = source;
        this.sourceLabel = sourceLabel;
        this.reason = reason;
        this.addedOn = addedOn;
        this.addedBy = addedBy;
        this.addedByUsername = addedByUsername;
    }

    /**
     * Retained for existing formatting code. Coral names this field tag_type.
     */
    public String getType() {
        return tagType;
    }

    public String getSource() {
        return source;
    }

    public String getSourceLabel() {
        return sourceLabel;
    }

    public String getReason() {
        return reason;
    }

    public Instant getAddedOn() {
        return addedOn;
    }

    public Long getAddedBy() {
        return addedBy;
    }

    public String getAddedByUsername() {
        return addedByUsername;
    }
}
