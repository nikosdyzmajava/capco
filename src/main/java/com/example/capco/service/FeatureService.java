package com.example.capco.service;

import com.example.capco.domain.Feature;
import com.example.capco.domain.User;
import com.example.capco.exception.FeatureEnabledException;
import com.example.capco.exception.FeatureNotFoundException;
import com.example.capco.exception.UserNotFoundException;
import com.example.capco.repository.FeatureRepository;
import com.example.capco.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeatureService {

    private final FeatureRepository featureRepository;
    private final UserRepository userRepository;

    public Feature create(Feature feature) {
        return featureRepository.saveAndFlush(feature);
    }

    public List<Feature> findAll() {
        return featureRepository.findAll();
    }

    @Transactional
    public Feature enable(Long userId, Long featureId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Feature featureForUpdate = user
                .getFeatures().stream()
                .filter(feature -> Objects.equals(feature.getId(), featureId)).findFirst()
                .orElseThrow(() -> new FeatureNotFoundException(featureId));
        if (featureForUpdate.isEnabled()) throw new FeatureEnabledException(featureId);
        return featureRepository.save(
                Feature.builder()
                        .id(featureForUpdate.getId())
                        .name(featureForUpdate.getName())
                        .enabled(true)
                        .user(user)
                        .build()
        );
    }
}
