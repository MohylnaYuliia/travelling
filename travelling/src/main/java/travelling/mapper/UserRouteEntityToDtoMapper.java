package travelling.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import travelling.dto.UserRouteDto;
import travelling.entity.UserRouteEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRouteEntityToDtoMapper {

    List<UserRouteDto> map(List<UserRouteEntity> userRouteEntityList);

    @Mappings({
            @Mapping(target = "routeName", source = "userRouteEntity.route.name"),
            @Mapping(target = "userName", source = "userRouteEntity.user.name"),
            @Mapping(target = "routeSpotsNumber", source = "userRouteEntity.route.spots"),
            @Mapping(target = "reservedSpots", source = "userRouteEntity.spotCount"),
            @Mapping(target = "userId", source = "userRouteEntity.user.id"),
            @Mapping(target = "routeId", source = "userRouteEntity.route.id")
    })
    UserRouteDto map(UserRouteEntity userRouteEntity);
}
