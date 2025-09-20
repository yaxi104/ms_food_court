package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.request.Auth;
import com.hexagonal.ms_foodcourt.domain.model.response.TokenResponse;

import java.util.Optional;

public interface IAuthFeignPort {

    Optional<TokenResponse> getToken(Auth auth);
}
