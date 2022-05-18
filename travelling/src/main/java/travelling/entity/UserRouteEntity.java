package travelling.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRouteEntity {

    @EmbeddedId
    private UserRouteId id;

    @ManyToOne(cascade = CascadeType.ALL) //check his type
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("routeId")
    @JoinColumn(name = "route_id")
    private RouteEntity route;

    private int spotCount;

}
