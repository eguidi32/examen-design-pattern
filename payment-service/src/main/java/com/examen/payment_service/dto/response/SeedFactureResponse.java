package com.examen.payment_service.dto.response;

import java.util.List;

public class SeedFactureResponse {

	private final List<String> walletsSeeded;
	private final int facturesCreated;
	private final String message;

	public SeedFactureResponse(List<String> walletsSeeded, int facturesCreated, String message) {
		this.walletsSeeded = walletsSeeded;
		this.facturesCreated = facturesCreated;
		this.message = message;
	}

	public List<String> getWalletsSeeded() {
		return walletsSeeded;
	}

	public int getFacturesCreated() {
		return facturesCreated;
	}

	public String getMessage() {
		return message;
	}
}
