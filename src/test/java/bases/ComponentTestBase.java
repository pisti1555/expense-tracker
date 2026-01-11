package bases;

import com.fasterxml.jackson.databind.ObjectMapper;
import factories.UserFactory;
import hu.projects.expense_tracker.features.users.entities.User;
import hu.projects.expense_tracker.services.auth_token_service.AuthTokenService;
import hu.projects.expense_tracker.services.error_response_provider.EnvironmentBasedErrorResponseProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest
public abstract class ComponentTestBase {
    @MockitoBean
    protected AuthTokenService tokenService;

    @MockitoBean
    protected EnvironmentBasedErrorResponseProvider errorResponseProvider;

    @Autowired
    protected MockMvc mvc;

    protected static final User TEST_USER = UserFactory.create();
    protected static final String TEST_JWT_TOKEN = "testJwtToken";

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected void mockAuthentication() {
        when(tokenService.getUsernameFromToken(eq(TEST_JWT_TOKEN))).thenReturn(TEST_USER.getUsername());
        when(tokenService.validateToken(eq(TEST_JWT_TOKEN), eq(TEST_USER))).thenReturn(true);
    }

    protected RequestPostProcessor authorization() {
        return request -> {
            csrf().postProcessRequest(request);
            user(TEST_USER).postProcessRequest(request);
            request.addHeader("Authorization", "Bearer " + TEST_JWT_TOKEN);
            return request;
        };
    }

    protected String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}
