package org.cartradingplatform.service;

import org.cartradingplatform.model.dto.response.ProfileDTO;

public interface AccountUserService {
    ProfileDTO getUserProfile(Long id);
}
