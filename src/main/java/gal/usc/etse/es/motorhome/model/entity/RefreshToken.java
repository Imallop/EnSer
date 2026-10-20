package gal.usc.etse.es.motorhome.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@SuppressWarnings("unused")
@RedisHash()
@Getter
@Setter
@Accessors(chain = true)
public class RefreshToken {
    @Id
    private String token;
    @Indexed
    private String user;
    @TimeToLive
    private long ttl;

    public RefreshToken() {
    }

    public RefreshToken(String token, String user, long ttl) {
        this.token = token;
        this.user = user;
        this.ttl = ttl;
    }
}
