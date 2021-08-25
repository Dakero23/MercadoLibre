package co.com.dakero.serviciomimpl;

import java.time.Duration;

import org.springframework.stereotype.Service;

import co.com.dakero.servicio.IRateLimitService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@Service
public class RateLimitService implements IRateLimitService {

	private final Bucket bucket;

	public RateLimitService() {
//		long capacity = 100000;
		long capacity = 20;
		Refill refill = Refill.greedy(10, Duration.ofMinutes(1));
		Bandwidth limit = Bandwidth.classic(capacity, refill);
		this.bucket = Bucket4j.builder().addLimit(limit).build();
	}

	@Override
	public Bucket getBucket() {
		return bucket;
	}
}
