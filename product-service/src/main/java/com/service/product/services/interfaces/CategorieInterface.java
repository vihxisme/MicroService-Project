package com.service.product.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.service.product.entities.Categorie;
import com.service.product.requests.CategorieRequest;
import com.service.product.requests.PaginationRequest;
import com.service.product.responses.PaginationResponse;

public interface CategorieInterface {
  Categorie createCategorie(CategorieRequest request);

  Categorie updateCategorie(CategorieRequest request);

  Boolean deleteCategorie(UUID id);

  List<Categorie> getAllCategorie();

  PaginationResponse<Categorie> getAllCategorie(PaginationRequest request);

  Map<UUID, String> getCategorieName(List<UUID> categorieIds);
}
