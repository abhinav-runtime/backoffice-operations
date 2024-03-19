package com.backoffice.operations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backoffice.operations.payloads.AnnexureTransferLimitsDTO;
import com.backoffice.operations.payloads.AnnexureTransferWithSubLimitsDTO.AnnexureTransferWithSubLimitsRequestDTO;
import com.backoffice.operations.payloads.common.GenericResponseDTO;
import com.backoffice.operations.service.AnnexureTransferLimitService;

@RestController
@RequestMapping("/bo/annexure-transfer-limits")
public class AnnexureTransferLimitController {
	@Autowired
	private AnnexureTransferLimitService annexureTransferLimitService;

	@GetMapping("/get-annexure-transfer-limits")
	public GenericResponseDTO<Object> getAllAnnexureTransferLimits() {
		return annexureTransferLimitService.getAllAnnexureTransferLimits();
	}

	@GetMapping("get-annexure-transfer-limits/{id}")
	public GenericResponseDTO<Object> getAnnexureTransferLimitsById(@PathVariable(name = "id") String id) {
		return annexureTransferLimitService.getAnnexureTransferLimitsById(id);
	}

	@PostMapping("/create-annexure-transfer-limits")
	public GenericResponseDTO<Object> addAnnexureTransferLimits(@RequestBody AnnexureTransferLimitsDTO requestDTO) {
		return annexureTransferLimitService.createAnnexureTransferLimits(requestDTO);
	}
	
	@PutMapping("/update-annexure-transfer-limits/{id}")
	public GenericResponseDTO<Object> updateAnnexureTransferLimits(@PathVariable(name = "id") String id,@RequestBody AnnexureTransferLimitsDTO requestDTO) {
		return annexureTransferLimitService.updateAnnexureTransferLimits(id,requestDTO);
	}
	
	@DeleteMapping("/delete-annexure-transfer-limits/{id}")
	public GenericResponseDTO<Object> deleteAnnexureTransferLimits(@PathVariable(name = "id") String id) {
		return annexureTransferLimitService.deleteAnnexureTransferLimits(id);
	}
	
	@GetMapping("/get-annexure-transfer-with-sub-limits")
	public GenericResponseDTO<Object> getAllAnnexureTransferWithSubLimits() {
		return annexureTransferLimitService.getAllAnnexureTransferWithSubLimits();
	}
	
	@GetMapping("get-annexure-transfer-with-sub-limits/{id}")
	public GenericResponseDTO<Object> getAnnexureTransferWithSubLimitsById(@PathVariable(name = "id") String id) {
		return annexureTransferLimitService.getAnnexureTransferWithSubLimitsById(id);
	}

	@PostMapping("/create-annexure-transfer-with-sub-limits")
	public GenericResponseDTO<Object> addAnnexureTransferWithSubLimits(
			@RequestBody AnnexureTransferWithSubLimitsRequestDTO requestDTO) {
		return annexureTransferLimitService.createAnnexureTransferWithSubLimits(requestDTO);
	}
	
	@PutMapping("/update-annexure-transfer-with-sub-limits/{id}")
	public GenericResponseDTO<Object> updateAnnexureTransferWithSubLimits(@PathVariable(name = "id") String id,
			@RequestBody AnnexureTransferWithSubLimitsRequestDTO requestDTO) {
		return annexureTransferLimitService.updateAnnexureTransferWithSubLimits(id, requestDTO);
	}
	
	@DeleteMapping("/delete-annexure-transfer-with-sub-limits/{id}")
	public GenericResponseDTO<Object> deleteAnnexureTransferWithSubLimits(@PathVariable(name = "id") String id) {
		return annexureTransferLimitService.deleteAnnexureTransferWithSubLimits(id);
	}
}
