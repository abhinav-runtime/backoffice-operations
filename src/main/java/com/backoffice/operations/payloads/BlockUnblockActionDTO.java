package com.backoffice.operations.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockUnblockActionDTO {
	
	@JsonProperty("entityId")
    private String entityId;

    @JsonProperty("kitNo")
    private String kitNo;

    @JsonProperty("flag")
    private String flag;

    @JsonProperty("reason")
    private String reason;

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getKitNo() {
		return kitNo;
	}

	public void setKitNo(String kitNo) {
		this.kitNo = kitNo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
