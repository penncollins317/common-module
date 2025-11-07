package top.echovoid.security.apikeys.authencation;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

/**
 * @author Peng
 * @since 2025/1/20
 */
public class SignatureAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(SignatureAuthenticationToken.class, authentication, () -> "Only SignatureAuthenticationToken is supported.");
        SignatureAuthenticationToken authenticationToken = (SignatureAuthenticationToken) authentication;


        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SignatureAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
