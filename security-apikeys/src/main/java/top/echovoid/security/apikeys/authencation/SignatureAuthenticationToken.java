package top.echovoid.security.apikeys.authencation;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * @author Peng
 * @since 2025/1/20
 */
@Getter
public class SignatureAuthenticationToken implements Authentication {
    private boolean isAuthenticated;
    private final Object details;
    private final OutAppPrincipal principal;

    public SignatureAuthenticationToken(OutAppPrincipal principal, Object details) {
        this.principal = principal;
        this.details = details;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return this.details;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return this.principal.getAppid().toString();
    }
}
