package com.example.capco.controller;

import com.example.capco.command.FeatureCommand;
import com.example.capco.domain.Feature;
import com.example.capco.dto.FeatureDto;
import com.example.capco.service.FeatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/features")
@RequiredArgsConstructor
public class FeatureController {

    private final FeatureService featureService;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<FeatureDto> add(@RequestBody FeatureCommand featureCommand) {
        return new ResponseEntity<>(modelMapper.map(featureService.create(modelMapper.map(featureCommand, Feature.class)), FeatureDto.class),
                HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{featureId}/user/{userId}")
    public ResponseEntity<FeatureDto> enable(@PathVariable("featureId") Long featureId, @PathVariable("userId") Long userId) {
        log.error(featureId + " " + userId);
        FeatureDto map = modelMapper.map(featureService.enable(userId, featureId), FeatureDto.class);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<FeatureDto>> findAll() {
        return new ResponseEntity<>(featureService.findAll().stream()
                .map(feature -> modelMapper.map(feature, FeatureDto.class)).collect(Collectors.toList()), HttpStatus.OK);
    }
}
