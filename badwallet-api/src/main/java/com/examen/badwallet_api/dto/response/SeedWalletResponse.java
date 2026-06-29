package com.examen.badwallet_api.dto.response;

public class SeedWalletResponse {

	private final int requestedWallets;
	private final int eventsPerWallet;
	private final int walletsCreated;
	private final int transactionsCreated;
	private final String message;

	public SeedWalletResponse(
			int requestedWallets,
			int eventsPerWallet,
			int walletsCreated,
			int transactionsCreated,
			String message) {
		this.requestedWallets = requestedWallets;
		this.eventsPerWallet = eventsPerWallet;
		this.walletsCreated = walletsCreated;
		this.transactionsCreated = transactionsCreated;
		this.message = message;
	}

	public int getRequestedWallets() {
		return requestedWallets;
	}

	public int getEventsPerWallet() {
		return eventsPerWallet;
	}

	public int getWalletsCreated() {
		return walletsCreated;
	}

	public int getTransactionsCreated() {
		return transactionsCreated;
	}

	public String getMessage() {
		return message;
	}
}
