package com.service.product.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.product.entities.Color;
import com.service.product.mappers.ColorMapper;
import com.service.product.repositories.ColorRepository;
import com.service.product.requests.ColorRequest;
import com.service.product.services.interfaces.ColorInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ColorService implements ColorInterface {

  @Autowired
  private ColorRepository colorRepository;

  @Autowired
  private ColorMapper colorMapper;

  @Override
  @Transactional
  public Color createColor(ColorRequest request) {
    Color color = colorMapper.toColor(request);

    return colorRepository.save(color);
  }

  @Override
  @Transactional
  public Color updateColor(ColorRequest request) {
    Color existColor = colorRepository.findById(request.getId())
        .orElseThrow(() -> new EntityNotFoundException("Color not found"));

    colorMapper.updateColorFromRequest(request, existColor);

    return colorRepository.save(existColor);
  }

  @Override
  @Transactional
  public Boolean deleteRequest(Integer id) {
    Color existColor = colorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Color not found"));

    colorRepository.delete(existColor);

    return true;
  }

  @Override
  public List<Color> getAllColor() {
    return colorRepository.findAll();
  }

}
