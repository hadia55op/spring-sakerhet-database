package com.example.spring_sakerhet_database.service;
import java.util.List;
import java.util.stream.Collectors;




import com.example.spring_sakerhet_database.dto.UserDto;

import com.example.spring_sakerhet_database.entity.User;
import com.example.spring_sakerhet_database.repository.RoleRepository;
import com.example.spring_sakerhet_database.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public Optional<UserDto> getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        return userOptional.map(this::mapToDTO);
    }
    public UserDto createUser(User user) {
        user.setRegistrationDate(LocalDate.now());
        User saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    private UserDto mapToDTO(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRegistrationDate(user.getRegistrationDate());
        return dto;
    }
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


}





