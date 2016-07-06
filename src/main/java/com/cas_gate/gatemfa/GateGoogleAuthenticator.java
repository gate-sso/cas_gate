package com.cas_gate.gatemfa;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.authentication.*;
import org.jasig.cas.authentication.principal.DefaultPrincipalFactory;
import org.jasig.cas.authentication.principal.PrincipalFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;
import javax.naming.ConfigurationException;

/**
 * Created by ajey on 18/05/16.
 */

@Component("gateGoogleAuthenticator")
public class GateGoogleAuthenticator implements AuthenticationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GateGoogleAuthenticator.class);

    /**
     * Factory to create the principal type.
     **/
    @NotNull
    @Autowired
    @Qualifier("principalFactory")
    protected PrincipalFactory principalFactory = new DefaultPrincipalFactory();

    public GateGoogleAuthenticator() { }

    @PostConstruct
    private void init() {
        LOGGER.warn("{} Authenticator initialized.", this.getClass().getName());
    }

    @Override
    public HandlerResult authenticate(Credential credential) throws GeneralSecurityException, PreventedException {
        LOGGER.debug("::GateGoogleAuthenticator:authenticate");

        final UsernamePasswordCredential upCredentials = (UsernamePasswordCredential) credential;
        final String username = upCredentials.getUsername();
        final String password = upCredentials.getPassword();


        if (StringUtils.isBlank(username)) {
            LOGGER.debug("* GateGoogle was provided empty username!" );
            throw new AccountNotFoundException("username can not be blank.");
        }
        if (StringUtils.isBlank(password)) {
            LOGGER.debug("* GateGoogle was provided empty password for [{}]", username);
            throw new FailedLoginException("password can not be blank.");
        }

        try {
          if (authenticateWithGateMFA(username, password)) {
            LOGGER.debug("* GateGoogle succeeded to authenticate [{}]", username);
            return new DefaultHandlerResult(this, new BasicCredentialMetaData(credential), this.principalFactory.createPrincipal(username));
          }
        } catch (Exception e) {
            LOGGER.error("::GateGoogleAuthenticator failed for GateGoogleAuthenticateURI not found.");
            e.printStackTrace();
        }


        LOGGER.error("* GateGoogle failed to authenticate [{}]", username);
        throw new FailedLoginException("Username or password not valid");
    }

    @Override
    public boolean supports(Credential credential) {
        final UsernamePasswordCredential upCredentials = (UsernamePasswordCredential) credential;
        LOGGER.debug("::GateGoogleAuthenticator:supports for [{}]", upCredentials.getUsername());

        return credential instanceof UsernamePasswordCredential;
    }

    @Override
    public String getName() {
        LOGGER.debug("::GateGoogleAuthenticator:getName");
        return getClass().getSimpleName();
    }

    public boolean authenticateWithGateMFA(String username, String password) throws ConfigurationException {
      /*
       * GateGoogleAuthenticateURI = https://<gate server url>/profile/authenticate
       */
        String gateGoogleAuthenticateURI = System.getenv("GateGoogleAuthenticateURI");
        if (StringUtils.isBlank(gateGoogleAuthenticateURI)) {
          throw new ConfigurationException("No environment variable GateGoogleAuthenticateURI found.");
        }

        LOGGER.debug("::GateGoogleAuthenticator:authenticateWithGateMFA");
        String url = String.format("%s?email=%s&token=%s", gateGoogleAuthenticateURI, username, password);
        HttpURLConnection httpURLConnection = new HttpURLConnection();
        String response = httpURLConnection.sendGet(url);
        int result = Integer.parseInt(response);
        return (result == 0);
    }
}
