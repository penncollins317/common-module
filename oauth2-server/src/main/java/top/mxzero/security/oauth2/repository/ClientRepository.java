package top.mxzero.security.oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.mxzero.security.oauth2.entity.OAuth2Client;

import java.util.Optional;

/**
 * @author Peng
 * @since 2025/3/31
 */
@Repository
public interface ClientRepository extends JpaRepository<OAuth2Client, String> {
    Optional<OAuth2Client> findByClientId(String clientId);
}