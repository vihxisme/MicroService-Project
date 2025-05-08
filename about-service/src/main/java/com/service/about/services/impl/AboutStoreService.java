package com.service.about.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.about.entities.AboutStore;
import com.service.about.mappers.AboutStoreMapper;
import com.service.about.repositories.AboutStoreRepository;
import com.service.about.requests.AboutStoreRequest;
import com.service.about.services.interfaces.AboutStoreInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AboutStoreService implements AboutStoreInterface {

    @Autowired
    private AboutStoreRepository aboutStoreRepository;

    @Autowired
    private AboutStoreMapper aboutStoreMapper;

    private Logger logger = LoggerFactory.getLogger(AboutStoreService.class);

    @Override
    @Transactional
    public AboutStore createAboutStore(AboutStoreRequest request) {
        AboutStore aboutStore = aboutStoreMapper.toAboutStore(request);

        return aboutStoreRepository.save(aboutStore);
    }

    @Override
    @Transactional
    public AboutStore updateAboutStore(AboutStoreRequest request) {
        AboutStore existAboutStore = aboutStoreRepository.findById(request.getId()).orElseThrow(() -> new EntityNotFoundException("AboutStore not found"));

        aboutStoreMapper.updateAboutStoreFromDto(existAboutStore, request);

        return aboutStoreRepository.save(existAboutStore);
    }

    @Override
    @Transactional
    public Boolean deleteAboutStore(Integer id) {
        AboutStore existAboutStore = aboutStoreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("AboutStore not found"));

        aboutStoreRepository.delete(existAboutStore);

        return true;
    }

    @Override
    public List<AboutStore> getAllAboutStore() {
        return aboutStoreRepository.findAll();
    }
}
