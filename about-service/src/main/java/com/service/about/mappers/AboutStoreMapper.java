package com.service.about.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.about.entities.AboutStore;
import com.service.about.requests.AboutStoreRequest;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AboutStoreMapper {

    AboutStore toAboutStore(AboutStoreRequest request);

    void updateAboutStoreFromDto(@MappingTarget AboutStore aboutStore, AboutStoreRequest dto);
}
