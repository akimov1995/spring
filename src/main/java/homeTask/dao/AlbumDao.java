package homeTask.dao;

import homeTask.model.Album;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AlbumDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Album> findAll() {
        String queryString = "select a from Album a order by a.name";
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }

    @Transactional
    public boolean addAlbum(Album album) {
        String queryString = "insert into albums (genre, name, artist_id) values(?,?,?)";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter(1, album.getGenre());
        query.setParameter(2, album.getName());
        query.setParameter(3, album.getArtist().getId());
        int rowCount = query.executeUpdate();
        return rowCount > 0;
    }

    @Transactional
    public boolean deleteAlbum(int id) {
        Album albumToDelete = entityManager.find(Album.class, id);

        if (albumToDelete != null) {
            entityManager.remove(albumToDelete);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean updateAlbum(Album album) {
        String queryString = "update albums set name = ?, genre = ?, artist_id = ? where album_id = ?";
        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter(1, album.getName());
        query.setParameter(2, album.getGenre());
        query.setParameter(3, album.getArtist().getId());
        query.setParameter(4, album.getId());
        int rowCount = query.executeUpdate();
        return rowCount > 0;
    }

    public Album getAlbumById(int id) {
        return entityManager.find(Album.class, id);
    }
}
