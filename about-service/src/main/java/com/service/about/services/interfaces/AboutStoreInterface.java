package com.service.about.services.interfaces;

import java.util.List;

import com.service.about.entities.AboutStore;
import com.service.about.requests.AboutStoreRequest;

public interface AboutStoreInterface {
  AboutStore createAboutStore(AboutStoreRequest request);

  AboutStore updateAboutStore(AboutStoreRequest request);

  Boolean deleteAboutStore(Integer id);

  List<AboutStore> getAllAboutStore();
}
