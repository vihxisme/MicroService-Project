package com.service.auth.securities;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.service.auth.configs.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

  private final JwtConfig jwtConfig;

  private final Key key;

  public JwtTokenProvider(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
    this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtConfig.getJwtSecret().getBytes()));
  }

  // Tạo token JWT từ thông tin user
  public String generateToken(String id, String username, String email, String role) {

    return Jwts.builder()
        .setSubject(id)
        .claim("username", username)
        .claim("email", email)
        .claim("role", role)
        .setIssuer(jwtConfig.getJwtIssuer())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getJwtExpiration()))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  // Lấy userId từ token jwt
  public String getUserIdFromJWT(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  // lấy username từ token jwt
  public String getUsernameFromJWT(String token) {
    return (String) Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("username");
  }

  // Lây email từ token jwt
  public String getEmailFromJWT(String token) {
    return (String) Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("email");
  }

  // Lấy role từ token jwt
  public String getRoleFromJWT(String token) {
    return (String) Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("role");
  }

  /**
   * Kiểm tra tính hợp lệ của token JWT.
   *
   * @throws ExpiredJwtException      nếu token đã hết hạn.
   * @throws MalformedJwtException    nếu token không đúng định dạng.
   * @throws UnsupportedJwtException  nếu token không được hỗ trợ.
   * @throws IllegalArgumentException nếu token không hợp lệ.
   */
  public boolean validateToken(String authToken) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(authToken);
      return true;
    } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
      return false;
    }
  }

  /**
   * Phương thức này dùng để trích xuất token JWT từ chuỗi yêu cầu.
   * Token được truyền trong header Authorization theo định dạng
   */
  public String resolveToken(String req) {
    String bearerToken = req;
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  /**
   * Kiểm tra định dạng của token có hợp lệ hay không.
   * Token hợp lệ phải có 3 phần được ngăn cách bởi dấu chấm ('.').
   */
  public boolean isTokenFormatValid(String token) {
    try {
      String[] tokenParts = token.split("\\.");
      return tokenParts.length == 3;
    } catch (Exception e) {
      return false;
    }
  }

  // Kiểm tra tính hợp lệ của chữ ký JWT
  public boolean isSignatureValid(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
      return false;
    }
  }

  // Lấy key ký từ token
  public Key getSigningKey(String token) {
    byte[] keyBytes = jwtConfig.getJwtSecret().getBytes();
    return Keys.hmacShaKeyFor(Base64.getEncoder().encode(keyBytes));
  }

  // Kiểm tra token có hết hạn hay không
  public boolean isTokenExpired(String token) {
    final Date expiration = getClaimFromToken(token, Claims::getExpiration);
    return expiration.after(new Date());
  }

  // Kiểm tra token có phải do issuer phát hành hay không
  public boolean isIssuerToken(String token) {
    String tokenIssuer = getClaimFromToken(token, Claims::getIssuer);
    return jwtConfig.getJwtIssuer().equals(tokenIssuer);
  }

  // Lấy tất cả claims từ token
  private Claims getAllClaimsFromToken(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey(token))
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  /**
   * Lấy một giá trị cụ thể từ token JWT bằng cách sử dụng hàm xử lý claims.
   *
   * @param <T>           Kiểu dữ liệu của giá trị cần lấy từ claims.
   * @param claimsResolve Hàm xử lý để lấy giá trị từ claims.
   * @return Giá trị cụ thể từ claims.
   */
  private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolve) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolve.apply(claims);
  }


  /**
   * Phương thức này tạo và trả về một đối tượng JwtDecoder sử dụng thuật toán mã hóa HS256.
   * 
   * @return JwtDecoder đối tượng dùng để giải mã JWT.
   */
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder
        .withSecretKey(new SecretKeySpec(
            jwtConfig.getJwtSecret().getBytes(),
            "HS256"
        ))
        .macAlgorithm(MacAlgorithm.HS256)
        .build();
  }
}
