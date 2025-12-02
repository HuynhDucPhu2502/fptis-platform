package fpt.is.bnk.fptis_platform.mapper;

import fpt.is.bnk.fptis_platform.dto.identity.RemoteUser;
import fpt.is.bnk.fptis_platform.entity.Profile;
import fpt.is.bnk.fptis_platform.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Admin 12/2/2025
 *
 **/
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "profile.firstName")
    @Mapping(target = "lastName", source = "profile.lastName")
    @Mapping(target = "dob", expression = "java(profile.getDob() != null ? profile.getDob().toString() : null)")
    @Mapping(target = "active", source = "user.active")
    RemoteUser toRemoteUser(User user, Profile profile);
}
