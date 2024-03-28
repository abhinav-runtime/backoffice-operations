package com.backoffice.operations.service;


import com.backoffice.operations.payloads.CmsControlParameterDTO;

import java.util.List;

public interface CmsControlParameterService {

    List<CmsControlParameterDTO> getAllParameters();

    CmsControlParameterDTO getParameterById(String id);

    CmsControlParameterDTO createParameter(CmsControlParameterDTO parameterDTO);

    CmsControlParameterDTO updateParameter(String id, CmsControlParameterDTO parameterDTO);

    boolean deleteParameter(String id);
}