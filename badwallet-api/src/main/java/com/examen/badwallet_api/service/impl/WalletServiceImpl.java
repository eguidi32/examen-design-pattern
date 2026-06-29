package com.examen.badwallet_api.service.impl;

import com.examen.badwallet_api.dto.request.CreateWalletRequest;
import com.examen.badwallet_api.dto.request.DepositRequest;
import com.examen.badwallet_api.dto.request.PayCurrentFactureRequest;
import com.examen.badwallet_api.dto.request.TransferRequest;
import com.examen.badwallet_api.dto.request.WithdrawRequest;
import com.examen.badwallet_api.dto.response.BillPaymentResponse;
import com.examen.badwallet_api.dto.response.ExternalFactureResponse;
import com.examen.badwallet_api.dto.response.TransactionResponse;
import com.examen.badwallet_api.dto.response.WalletBalanceResponse;
import com.examen.badwallet_api.dto.response.WalletResponse;
import com.examen.badwallet_api.entity.Transaction;
import com.examen.badwallet_api.entity.Wallet;
import com.examen.badwallet_api.enums.PaymentMethod;
import com.examen.badwallet_api.enums.TransactionStatus;
import com.examen.badwallet_api.enums.TransactionType;
import com.examen.badwallet_api.exception.BusinessException;
import com.examen.badwallet_api.patterns.factory.BillPaymentFactory;
import com.examen.badwallet_api.patterns.factory.BillPaymentHandler;
import com.examen.badwallet_api.patterns.proxy.PaymentServiceProxy;
import com.examen.badwallet_api.patterns.strategy.DepositStrategy;
import com.examen.badwallet_api.patterns.strategy.DepositStrategyFactory;
import com.examen.badwallet_api.repository.TransactionRepository;
import com.examen.badwallet_api.repository.WalletRepository;
import com.examen.badwallet_api.service.WalletService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WalletServiceImpl implements WalletService {

	private static final BigDecimal WITHDRAW_FEE_RATE = new BigDecimal("0.01");
	private static final BigDecimal MAX_WITHDRAW_FEE = new BigDecimal("5000.00");

	private final WalletRepository walletRepository;
	private final TransactionRepository transactionRepository;
	private final DepositStrategyFactory depositStrategyFactory;
	private final BillPaymentFactory billPaymentFactory;
	private final PaymentServiceProxy paymentServiceProxy;

	public WalletServiceImpl(
			WalletRepository walletRepository,
			TransactionRepository transactionRepository,
			DepositStrategyFactory depositStrategyFactory,
			BillPaymentFactory billPaymentFactory,
			PaymentServiceProxy paymentServiceProxy) {
		this.walletRepository = walletRepository;
		this.transactionRepository = transactionRepository;
		this.depositStrategyFactory = depositStrategyFactory;
		this.billPaymentFactory = billPaymentFactory;
		this.paymentServiceProxy = paymentServiceProxy;
	}

	@Override
	@Transactional
	public WalletResponse createWallet(CreateWalletRequest request) {
		validateUniqueWallet(request);

		Wallet wallet = new Wallet();
		wallet.setPhoneNumber(request.getPhoneNumber());
		wallet.setEmail(request.getEmail());
		wallet.setBalance(request.getInitialBalance());
		wallet.setCode(request.getCode());
		wallet.setCurrency(request.getCurrency());

		return toResponse(walletRepository.save(wallet));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<WalletResponse> listWallets(Pageable pageable) {
		return walletRepository.findAll(pageable).map(this::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public WalletResponse getWalletByPhoneNumber(String phoneNumber) {
		return toResponse(findWalletByPhoneNumber(phoneNumber));
	}

	@Override
	@Transactional(readOnly = true)
	public WalletBalanceResponse getWalletBalance(String phoneNumber) {
		return toBalanceResponse(findWalletByPhoneNumber(phoneNumber));
	}

	@Override
	@Transactional
	public TransactionResponse deposit(Long id, DepositRequest request) {
		if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Le montant du depot doit etre superieur a 0");
		}

		Wallet wallet = walletRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Aucun wallet trouve avec cet id"));

		DepositStrategy strategy = depositStrategyFactory.getStrategy(request.getPaymentMethod());
		Transaction transaction = strategy.deposit(wallet, request.getAmount());

		Wallet savedWallet = walletRepository.saveAndFlush(wallet);
		Transaction savedTransaction = transactionRepository.saveAndFlush(transaction);

		return toTransactionResponse(savedTransaction, savedWallet);
	}

	@Override
	@Transactional
	public TransactionResponse withdraw(WithdrawRequest request) {
		Wallet wallet = findWalletByPhoneNumber(request.getPhoneNumber());
		BigDecimal amount = request.getAmount();
		BigDecimal fees = calculateWithdrawFees(amount);
		BigDecimal total = amount.add(fees);

		if (wallet.getBalance().compareTo(total) < 0) {
			throw new ResponseStatusException(
					HttpStatus.CONFLICT,
					"Solde insuffisant pour effectuer le retrait");
		}

		wallet.setBalance(wallet.getBalance().subtract(total));

		Transaction transaction = new Transaction();
		transaction.setWallet(wallet);
		transaction.setAmount(amount);
		transaction.setPaymentMethod(PaymentMethod.WALLET);
		transaction.setType(TransactionType.WITHDRAW);
		transaction.setStatus(TransactionStatus.SUCCESS);
		transaction.setReference("WITHDRAW-" + UUID.randomUUID());

		Wallet savedWallet = walletRepository.saveAndFlush(wallet);
		Transaction savedTransaction = transactionRepository.saveAndFlush(transaction);

		return toWithdrawTransactionResponse(savedTransaction, savedWallet, fees, total);
	}

	@Override
	@Transactional
	public TransactionResponse transfer(TransferRequest request) {
		if (request.getSenderPhone().equals(request.getReceiverPhone())) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Le wallet expediteur et le wallet destinataire doivent etre differents");
		}

		Wallet sender = findWalletByPhoneNumber(request.getSenderPhone());
		Wallet receiver = findWalletByPhoneNumber(request.getReceiverPhone());
		BigDecimal amount = request.getAmount();

		if (sender.getBalance().compareTo(amount) < 0) {
			throw new ResponseStatusException(
					HttpStatus.CONFLICT,
					"Solde insuffisant pour effectuer le transfert");
		}

		sender.setBalance(sender.getBalance().subtract(amount));
		receiver.setBalance(receiver.getBalance().add(amount));

		Wallet savedSender = walletRepository.saveAndFlush(sender);
		walletRepository.saveAndFlush(receiver);

		String reference = "TRANSFER-" + UUID.randomUUID();
		Transaction senderTransaction = createTransferTransaction(savedSender, amount, reference);
		Transaction receiverTransaction = createTransferTransaction(receiver, amount, reference);

		Transaction savedSenderTransaction = transactionRepository.saveAndFlush(senderTransaction);
		transactionRepository.saveAndFlush(receiverTransaction);

		return toTransferTransactionResponse(savedSenderTransaction, savedSender, receiver);
	}

	@Override
	@Transactional
	public BillPaymentResponse payCurrentFacture(PayCurrentFactureRequest request) {
		Wallet wallet = findWalletByPhoneNumber(request.getPhoneNumber());
		BillPaymentHandler handler = billPaymentFactory.getHandler(request.getServiceName());
		ExternalFactureResponse facture = handler.findCurrentUnpaidFacture(wallet.getCode())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Aucune facture impayee du mois courant trouvee pour ce service"));

		if ("PAID".equalsIgnoreCase(facture.getStatus())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "La facture est deja payee");
		}

		BigDecimal amount = request.getAmount();
		if (wallet.getBalance().compareTo(amount) < 0) {
			throw new ResponseStatusException(
					HttpStatus.CONFLICT,
					"Solde insuffisant pour payer la facture");
		}

		wallet.setBalance(wallet.getBalance().subtract(amount));
		Wallet savedWallet = walletRepository.saveAndFlush(wallet);

		ExternalFactureResponse paidFacture = paymentServiceProxy.markFactureAsPaid(facture.getReference());

		Transaction transaction = new Transaction();
		transaction.setWallet(savedWallet);
		transaction.setAmount(amount);
		transaction.setPaymentMethod(PaymentMethod.WALLET);
		transaction.setType(TransactionType.BILL_PAYMENT);
		transaction.setStatus(TransactionStatus.SUCCESS);
		transaction.setReference("BILL-PAYMENT-" + UUID.randomUUID());
		transactionRepository.saveAndFlush(transaction);

		String factureReference = paidFacture != null && paidFacture.getReference() != null
				? paidFacture.getReference()
				: facture.getReference();

		return new BillPaymentResponse(
				savedWallet.getPhoneNumber(),
				savedWallet.getCode(),
				handler.serviceName(),
				amount,
				factureReference,
				savedWallet.getBalance(),
				TransactionType.BILL_PAYMENT,
				TransactionStatus.SUCCESS,
				"Facture payee avec succes");
	}

	private void validateUniqueWallet(CreateWalletRequest request) {
		if (walletRepository.existsByPhoneNumber(request.getPhoneNumber())) {
			throw new BusinessException("Un wallet existe deja avec ce numero de telephone");
		}
		if (walletRepository.existsByEmail(request.getEmail())) {
			throw new BusinessException("Un wallet existe deja avec cet email");
		}
		if (walletRepository.existsByCode(request.getCode())) {
			throw new BusinessException("Un wallet existe deja avec ce code");
		}
	}

	private Wallet findWalletByPhoneNumber(String phoneNumber) {
		return walletRepository.findByPhoneNumber(phoneNumber)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Aucun wallet trouve avec ce numero de telephone"));
	}

	private WalletResponse toResponse(Wallet wallet) {
		return new WalletResponse(
				wallet.getId(),
				wallet.getPhoneNumber(),
				wallet.getEmail(),
				wallet.getBalance(),
				wallet.getCode(),
				wallet.getCurrency(),
				wallet.getCreatedAt(),
				wallet.getUpdatedAt());
	}

	private WalletBalanceResponse toBalanceResponse(Wallet wallet) {
		return new WalletBalanceResponse(
				wallet.getPhoneNumber(),
				wallet.getCode(),
				wallet.getBalance(),
				wallet.getCurrency());
	}

	private BigDecimal calculateWithdrawFees(BigDecimal amount) {
		BigDecimal fees = amount.multiply(WITHDRAW_FEE_RATE);
		if (fees.compareTo(MAX_WITHDRAW_FEE) > 0) {
			fees = MAX_WITHDRAW_FEE;
		}
		return fees.setScale(2, RoundingMode.HALF_UP);
	}

	private Transaction createTransferTransaction(Wallet wallet, BigDecimal amount, String reference) {
		Transaction transaction = new Transaction();
		transaction.setWallet(wallet);
		transaction.setAmount(amount);
		transaction.setPaymentMethod(PaymentMethod.WALLET);
		transaction.setType(TransactionType.TRANSFER);
		transaction.setStatus(TransactionStatus.SUCCESS);
		transaction.setReference(reference);
		return transaction;
	}

	private TransactionResponse toTransactionResponse(Transaction transaction, Wallet wallet) {
		return new TransactionResponse(
				transaction.getId(),
				wallet.getId(),
				transaction.getAmount(),
				wallet.getBalance(),
				transaction.getPaymentMethod(),
				transaction.getType(),
				transaction.getStatus(),
				transaction.getReference(),
				transaction.getCreatedAt(),
				"Depot effectue avec succes");
	}

	private TransactionResponse toWithdrawTransactionResponse(
			Transaction transaction,
			Wallet wallet,
			BigDecimal fees,
			BigDecimal total) {
		return new TransactionResponse(
				transaction.getId(),
				wallet.getId(),
				transaction.getAmount(),
				fees,
				total,
				wallet.getBalance(),
				transaction.getPaymentMethod(),
				transaction.getType(),
				transaction.getStatus(),
				transaction.getReference(),
				transaction.getCreatedAt(),
				"Retrait effectue avec succes");
	}

	private TransactionResponse toTransferTransactionResponse(
			Transaction transaction,
			Wallet sender,
			Wallet receiver) {
		return new TransactionResponse(
				transaction.getId(),
				sender.getId(),
				transaction.getAmount(),
				null,
				null,
				sender.getBalance(),
				transaction.getPaymentMethod(),
				transaction.getType(),
				transaction.getStatus(),
				transaction.getReference(),
				transaction.getCreatedAt(),
				"Transfert effectue avec succes",
				sender.getPhoneNumber(),
				receiver.getPhoneNumber());
	}
}
