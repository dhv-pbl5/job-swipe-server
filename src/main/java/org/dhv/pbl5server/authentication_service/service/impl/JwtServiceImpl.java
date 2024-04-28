package org.dhv.pbl5server.authentication_service.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.config.JwtApplicationProperty;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.response.CredentialResponse;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.authentication_service.service.JwtService;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.constant.RedisCacheConstant;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.ForbiddenException;
import org.dhv.pbl5server.common_service.exception.UnauthorizedException;
import org.dhv.pbl5server.common_service.repository.RedisRepository;
import org.dhv.pbl5server.common_service.utils.LogUtils;
import org.dhv.pbl5server.constant_service.enums.SystemRoleName;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtApplicationProperty jwtAppProperty;
    private final AccountRepository accountRepository;
    private final RedisRepository redisRepository;

    public UserDetails getAccountFromToken(String token) {
        Claims jwtClaims = getJwtClaims(token, TokenType.ACCESS_TOKEN);
        UUID accountId = UUID.fromString(jwtClaims.getSubject());
        try {
            if (redisRepository.findAllByHashKeyPrefix(
                RedisCacheConstant.AUTH_KEY,
                RedisCacheConstant.REVOKE_ACCESS_TOKEN_HASH(accountId.toString(), false)).contains(token)) {
                throw new UnauthorizedException(ErrorMessageConstant.REVOKED_TOKEN);
            }
        } catch (UnauthorizedException ex) {
            throw ex;
        } catch (Exception ex) {
            LogUtils.error("JWT SERVICE IMPL", ex);
        }
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new ForbiddenException(ErrorMessageConstant.FORBIDDEN));
    }

    public CredentialResponse generateToken(String accountId) {
        String accessToken = generateAccessToken(accountId);
        return CredentialResponse.builder()
            .accessToken(accessToken)
            .refreshToken(generateRefreshToken(accountId))
            .expiredAt(new Timestamp(getJwtClaims(accessToken, TokenType.ACCESS_TOKEN).getExpiration().getTime()))
            .type(CommonConstant.JWT_TYPE)
            .build();

    }

    public CredentialResponse refreshToken(String refreshToken, boolean isAdmin) {
        Claims jwtClaims = getJwtClaims(refreshToken, TokenType.REFRESH_TOKEN);
        UUID accountId = UUID.fromString(jwtClaims.getSubject());
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new ForbiddenException(ErrorMessageConstant.FORBIDDEN));
        // Check if the user is an admin
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        if (isAdmin && role != SystemRoleName.ADMIN) {
            throw new ForbiddenException(ErrorMessageConstant.FORBIDDEN);
        }
        if (account.getRefreshToken().equals(refreshToken)) {
            String accessToken = generateAccessToken(accountId.toString());
            return CredentialResponse.builder()
                .accessToken(accessToken)
                .type(CommonConstant.JWT_TYPE)
                .refreshToken(account.getRefreshToken())
                .expiredAt(
                    new Timestamp(getJwtClaims(accessToken, TokenType.ACCESS_TOKEN).getExpiration().getTime()))
                .build();
        }
        throw new BadRequestException(ErrorMessageConstant.REFRESH_TOKEN_NOT_FOUND);
    }

    private String generateAccessToken(String accountId) {
        return Jwts.builder()
            .subject(accountId)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtAppProperty.getAccessTokenExpirationMs()))
            .signWith(getSignInKey(jwtAppProperty.getAccessTokenSecret()))
            .compact();
    }

    private String generateRefreshToken(String accountId) {
        return Jwts.builder()
            .subject(accountId)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtAppProperty.getRefreshTokenExpirationMs()))
            .signWith(getSignInKey(jwtAppProperty.getRefreshTokenSecret()))
            .compact();
    }

    private Claims getJwtClaims(String token, TokenType tokenType) {
        switch (tokenType) {
            case ACCESS_TOKEN:
                try {
                    return Jwts.parser()
                        .verifyWith(getSignInKey(jwtAppProperty.getAccessTokenSecret()))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
                } catch (ExpiredJwtException ex) {
                    throw new UnauthorizedException(ErrorMessageConstant.EXPIRED_TOKEN);
                } catch (Exception ex) {
                    throw new UnauthorizedException(ErrorMessageConstant.INVALID_TOKEN);
                }
            case REFRESH_TOKEN:
                try {
                    return Jwts.parser()
                        .verifyWith(getSignInKey(jwtAppProperty.getRefreshTokenSecret()))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
                } catch (ExpiredJwtException ex) {
                    throw new UnauthorizedException(ErrorMessageConstant.EXPIRED_REFRESH_TOKEN);
                } catch (Exception ex) {
                    throw new UnauthorizedException(ErrorMessageConstant.INVALID_REFRESH_TOKEN);
                }
            default:
                throw new UnauthorizedException(ErrorMessageConstant.INVALID_TOKEN);
        }
    }

    private SecretKey getSignInKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

enum TokenType {
    ACCESS_TOKEN, REFRESH_TOKEN
}
