package com.service.product.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.product.entities.Size;
import com.service.product.mappers.SizeMapper;
import com.service.product.repositories.SizeRepository;
import com.service.product.requests.SizeRequest;
import com.service.product.services.interfaces.SizeInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class SizeService implements SizeInterface {

  @Autowired
  private SizeRepository sizeRepository;

  @Autowired
  private SizeMapper sizeMapper;

  @Override
  @Transactional
  public Size createSize(SizeRequest request) {
    Size size = sizeMapper.toSize(request);

    return sizeRepository.save(size);
  }

  @Override
  @Transactional
  public Size updateSize(SizeRequest request) {
    Size existSize = sizeRepository.findById(request.getId())
        .orElseThrow(() -> new EntityNotFoundException("Size not found"));

    sizeMapper.updateSizeToRequest(request, existSize);

    return sizeRepository.save(existSize);
  }

  @Override
  @Transactional
  public Boolean deleteSize(Integer id) {
    Size existSize = sizeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Size not found"));

    sizeRepository.delete(existSize);

    return true;
  }

  @Override
  public List<Size> getAllSize() {
    return sizeRepository.findAll();
  }

}
