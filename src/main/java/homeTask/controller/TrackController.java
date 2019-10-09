package homeTask.controller;

import homeTask.dao.TrackDao;
import homeTask.model.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/trackRestController")
public class TrackController {

    @Autowired
    private TrackDao trackDao;

    @GetMapping(value = "/tracks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Track>> listTracks() {
        List<Track> tracks = trackDao.findAll();
        return new ResponseEntity<List<Track>>(tracks, HttpStatus.OK);
    }

    @PostMapping(value = "/addTrack", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Track> addTrack(@RequestBody Track track) {
        trackDao.addTrack(track);
        return new ResponseEntity(track, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/deleteTrack", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteTrack(@RequestParam int id) {
        if (trackDao.deleteTrack(id)) {
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/track", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Track> getTrackById(@RequestParam int id) {
        Track track = trackDao.getTrackById(id);
        if (track != null) {
            return new ResponseEntity<Track>(track, HttpStatus.OK);
        } else {
            return new ResponseEntity<Track>(track, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/updateTrack", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Track> updateTrack(@RequestBody Track track) {
        if (trackDao.updateTrack(track)) {
            return new ResponseEntity<Track>(track, HttpStatus.OK);
        } else {
            return new ResponseEntity<Track>(track, HttpStatus.NOT_FOUND);
        }
    }
}
