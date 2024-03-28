package com.backoffice.operations.service;

import java.util.List;

import com.backoffice.operations.entity.BankList;
import com.backoffice.operations.payloads.BankListDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface BankListService {
	List<BankList> getAllBanks();

	GenericResponseDTO<Object> getBankById(String id);

	GenericResponseDTO<Object> getBankByBicCode(String bicCode);

	GenericResponseDTO<Object> getBankByBankName(String bankName);

	GenericResponseDTO<Object> addBank(BankListDTO bank);

	GenericResponseDTO<Object> updateBankById(String Id, BankListDTO bank);

	GenericResponseDTO<Object> deleteBankById(String id);

}
