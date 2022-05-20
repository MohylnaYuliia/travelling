package travelling.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRouteDto {

    private Integer userId;

    private String userName;

    private String routeName;

    private Integer routeId;

    private Integer routeSpotsNumber;

    private Integer reservedSpots;
}
