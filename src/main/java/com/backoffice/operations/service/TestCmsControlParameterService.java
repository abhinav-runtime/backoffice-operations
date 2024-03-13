package com.backoffice.operations.service;

import com.backoffice.operations.payloads.TestCmsControlParameterDTO;
import java.util.List;

public interface TestCmsControlParameterService {

    List<TestCmsControlParameterDTO> getAllParameters();

    TestCmsControlParameterDTO getParameterById(String id);

    TestCmsControlParameterDTO createParameter(TestCmsControlParameterDTO parameterDTO);

    TestCmsControlParameterDTO updateParameter(String id, TestCmsControlParameterDTO parameterDTO);

    void deleteParameter(String id);
}