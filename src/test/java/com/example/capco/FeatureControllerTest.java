package com.example.capco;

import com.example.capco.command.FeatureCommand;
import com.example.capco.domain.Feature;
import com.example.capco.domain.User;
import com.example.capco.exception.FeatureEnabledException;
import com.example.capco.exception.FeatureNotFoundException;
import com.example.capco.exception.UserNotFoundException;
import com.example.capco.repository.FeatureRepository;
import com.example.capco.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("integration-tests")
public class FeatureControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeatureRepository featureRepository;


    @Test
    public void shouldAddFeatureTest() throws Exception {
        FeatureCommand featureCommand = new FeatureCommand("testName");

        this.mockMvc.perform(post("/features")
                        .content(objectMapper.writeValueAsString(featureCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    public void shouldMakeFeatureEnabledTest() throws Exception {
        User user = new User("userName", "login", "password", "ADMIN", false, null);
        Feature feature = new Feature("testName", false, user);
        user.setFeatures(Set.of(feature));

        User savedUser = userRepository.saveAndFlush(user);
        Feature savedFeature = featureRepository.saveAndFlush(feature);

        this.mockMvc.perform(put("/features/{featureId}/user/{userId}", savedFeature.getId(), savedUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() throws Exception {
        Feature feature = new Feature("testName", false, null);

        Feature savedFeature = featureRepository.saveAndFlush(feature);

        this.mockMvc.perform(put("/features/{featureId}/user/{userId}", savedFeature.getId(), 123)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));
    }

    @Test
    public void shouldThrowExceptionWhenFeatureNotFound() throws Exception {
        User user = new User("userName", "login", "password", "ADMIN", false, null);

        User savedUser = userRepository.saveAndFlush(user);

        this.mockMvc.perform(put("/features/{featureId}/user/{userId}", 123, savedUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FeatureNotFoundException));
    }

    @Test
    public void shouldThrowExceptionWhenUserWasAlreadyEnabled() throws Exception {
        User user = new User("userName", "login", "password", "ADMIN", false, null);
        Feature feature = new Feature("testName", true, user);
        user.setFeatures(Set.of(feature));

        User savedUser = userRepository.saveAndFlush(user);
        Feature savedFeature = featureRepository.saveAndFlush(feature);

        this.mockMvc.perform(put("/features/{featureId}/user/{userId}", savedFeature.getId(), savedUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FeatureEnabledException));
    }


    @Test
    public void shouldGetAllFeaturesTest() throws Exception {
        Feature feature = new Feature("testName", false, null);
        featureRepository.saveAndFlush(feature);

        this.mockMvc.perform(get("/features").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldNotGetAnyFeaturesTest() throws Exception {
        this.mockMvc.perform(get("/features").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

}
