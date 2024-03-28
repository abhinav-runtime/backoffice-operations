package com.backoffice.operations.service.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.DuplicateMappingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.backoffice.operations.entity.BankList;
import com.backoffice.operations.exceptions.DuplicateEntryException;
import com.backoffice.operations.payloads.BankListDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.repository.BankListRepo;
import com.backoffice.operations.service.BankListService;

import jakarta.persistence.Entity;

@Service
public class BankListServiceImp implements BankListService {
	private static final Logger logger = LoggerFactory.getLogger(BankListServiceImp.class);
	ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private BankListRepo bankListRepo;

	@Override
	public List<BankList> getAllBanks() {
		List<BankList> bankList = bankListRepo.findAll();
		return bankList;
	}

	@Override
	public GenericResponseDTO<Object> getBankById(String id) {
		logger.info("getBankById : {}", id);
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		try {
			BankList bank = bankListRepo.findById(id).get();
			BankListDTO bankDTO = modelMapper.map(bank, BankListDTO.class);
			response.setStatus("Success");
			response.setMessage("Bank found successfully.");
			response.setData(bankDTO);
			return response;
		} catch (Exception e) {
			logger.error("Error on getBankById : {}", e.getMessage());
			response.setStatus("Failure");
			response.setMessage("Bank not found.");
			response.setData(new HashMap<>());
			return response;
		}
	}

	@Override
	public GenericResponseDTO<Object> getBankByBicCode(String bicCode) {
		logger.info("getBankByBicCode : {}", bicCode);
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		try {
			BankList bank = bankListRepo.findByBicCode(bicCode);
			BankListDTO bankDTO = modelMapper.map(bank, BankListDTO.class);
			response.setStatus("Success");
			response.setMessage("Bank found successfully.");
			response.setData(bankDTO);
			return response;
		} catch (Exception e) {
			logger.error("Error on getBankByBicCode : {}", e.getMessage());
			response.setStatus("Failure");
			response.setMessage("Bank not found.");
			response.setData(new HashMap<>());
			return response;
		}
	}

	@Override
	public GenericResponseDTO<Object> getBankByBankName(String bankName) {
		logger.info("getBankByBankName : {}", bankName);
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		try {
			BankList bank = bankListRepo.findByBankName(bankName);
			BankListDTO bankDTO = modelMapper.map(bank, BankListDTO.class);
			response.setStatus("Success");
			response.setMessage("Bank found successfully.");
			response.setData(bankDTO);
			return response;
		} catch (Exception e) {
			logger.error("Error on getBankByBankName : {}", e.getMessage());
			response.setStatus("Failure");
			response.setMessage("Bank not found.");
			response.setData(new HashMap<>());
			return response;
		}
	}

	@Override
	public GenericResponseDTO<Object> addBank(BankListDTO bank) {
		logger.info("addBank : {}", bank);
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		try {
			BankList bankList = modelMapper.map(bank, BankList.class);
			bankList = bankListRepo.save(bankList);
			response.setData(modelMapper.map(bankList, BankListDTO.class));
			response.setMessage("Bank added successfully.");
			response.setStatus("Success");
			return response;
		} catch (DataIntegrityViolationException e) {
			logger.error("Error on addBank : {}", e.getMessage());
			response.setStatus("Failure");
			response.setMessage("Bank already exists.");
			response.setData(new HashMap<>());
			return response;
		} catch (Exception e) {
			logger.error("Error on addBank : {}", e.getMessage());
			response.setStatus("Failure");
			response.setMessage("Bank not added.");
			response.setData(new HashMap<>());
			return response;
		}

	}

	@Override
	public GenericResponseDTO<Object> updateBankById(String Id, BankListDTO bank) {
		logger.info("updateBankById : {}", Id);
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		try {
			BankList bankList = bankListRepo.findById(Id).get();
			if (bank.getBankName() != null && !bank.getBankName().isEmpty() && bank.getBankName() != "") {
				bankList.setBankName(bank.getBankName());
			}
			if (bank.getBicCode() != null && !bank.getBicCode().isEmpty() && bank.getBicCode() != "") {
				bankList.setBicCode(bank.getBicCode());
			}
			bankList = bankListRepo.save(bankList);
			response.setData(modelMapper.map(bankList, BankListDTO.class));
			response.setMessage("Bank updated successfully.");
			response.setStatus("Success");
			return response;
		} catch (DataIntegrityViolationException e) {
			logger.error("Error on addBank : {}", e.getMessage());
			response.setStatus("Failure");
			response.setMessage("Bank already exists.");
			response.setData(new HashMap<>());
			return response;
		} catch (Exception e) {
			logger.error("Error on updateBankById : {}", e.getMessage());
			response.setStatus("Failure");
			response.setMessage("Bank not updated.");
			response.setData(new HashMap<>());
			return response;
		}
	}

	@Override
	public GenericResponseDTO<Object> deleteBankById(String id) {
		logger.info("deleteBankById : {}", id);
		GenericResponseDTO<Object> response = new GenericResponseDTO<Object>();
		try {
			BankListDTO bank = modelMapper.map(bankListRepo.findById(id).get(), BankListDTO.class);
			bankListRepo.deleteById(id);
			response.setStatus("Success");
			response.setMessage("Bank deleted successfully.");
			response.setData(bank);
			return response;
		} catch (Exception e) {
			logger.error("Error on deleteBankById : {}", e.getMessage());
			response.setStatus("Failure");
			response.setMessage("Bank not deleted.");
			response.setData(new HashMap<>());
			return response;
		}
	}

}
