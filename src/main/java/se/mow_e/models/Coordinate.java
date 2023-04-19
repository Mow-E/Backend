package se.mow_e.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.*;

@Entity
@Table(name = "coordinate_table")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // By default, enabled, but I don't trust them xD
    private Long id;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "mower_id")
    private String mowerId;

    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;

    @Column(name = "time")
    private Long time;

    @Column(name = "state")
    private Integer state;

    @Column(name = "collision_id")
    private Integer collisionId;

    @Column(name = "extra")
    private String extra;

    public Coordinate(Long sessionId, String mowerId, Double x, Double y, Long time, Integer state, Integer collisionId, String extra) {
        this.sessionId = sessionId;
        this.mowerId = mowerId;
        this.x = x;
        this.y = y;
        this.time = time;
        this.state = state;
        this.collisionId = collisionId;
        this.extra = extra;
    }

    public Coordinate() {
        // Dummy constructor for unknown spring mind games
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
}
