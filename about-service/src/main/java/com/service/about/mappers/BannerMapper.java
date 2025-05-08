package com.service.about.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.about.entities.Banner;
import com.service.about.requests.BannerRequest;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BannerMapper {

    Banner toBanner(BannerRequest request);

    void updateBannerFromDto(Banner banner, @MappingTarget BannerRequest dto);
}
