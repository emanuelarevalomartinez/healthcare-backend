package com.healthcare.modules.auth.service;

import com.healthcare.config.security.JwtGenerator;
import com.healthcare.modules.auth.dto.RefreshTokenResponseDTO;
import com.healthcare.modules.auth.entity.RefreshTokenEntity;
import com.healthcare.modules.auth.repository.RefreshTokenRepository;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import io.jsonwebtoken.Claims;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtGenerator jwtGenerator;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(JwtGenerator jwtGenerator, RefreshTokenRepository refreshTokenRepository) {
        this.jwtGenerator = jwtGenerator;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String createAndSaveRefreshToken(UserEntity user) {
        return saveRefreshToken(user);
    }


    @Override
    public RefreshTokenResponseDTO validateAndSaveNewRefreshToken(String refreshToken) {
      try{
          if (!jwtGenerator.validateToken(refreshToken)) {
              throw new ApplicationException(ErrorMessage.JWT_EXPIRED, "");
          }

          if (!jwtGenerator.isRefreshToken(refreshToken)) {
              throw new ApplicationException(ErrorMessage.JWT_INVALID_TYPE, "");
          }

          String tokenHash = hashToken(refreshToken);

          RefreshTokenEntity storedToken = refreshTokenRepository
                  .findByTokenHashAndRevokedFalse(tokenHash)
                  .orElseThrow(() ->
                          new ApplicationException(ErrorMessage.REFRESH_TOKEN_INVALID, "")
                  );

          storedToken.setRevoked(true);
          refreshTokenRepository.save(storedToken);

          UserEntity user = storedToken.getUser();

          storedToken.setRevoked(true);
          refreshTokenRepository.save(storedToken);

          String newAccessToken = jwtGenerator.generateAccessToken(user);
          String newRefreshToken = this.saveRefreshToken(user);

          return new RefreshTokenResponseDTO(
                  newAccessToken,
                  newRefreshToken
          );

      } catch(ApplicationException ex){
          throw ex;
      } catch(Exception ex) {
          throw new ApplicationException(ex);
      }
    }

    private String saveRefreshToken(UserEntity user){

        String refreshToken = jwtGenerator.generateRefreshToken(user);

        Date jwtExpiry = jwtGenerator.getClaimFromToken(
                refreshToken,
                Claims::getExpiration
        );

        Instant expiry = jwtExpiry.toInstant();

        RefreshTokenEntity tokenEntity = new RefreshTokenEntity();
        tokenEntity.setUser(user);
        tokenEntity.setTokenHash(hashToken(refreshToken));
        tokenEntity.setExpiresAt(expiry);
        tokenEntity.setRevoked(false);

        refreshTokenRepository.save(tokenEntity);

        return refreshToken;
    }

    private String hashToken(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
