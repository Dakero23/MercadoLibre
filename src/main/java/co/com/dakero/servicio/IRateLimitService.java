package co.com.dakero.servicio;


import io.github.bucket4j.Bucket;

public interface IRateLimitService {

	Bucket getBucket() ;
	
}
