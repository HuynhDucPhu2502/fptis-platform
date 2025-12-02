package fpt.is.bnk.fptis_platform.mapper;

import fpt.is.bnk.fptis_platform.dto.request.authentication.RegistrationRequest;
import fpt.is.bnk.fptis_platform.dto.response.authentication.ProfileResponse;
import fpt.is.bnk.fptis_platform.entity.Profile;
import org.mapstruct.Mapper;

/**
 * Admin 11/25/2025
 *
 **/
@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileResponse toProfileResponse(Profile profile);

}
