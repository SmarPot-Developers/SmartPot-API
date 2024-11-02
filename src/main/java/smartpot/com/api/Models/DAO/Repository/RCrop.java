package smartpot.com.api.Models.DAO.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import smartpot.com.api.Models.Entity.Crop;
import smartpot.com.api.Models.Entity.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface RCrop extends MongoRepository<Crop, String> {

    @Query("{ 'user' : ?0 }")
    List<Crop> findByUser(User user);

    @Query("{ 'type' : ?0 }")
    List<Crop> findByType(String type);

    @Query("{ 'status' : ?0 }")
    List<Crop> findByStatus(String type);

    long countByUser(User user);
}