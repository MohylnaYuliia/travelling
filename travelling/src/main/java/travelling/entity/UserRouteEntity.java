package travelling.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Version;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRouteEntity {

    @EmbeddedId
    private UserRouteId id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}) //check his type
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("routeId")
    @JoinColumn(name = "route_id")
    private RouteEntity route;

    private int spotCount;

    @Version
    private long version;
}
