package travelling.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteEntity {

    @Id
    @Column(name = "route_id")
    private Integer id;

    private String name;

    private Integer spots;
}
