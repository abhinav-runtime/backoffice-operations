package com.backoffice.operations.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.TransactionLimitsEntity;
import com.backoffice.operations.entity.TransactionMaxMinLimitsParameter;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.TransactionLimitsDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.TransactionLimitsRepository;
import com.backoffice.operations.repository.TransactionMaxMinLimitsParameterRepo;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.TransactionLimitService;

@Service
public class TransactionLimitServiceImpl implements TransactionLimitService{
	
	@Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;
    
	@Autowired
	private TransactionMaxMinLimitsParameterRepo transactionMaxLimitsParameterRepo;
	
	@Autowired
    private TransactionLimitsRepository transactionLimitsRepository;	
	
	@Override
	public GenericResponseDTO<Object> setMerchantOutletLimits(TransactionLimitsDTO transactionLimitsDTO, String token) {
		
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        
        if (user.isPresent()) {
        	
        	TransactionLimitsEntity transactionLimitsEntity = transactionLimitsRepository.findByCustomerId(transactionLimitsDTO.getCustomerId());
        	long id = 1;
    		TransactionMaxMinLimitsParameter transactionMaxMinLimitsParameter = transactionMaxLimitsParameterRepo.findById(id).orElse(null);
    		double merchantOutletMaxLimits = transactionMaxMinLimitsParameter.getMerchantOutletMaxLimits();
    		double merchantOutletMinLimits = transactionMaxMinLimitsParameter.getMerchantOutletMinLimits();
    		
        	if (transactionLimitsEntity != null) {
    		
        		if(transactionLimitsDTO.getMerchantOutletLimits() > merchantOutletMaxLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("Merchant outlet limits should not exceed 100000 OMR.");
        			return responseDTO;
        		}
        		if((transactionLimitsDTO.getMerchantOutletLimits() >= merchantOutletMinLimits)&&(transactionLimitsDTO.getMerchantOutletLimits() <= merchantOutletMaxLimits)) {
        			transactionLimitsEntity.setMerchantOutletLimits(transactionLimitsDTO.getMerchantOutletLimits());
        		}
        		if(transactionLimitsDTO.getMerchantOutletLimits() < merchantOutletMinLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("Merchant outlet limits should not below 1 OMR.");
        			return responseDTO;
        		}
        		transactionLimitsRepository.save(transactionLimitsEntity);
        		Map<String, Object> data = new HashMap<>();
        		responseDTO.setStatus("Success");
        		responseDTO.setMessage("Merchant outlet transaction limits is set!");
        		data.put("Transaction limits", transactionLimitsDTO);
        		responseDTO.setData(data);
        		return responseDTO;
        	}
        	if(transactionLimitsEntity == null) {
        		
        		TransactionLimitsEntity newTransactionLimitsEntity = new TransactionLimitsEntity();
        		
        		if(transactionLimitsDTO.getMerchantOutletLimits() > merchantOutletMaxLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("Merchant outlet limits should not exceed 100000 OMR.");
        			return responseDTO;
        		}
        		if((transactionLimitsDTO.getMerchantOutletLimits() >= merchantOutletMinLimits)&&(transactionLimitsDTO.getMerchantOutletLimits() <= merchantOutletMaxLimits)) {
        			newTransactionLimitsEntity.setMerchantOutletLimits(transactionLimitsDTO.getMerchantOutletLimits());
        			newTransactionLimitsEntity.setCustomerId(transactionLimitsDTO.getCustomerId());
        		}
        		if(transactionLimitsDTO.getMerchantOutletLimits() < merchantOutletMinLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("Merchant outlet limits should not below 1 OMR.");
        			return responseDTO;
        		}
        		transactionLimitsRepository.save(newTransactionLimitsEntity);
        		Map<String, Object> data = new HashMap<>();
        		responseDTO.setStatus("Success");
        		responseDTO.setMessage("Merchant outlet transaction limits is added!");
        		data.put("Transaction limits", transactionLimitsDTO);
        		responseDTO.setData(data);
        		return responseDTO;
        	}
		}
		responseDTO.setStatus("Failure");
    	responseDTO.setMessage("Something went wrong");
    	return responseDTO;
	}
	
	@Override
	public GenericResponseDTO<Object> setOnlineShoppingLimits(TransactionLimitsDTO.OnlineShopping onlineShoppingLimitDTO, String token) {
		
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        
        if (user.isPresent()) {
        	
        	TransactionLimitsEntity transactionLimitsEntity = transactionLimitsRepository.findByCustomerId(onlineShoppingLimitDTO.getCustomerId());
        	long id = 1;
    		TransactionMaxMinLimitsParameter transactionMaxMinLimitsParameter = transactionMaxLimitsParameterRepo.findById(id).orElse(null);
    		double onlineShoppingMaxLimits = transactionMaxMinLimitsParameter.getOnlineShoppingMaxLimits();
    		double onlineShoppingMinLimits = transactionMaxMinLimitsParameter.getOnlineShoppingMinLimits();
    		
        	if (transactionLimitsEntity != null) {
        		
        		if(onlineShoppingLimitDTO.getOnlineShoppingLimits() > onlineShoppingMaxLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("Online shopping limits should not exceed 100000 OMR.");
        			return responseDTO;
        		}
        		if((onlineShoppingLimitDTO.getOnlineShoppingLimits() >= onlineShoppingMinLimits)&&(onlineShoppingLimitDTO.getOnlineShoppingLimits() <= onlineShoppingMaxLimits)) {
        			transactionLimitsEntity.setOnlineShoppingLimits(onlineShoppingLimitDTO.getOnlineShoppingLimits());
        		}
        		if(onlineShoppingLimitDTO.getOnlineShoppingLimits() < onlineShoppingMinLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("Online shopping limits should not below 1 OMR.");
        			return responseDTO;
        		}
        		transactionLimitsRepository.save(transactionLimitsEntity);
        		Map<String, Object> data = new HashMap<>();
        		responseDTO.setStatus("Success");
        		responseDTO.setMessage("Online Shopping transaction limits is set!");
        		data.put("Transaction limits", onlineShoppingLimitDTO);
        		responseDTO.setData(data);
        		return responseDTO;
        	}
        	
        	if(transactionLimitsEntity == null) {
        		
        		TransactionLimitsEntity newTransactionLimitsEntity = new TransactionLimitsEntity();
        		
        		if(onlineShoppingLimitDTO.getOnlineShoppingLimits() > onlineShoppingMaxLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("Online shopping limits should not exceed 100000 OMR.");
        			return responseDTO;
        		}
        		if((onlineShoppingLimitDTO.getOnlineShoppingLimits() >= onlineShoppingMinLimits)&&(onlineShoppingLimitDTO.getOnlineShoppingLimits() <= onlineShoppingMaxLimits)) {
        			newTransactionLimitsEntity.setOnlineShoppingLimits(onlineShoppingLimitDTO.getOnlineShoppingLimits());
        			newTransactionLimitsEntity.setCustomerId(onlineShoppingLimitDTO.getCustomerId());
        		}
        		if(onlineShoppingLimitDTO.getOnlineShoppingLimits() < onlineShoppingMinLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("Online shopping limits should not below 1 OMR.");
        			return responseDTO;
        		}
        		transactionLimitsRepository.save(newTransactionLimitsEntity);
        		Map<String, Object> data = new HashMap<>();
        		responseDTO.setStatus("Success");
        		responseDTO.setMessage("Online Shopping transaction limits is added!");
        		data.put("Transaction limits", onlineShoppingLimitDTO);
        		responseDTO.setData(data);
        		return responseDTO;
        	}
        }
        responseDTO.setStatus("Failure");
    	responseDTO.setMessage("Something went wrong");
    	return responseDTO;
	}
	
	@Override
	public GenericResponseDTO<Object> setAtmWithdrawalLimits(TransactionLimitsDTO.ATMwithdrawal atmWithdrawalLimitDTO, String token) {
		
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        
        if (user.isPresent()) {
        	
        	TransactionLimitsEntity transactionLimitsEntity = transactionLimitsRepository.findByCustomerId(atmWithdrawalLimitDTO.getCustomerId());        	
        	long id = 1;
    		TransactionMaxMinLimitsParameter transactionMaxMinLimitsParameter = transactionMaxLimitsParameterRepo.findById(id).orElse(null);
    		double atmWithdrawalMaxLimits = transactionMaxMinLimitsParameter.getAtmWithdrawalMaxLimits();
    		double atmWithdrawalMinLimits = transactionMaxMinLimitsParameter.getAtmWithdrawalMinLimits();
    		
        	if (transactionLimitsEntity != null) {
    		
        		if(atmWithdrawalLimitDTO.getAtmWithdrawalLimits() > atmWithdrawalMaxLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("ATM withdrawal limits should not exceed 100000 OMR.");
        			return responseDTO;
        		}
        		if((atmWithdrawalLimitDTO.getAtmWithdrawalLimits() >= atmWithdrawalMinLimits)&&(atmWithdrawalLimitDTO.getAtmWithdrawalLimits() <= atmWithdrawalMaxLimits)) {
        			transactionLimitsEntity.setAtmWithdrawalLimits(atmWithdrawalLimitDTO.getAtmWithdrawalLimits());
        		}
        		if(atmWithdrawalLimitDTO.getAtmWithdrawalLimits() < atmWithdrawalMinLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("ATM withdrawal limits should not below 1 OMR.");
        			return responseDTO;
        		}
        		transactionLimitsRepository.save(transactionLimitsEntity);
        		Map<String, Object> data = new HashMap<>();
        		responseDTO.setStatus("Success");
        		responseDTO.setMessage("ATM withdrawal limits is set!");
        		data.put("Transaction limits", atmWithdrawalLimitDTO);
        		responseDTO.setData(data);
        		return responseDTO;
        	}
        	if (transactionLimitsEntity == null){
        		
        		TransactionLimitsEntity newTransactionLimitsEntity = new TransactionLimitsEntity();
        		
        		if(atmWithdrawalLimitDTO.getAtmWithdrawalLimits() > atmWithdrawalMaxLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("ATM withdrawal limits should not exceed 100000 OMR.");
        			return responseDTO;
        		}
        		if((atmWithdrawalLimitDTO.getAtmWithdrawalLimits() >= atmWithdrawalMinLimits)&&(atmWithdrawalLimitDTO.getAtmWithdrawalLimits() <= atmWithdrawalMaxLimits)) {
        			newTransactionLimitsEntity.setAtmWithdrawalLimits(atmWithdrawalLimitDTO.getAtmWithdrawalLimits());
        			newTransactionLimitsEntity.setCustomerId(atmWithdrawalLimitDTO.getCustomerId());
        		}
        		if(atmWithdrawalLimitDTO.getAtmWithdrawalLimits() < atmWithdrawalMinLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("ATM withdrawal limits should not below 1 OMR.");
        			return responseDTO;
        		}
        		transactionLimitsRepository.save(newTransactionLimitsEntity);
        		Map<String, Object> data = new HashMap<>();
        		responseDTO.setStatus("Success");
        		responseDTO.setMessage("ATM withdrawal limits is added!");
        		data.put("Transaction limits", atmWithdrawalLimitDTO);
        		responseDTO.setData(data);
        		return responseDTO;
        	}
        }
        responseDTO.setStatus("Failure");
    	responseDTO.setMessage("Something went wrong");
    	return responseDTO;
	}
	
	@Override
	public GenericResponseDTO<Object> setTapAndPayLimits(TransactionLimitsDTO.TapAndPay tapAndPayLimitDTO, String token) {
		
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
        
        if (user.isPresent()) {
        	
        	TransactionLimitsEntity transactionLimitsEntity = transactionLimitsRepository.findByCustomerId(tapAndPayLimitDTO.getCustomerId());
        	long id = 1;
    		TransactionMaxMinLimitsParameter transactionMaxMinLimitsParameter = transactionMaxLimitsParameterRepo.findById(id).orElse(null);
    		double tapAndPayMaxLimits = transactionMaxMinLimitsParameter.getTapAndPayMaxLimits();    		
    		double tapAndPayMinLimits = transactionMaxMinLimitsParameter.getTapAndPayMinLimits();
    		
        	if (transactionLimitsEntity != null) {   
        		
        		if(tapAndPayLimitDTO.getTapAndPayLimits() > tapAndPayMaxLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("Tap and pay limits should not exceed 100000 OMR.");
        			return responseDTO;
        		}
        		if((tapAndPayLimitDTO.getTapAndPayLimits() >= tapAndPayMinLimits)&&(tapAndPayLimitDTO.getTapAndPayLimits() <= tapAndPayMaxLimits)) {
        			transactionLimitsEntity.setTapAndPayLimits(tapAndPayLimitDTO.getTapAndPayLimits());
        		}
        		if(tapAndPayLimitDTO.getTapAndPayLimits() < tapAndPayMinLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("Tap and pay limits should not below 1 OMR.");
        			return responseDTO;
        		}
        		transactionLimitsRepository.save(transactionLimitsEntity);
        		Map<String, Object> data = new HashMap<>();
        		responseDTO.setStatus("Success");
        		responseDTO.setMessage("Tap and pay transaction limits is set!");
        		data.put("Transaction limits", tapAndPayLimitDTO);
        		responseDTO.setData(data);
        		return responseDTO;
        	}  
        	if (transactionLimitsEntity == null){
        		
        		TransactionLimitsEntity newTransactionLimitsEntity = new TransactionLimitsEntity();
        		
        		if(tapAndPayLimitDTO.getTapAndPayLimits() > tapAndPayMaxLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("Tap and pay limits should not exceed 100000 OMR.");
        			return responseDTO;
        		}
        		if((tapAndPayLimitDTO.getTapAndPayLimits() >= tapAndPayMinLimits)&&(tapAndPayLimitDTO.getTapAndPayLimits() <= tapAndPayMaxLimits)) {
        			newTransactionLimitsEntity.setTapAndPayLimits(tapAndPayLimitDTO.getTapAndPayLimits());
        			newTransactionLimitsEntity.setCustomerId(tapAndPayLimitDTO.getCustomerId());
        		}
        		if(tapAndPayLimitDTO.getTapAndPayLimits() < tapAndPayMinLimits) {
        			responseDTO.setStatus("Failure");
        			responseDTO.setMessage("Tap and pay limits should not below 1 OMR.");
        			return responseDTO;
        		}
        		transactionLimitsRepository.save(newTransactionLimitsEntity);
        		Map<String, Object> data = new HashMap<>();
        		responseDTO.setStatus("Success");
        		responseDTO.setMessage("Tap and pay transaction limits is added!");
        		data.put("Transaction limits", tapAndPayLimitDTO);
        		responseDTO.setData(data);
        		return responseDTO;
        	}
        }
        responseDTO.setStatus("Failure");
    	responseDTO.setMessage("Something went wrong");
    	return responseDTO;
	}
	
	
	@Override
	public GenericResponseDTO<Object> getAllTransactionLimits(String token) {
		
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);       
		
		if (user.isPresent()) {
			List<TransactionLimitsEntity> transactionLimitsEntity = transactionLimitsRepository.findAll();
			Map<String, Object> data = new HashMap<>();
			responseDTO.setStatus("Success");
			responseDTO.setMessage("Customers transaction limits list!");
			data.put("list", transactionLimitsEntity);
			responseDTO.setData(data);
			return responseDTO;	
		}
		responseDTO.setStatus("Failure");
    	responseDTO.setMessage("Something went wrong");
    	return responseDTO; 
	}
	
	@Override
	public GenericResponseDTO<Object> getTransactionLimitsByCustId(String customerId, String token) {
		
		GenericResponseDTO<Object> responseDTO = new GenericResponseDTO<>();
		String userEmail = jwtTokenProvider.getUsername(token);
        Optional<User> user = userRepository.findByEmail(userEmail);
       
        if (user.isPresent()) {
        	TransactionLimitsEntity transactionLimitsEntity = transactionLimitsRepository.findByCustomerId(customerId);
        	Map<String, Object> data = new HashMap<>();
        	responseDTO.setStatus("Success");
        	responseDTO.setMessage("Customer transaction limits list!");
        	data.put("list", transactionLimitsEntity);
        	responseDTO.setData(data);
        	return responseDTO;
        }  
        responseDTO.setStatus("Failure");
    	responseDTO.setMessage("Something went wrong");
    	return responseDTO;    	
	}
}
