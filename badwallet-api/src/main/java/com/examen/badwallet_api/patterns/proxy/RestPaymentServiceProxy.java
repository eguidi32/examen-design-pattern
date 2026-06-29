package com.examen.badwallet_api.patterns.proxy;

import com.examen.badwallet_api.dto.response.ExternalFactureResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RestPaymentServiceProxy implements PaymentServiceProxy {

	private final RestTemplate restTemplate;
	private final String paymentServiceBaseUrl;

	public RestPaymentServiceProxy(
			RestTemplate restTemplate,
			@Value("${payment.service.base-url}") String paymentServiceBaseUrl) {
		this.restTemplate = restTemplate;
		this.paymentServiceBaseUrl = paymentServiceBaseUrl;
	}

	@Override
	public List<ExternalFactureResponse> getCurrentFactures(String walletCode, String unite) {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(paymentServiceBaseUrl)
				.pathSegment("api", "factures", walletCode, "current");

		if (unite != null && !unite.isBlank()) {
			builder.queryParam("unite", unite);
		}

		return getFactures(builder);
	}

	@Override
	public List<ExternalFactureResponse> getFacturesByPeriod(String walletCode, LocalDate debut, LocalDate fin) {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(paymentServiceBaseUrl)
				.pathSegment("api", "factures", walletCode, "periode")
				.queryParam("debut", debut)
				.queryParam("fin", fin);

		return getFactures(builder);
	}

	@Override
	public ExternalFactureResponse markFactureAsPaid(String reference) {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(paymentServiceBaseUrl)
				.pathSegment("api", "factures", reference, "pay");

		try {
			ResponseEntity<ExternalFactureResponse> response = restTemplate.exchange(
					builder.build().toUri(),
					HttpMethod.PATCH,
					null,
					ExternalFactureResponse.class);
			return response.getBody();
		} catch (ResourceAccessException exception) {
			throw new ResponseStatusException(
					HttpStatus.SERVICE_UNAVAILABLE,
					"payment-service est indisponible",
					exception);
		} catch (HttpStatusCodeException exception) {
			throw new ResponseStatusException(
					HttpStatus.BAD_GATEWAY,
					"payment-service a retourne une erreur: " + exception.getStatusCode(),
					exception);
		} catch (RestClientException exception) {
			throw new ResponseStatusException(
					HttpStatus.BAD_GATEWAY,
					"Erreur lors de l'appel a payment-service",
					exception);
		}
	}

	private List<ExternalFactureResponse> getFactures(UriComponentsBuilder builder) {
		try {
			ExternalFactureResponse[] response = restTemplate.getForObject(
					builder.build().toUri(),
					ExternalFactureResponse[].class);
			if (response == null) {
				return Collections.emptyList();
			}
			return Arrays.asList(response);
		} catch (ResourceAccessException exception) {
			throw new ResponseStatusException(
					HttpStatus.SERVICE_UNAVAILABLE,
					"payment-service est indisponible",
					exception);
		} catch (HttpStatusCodeException exception) {
			throw new ResponseStatusException(
					HttpStatus.BAD_GATEWAY,
					"payment-service a retourne une erreur: " + exception.getStatusCode(),
					exception);
		} catch (RestClientException exception) {
			throw new ResponseStatusException(
					HttpStatus.BAD_GATEWAY,
					"Erreur lors de l'appel a payment-service",
					exception);
		}
	}
}
