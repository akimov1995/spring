package homeTask.controller;

import homeTask.dao.AlbumDao;
import homeTask.model.Album;
import homeTask.model.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/albumRestController")
public class AlbumController {

    @Autowired
    private AlbumDao albumDao;

    @GetMapping(value = "/albums", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Album>> listAlbums() {
        List<Album> albums = albumDao.findAll();
        return new ResponseEntity<List<Album>>(albums, HttpStatus.OK);
    }

    @PostMapping(value = "/addAlbum", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Artist> addAlbum(@RequestBody Album album) {
        albumDao.addAlbum(album);
        return new ResponseEntity(album, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/deleteAlbum", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteAlbum(@RequestParam int id) {
        if (albumDao.deleteAlbum(id)) {
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/album", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Album> getAlbumById(@RequestParam int id) {
        Album album = albumDao.getAlbumById(id);
        if (album != null) {
            return new ResponseEntity<Album>(album, HttpStatus.OK);
        } else {
            return new ResponseEntity<Album>(album, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/updateAlbum", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Album> updateAlbum(@RequestBody Album album) {
        if (albumDao.updateAlbum(album)) {
            return new ResponseEntity<Album>(album, HttpStatus.OK);
        } else {
            return new ResponseEntity<Album>(album, HttpStatus.NOT_FOUND);
        }
    }
}
