package com.service.product.services.interfaces;

import java.util.List;

import com.service.product.entities.Color;
import com.service.product.requests.ColorRequest;

public interface ColorInterface {
  Color createColor(ColorRequest request);

  Color updateColor(ColorRequest request);

  Boolean deleteRequest(Integer id);

  List<Color> getAllColor();
}
