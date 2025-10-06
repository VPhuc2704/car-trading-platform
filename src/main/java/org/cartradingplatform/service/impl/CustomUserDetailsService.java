package org.cartradingplatform.service.impl;

import org.cartradingplatform.exceptions.UsersException;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.repository.UserRepository;
import org.cartradingplatform.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsersEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsersException("User not found with email: " + email));

        if (!user.isActive()) {
            throw new UsersException("Tài khoản của bạn đã bị khóa vĩnh viễn.");
        }

        if (user.isSuspended()) {
            LocalDateTime end = user.getSuspensionEnd();

            if (end == null || end.isAfter(LocalDateTime.now())) {
                String message = "Tài khoản của bạn đang bị tạm khóa.";
                if (end != null) {
                    message += " Bạn có thể đăng nhập lại sau: " + end;
                }
                throw new UsersException(message);
            } else {
                // 🕓 Đã hết hạn tạm khóa → mở lại
                user.setSuspended(false);
                user.setSuspensionEnd(null);
                userRepository.save(user);
            }
        }

        return new CustomUserDetails(user);
    }
}
