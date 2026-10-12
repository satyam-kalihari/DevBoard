package com.satyam.DevBoard.service;

import com.satyam.DevBoard.exception.ProvisioningException;
import com.satyam.DevBoard.model.User;
import com.satyam.DevBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.io.opentelemetry.proto.profiles.v1experimental.Link;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserProvisioningService {

    private final UserRepository userRepository;
    private final JdbcClient jdbcClient;

    private final Set<UUID> knownUsers = ConcurrentHashMap.newKeySet();

    @Transactional
    public void ensureProvisioned(Jwt jwt){

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        if (knownUsers.contains(keycloakId)) return;

        if (userRepository.existsByKeycloakId(keycloakId)){
            knownUsers.add(keycloakId);
            return;
        }

        String email = jwt.getClaimAsString("email");
        if (email == null || email.isBlank()){
            throw new ProvisioningException("Your account has no email");
        }

        Optional<User> existing = userRepository.findByEmailIgnoreCase(email);
        if (existing.isPresent()){
            link(existing.get(), keycloakId, jwt);
        }else {
            insertIfAbsent(keycloakId, displayName(jwt, email), email);
        }
        knownUsers.add(keycloakId);
    }

    private void link(User existing, UUID keycloakId, Jwt jwt){
        if (existing.getKeycloakId() != null){
            throw new ProvisioningException("This email is already linked to a different account");
        }
        if (!Boolean.TRUE.equals(jwt.getClaimAsBoolean("email_verified"))){
            throw new ProvisioningException("Verify your email address before signing in");
        }
        existing.setKeycloakId(keycloakId);
        userRepository.save(existing);
    }

    private void insertIfAbsent(UUID keycloakId, String name, String email){
        jdbcClient.sql("""
            INSERT INTO users (name, email, keycloak_id)
            VALUES (:name, :email, :keycloakId)
            ON CONFLICT DO NOTHING 
            """)
                .param("name", name)
                .param("email", email)
                .param("keycloakId", keycloakId)
                .update();
    }

    private String displayName(Jwt jwt, String email){
        String name = jwt.getClaimAsString("name");
        if (name != null && !name.isBlank()) return name;
        String username = jwt.getClaimAsString("preferred_username");
        return (username != null && !username.isBlank()) ? username : email;
    }
}
