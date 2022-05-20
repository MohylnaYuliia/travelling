package travelling.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import travelling.dto.UserRouteDto;
import travelling.entity.UserRouteEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRouteEntityToDtoMapper {

    @Mappings({
            @Mapping(target="routeName", source="route.name"),
            @Mapping(target="userName", source="user.name"),
            @Mapping(target="reservedSpots", source="spotCount"),
            @Mapping(target="routeSpotsNumber", source="route.spots"),
    })
    List<UserRouteDto> map(List<UserRouteEntity> bookEntity);
}
