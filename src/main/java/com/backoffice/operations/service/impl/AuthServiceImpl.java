package com.backoffice.operations.service.impl;

import com.backoffice.operations.payloads.common.GenericResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backoffice.operations.entity.Role;
import com.backoffice.operations.entity.User;
import com.backoffice.operations.exceptions.BlogAPIException;
import com.backoffice.operations.payloads.LoginDto;
import com.backoffice.operations.payloads.RegisterDto;
import com.backoffice.operations.repository.RoleRepository;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;
import com.backoffice.operations.service.AuthService;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public GenericResponseDTO<User> register(RegisterDto registerDto) {

        GenericResponseDTO<User> genericResult = new GenericResponseDTO<>();
        genericResult.setStatus("Success");

        Boolean isUserNamePresent = userRepository.existsByUsername(registerDto.getEmail());
        // add check for username exists in database
        if(isUserNamePresent) {
            genericResult.setStatus("Error");
            genericResult.setMessage("Username is already exists!.");
            //throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        Boolean isEmailPresent = userRepository.existsByEmail(registerDto.getEmail());
        // add check for email exists in database
        if(isEmailPresent){
            genericResult.setStatus("Error");
            genericResult.setMessage("Email is already exists!.");
            //throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        try {
            userRepository.save(user);
            genericResult.setData(user);
            genericResult.setMessage("User registered successfully!.");
        } catch (Exception ex) {
            genericResult.setStatus("Error");
            //genericResult.setMessage(ex.getMessage());
            genericResult.setMessage("Something went wrong");
        }
        //return "User registered successfully!.";
        return genericResult;
    }
}
