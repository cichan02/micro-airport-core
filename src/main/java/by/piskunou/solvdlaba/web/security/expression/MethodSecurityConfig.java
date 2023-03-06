package by.piskunou.solvdlaba.web.security.expression;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@AllArgsConstructor
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private ApplicationContext applicationContext;

    @Bean
    public AuthenticationTrustResolver trustResolver() {
        return new AuthenticationTrustResolverImpl();
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        var expressionHandler = new CustomMethodSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        expressionHandler.setTrustResolver(trustResolver());
        return expressionHandler;
    }

    @Override
    protected AccessDecisionManager accessDecisionManager() {

        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();

        var expressionAdvice= new ExpressionBasedPreInvocationAdvice();
        expressionAdvice.setExpressionHandler(getExpressionHandler());

        decisionVoters.add(new PreInvocationAuthorizationAdviceVoter(expressionAdvice));
        decisionVoters.add(new AuthenticatedVoter());

        return new AffirmativeBased(decisionVoters);
    }

}
