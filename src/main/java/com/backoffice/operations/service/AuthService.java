package com.backoffice.operations.service;

import com.backoffice.operations.payloads.LoginDto;
import com.backoffice.operations.payloads.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
