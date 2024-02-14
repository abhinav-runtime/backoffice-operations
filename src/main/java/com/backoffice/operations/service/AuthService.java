package com.backoffice.operations.service;

import com.backoffice.operations.entity.User;
import com.backoffice.operations.payloads.LoginDto;
import com.backoffice.operations.payloads.RegisterDto;
import com.backoffice.operations.payloads.common.GenericResponseDTO;

public interface AuthService {
    String login(LoginDto loginDto);

    GenericResponseDTO<User> register(RegisterDto registerDto);
}
