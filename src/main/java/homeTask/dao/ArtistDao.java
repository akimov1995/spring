package homeTask.dao;

import homeTask.model.Artist;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ArtistDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Artist> findAll() {
        String queryString = "select a from Artist a order by a.id";
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }

    @Transactional
    public boolean addArtist(Artist artist) {
        String queryString = "insert into artists (name, label_name) values(?,?)";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter(1, artist.getName());
        query.setParameter(2, artist.getLabelName());
        int rowCount = query.executeUpdate();
        return rowCount > 0;
    }

    @Transactional
    public boolean deleteArtist(int id) {
        Artist artistToDelete = entityManager.find(Artist.class, id);

        if (artistToDelete != null) {
            entityManager.remove(artistToDelete);
            return true;
        } else {
            return false;
        }
    }


    @Transactional
    public boolean updateArtist(Artist artist) {
        String queryString = "update artists set name = ?, label_name = ? where artist_id = ?";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter(1, artist.getName());
        query.setParameter(2, artist.getLabelName());
        query.setParameter(3, artist.getId());
        int rowCount = query.executeUpdate();
        return rowCount > 0;
    }

    public Artist getArtistById(int id) {
        return entityManager.find(Artist.class, id);
    }
}
