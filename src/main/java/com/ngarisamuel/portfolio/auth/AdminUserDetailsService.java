package com.ngarisamuel.portfolio.auth;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin user not found"));

        return User.withUsername(adminUser.getUsername())
                .password(adminUser.getPassword())
                .authorities(List.of(adminUser.getRole()).toArray(String[]::new))
                .disabled(!adminUser.isEnabled())
                .accountLocked(adminUser.isLocked())
                .build();
    }
}
