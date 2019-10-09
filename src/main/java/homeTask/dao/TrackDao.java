package homeTask.dao;

import homeTask.model.Track;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class TrackDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Track> findAll() {
        String queryString = "select t from Track t order by t.id";
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }

    @Transactional
    public boolean addTrack(Track track) {
        String queryString = "insert into tracks (name, year, album_id) values(?,?,?)";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter(1, track.getName());
        query.setParameter(2, track.getYear());
        query.setParameter(3, track.getAlbum().getId());
        int rowCount = query.executeUpdate();
        return rowCount > 0;
    }

    @Transactional
    public boolean deleteTrack(int id) {
        Track track = entityManager.find(Track.class, id);

        if (track != null) {
            entityManager.remove(track);
            return true;
        } else {
            return false;
        }
    }


    @Transactional
    public boolean updateTrack(Track track) {
        String queryString = "update tracks set name = ?, year = ?, album_id = ? where track_id = ?";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter(1, track.getName());
        query.setParameter(2, track.getYear());
        query.setParameter(3, track.getAlbum().getId());
        query.setParameter(4, track.getId());
        int rowCount = query.executeUpdate();
        return rowCount > 0;
    }

    public Track getTrackById(int id) {
        return entityManager.find(Track.class, id);
    }
}
