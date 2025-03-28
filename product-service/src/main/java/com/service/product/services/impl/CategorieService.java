package com.service.product.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.service.product.entities.Categorie;
import com.service.product.mappers.CategorieMapper;
import com.service.product.repositories.CategorieRepository;
import com.service.product.requests.CategorieRequest;
import com.service.product.requests.PaginationRequest;
import com.service.product.responses.PaginationResponse;
import com.service.product.services.interfaces.CategorieInterface;

import jakarta.transaction.Transactional;

@Service
public class CategorieService implements CategorieInterface {

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private CategorieMapper categorieMapper;

    @Override
    @Transactional
    public Categorie createCategorie(CategorieRequest request) {
        if (request.getCategorieCode() == null) {
            request.setCategorieCode(generateCategorieCode());
        }

        Categorie categorie = categorieMapper.toCategorie(request);

        return categorieRepository.save(categorie);
    }

    @Override
    @Transactional
    public Categorie updateCategorie(CategorieRequest request) {
        Categorie existCategorie = categorieRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Categorie not found"));

        categorieMapper.updateCategorieFromRequest(request, existCategorie);

        return categorieRepository.save(existCategorie);
    }

    @Override
    @Transactional
    public Boolean deleteCategorie(UUID id) {
        Categorie existCategorie = categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categorie not found"));

        categorieRepository.delete(existCategorie);

        return true;
    }

    @Override
    public List<Categorie> getAllCategorie() {
        return categorieRepository.findAll();
    }

    private String generateCategorieCode() {
        Boolean isUnique;
        Random random = new Random();
        String code;
        do {
            int randomNumberStart = random.nextInt(100, 999);
            int randomNumberMiddle = random.nextInt(1000, 9999);
            int randomNumberEnd = random.nextInt(1000, 9999);
            code = String.format("%03d-%04d-%04d", randomNumberStart, randomNumberMiddle, randomNumberEnd);
            isUnique = !categorieRepository.existsByCategorieCode(code);
        } while (!isUnique);

        return code;
    }

    @Override
    public PaginationResponse<Categorie> getAllCategorie(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<Categorie> categories = categorieRepository.findAll(pageable);

        return PaginationResponse.<Categorie>builder()
                .content(categories.getContent())
                .pageNumber(request.getPage())
                .pageSize(request.getSize())
                .totalPages(categories.getTotalPages())
                .totalElements(categories.getTotalElements())
                .build();
    }

    @Override
    public Map<UUID, String> getCategorieName(List<UUID> categorieIds) {
        return categorieRepository.findAllById(categorieIds)
                .stream()
                .collect(Collectors.toMap(Categorie::getId, Categorie::getName));
    }
}
