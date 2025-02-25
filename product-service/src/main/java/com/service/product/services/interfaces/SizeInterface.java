package com.service.product.services.interfaces;

import java.util.List;

import com.service.product.entities.Size;
import com.service.product.requests.SizeRequest;

public interface SizeInterface {
  Size createSize(SizeRequest request);

  Size updateSize(SizeRequest request);

  Boolean deleteSize(Integer id);

  List<Size> getAllSize();
}
