package com.service.product.mappers;

import java.util.UUID;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.product.entities.Categorie;
import com.service.product.entities.Color;
import com.service.product.entities.Product;
import com.service.product.entities.Size;
import com.service.product.enums.StatusEnum;
import com.service.product.repositories.CategorieRepository;
import com.service.product.repositories.ColorRepository;
import com.service.product.repositories.ProductRepository;
import com.service.product.repositories.SizeRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class Convert {
  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategorieRepository categorieRepository;

  @Autowired
  private ColorRepository colorRepository;

  @Autowired
  private SizeRepository sizeRepository;

  @Named("UUIDToProduct")
  public Product UUIDToProduct(UUID productId) {
    return productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
  }

  @Named("idToColor")
  public Color idToColor(Integer id) {
    return colorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Color not found"));
  }

  @Named("idToSize")
  public Size idToSize(Integer id) {
    return sizeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Size not found"));
  }

  @Named("toCategorie")
  public Categorie toCategorie(UUID id) {
    return categorieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categorie not found"));
  }

  @Named("fromString")
  public StatusEnum fromString(String status) {
    return StatusEnum.valueOf(status);
  }

  @Named("toStringFromStatusEnum")
  public String toStringFromStatusEnum(StatusEnum status) {
    return String.valueOf(status);
  }

  @Named("categorieToUUID")
  public UUID categorieToUUID(Categorie categorie) {
    return categorie.getId();
  }
}
