package com.examen.badwallet_api.patterns.strategy;

import com.examen.badwallet_api.enums.PaymentMethod;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class DepositStrategyFactory {

	private final Map<PaymentMethod, DepositStrategy> strategies = new EnumMap<>(PaymentMethod.class);

	public DepositStrategyFactory(List<DepositStrategy> depositStrategies) {
		depositStrategies.forEach(strategy -> strategies.put(strategy.getPaymentMethod(), strategy));
	}

	public DepositStrategy getStrategy(PaymentMethod paymentMethod) {
		DepositStrategy strategy = strategies.get(paymentMethod);
		if (strategy == null) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Methode de depot non supportee: " + paymentMethod);
		}
		return strategy;
	}
}
