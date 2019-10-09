import com.fasterxml.jackson.databind.ObjectMapper;
import homeTask.controller.ArtistController;
import homeTask.dao.AlbumDao;
import homeTask.dao.ArtistDao;

import static org.junit.Assert.*;

import homeTask.dao.TrackDao;
import homeTask.model.Album;
import homeTask.model.Artist;
import homeTask.model.Track;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sun.nio.cs.Surrogate.is;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"file:web/WEB-INF/applicationContext.xml",
        "file:web/WEB-INF/dispatcher-servlet.xml"})
@ActiveProfiles("local")
public class SpringTest {
    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private ArtistDao artistDao;

    @Autowired
    private AlbumDao albumDao;

    @Autowired
    private TrackDao trackDao;

    /*
    @Mock
    ArtistDao artistDao;

    @InjectMocks
    ArtistController artistController;
    */

    @Before
    public void init() {
        //mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testArtistController() throws Exception {
        mockMvc.perform(get("/artistRestController/artists").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Eminem"))
                .andExpect(jsonPath("$[0].labelName").value("Shady Records"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Linkin Park"))
                .andExpect(jsonPath("$[1].labelName").value("Warner Bros."))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").value("Tech N9ne"))
                .andExpect(jsonPath("$[2].labelName").value("Strange Music"));


        Artist artistToCreate = new Artist();
        artistToCreate.setName("new Artist");
        mockMvc.perform(post("/artistRestController/addArtist").accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(artistToCreate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$.name").value("new Artist"));

        int id = artistDao.findAll().get(artistDao.findAll().size() - 1).getId();
        artistToCreate.setId(id);
        mockMvc.perform(get("/artistRestController/artist")
                .accept(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("new Artist"));


        artistToCreate.setName("updated name");
        mockMvc.perform(put("/artistRestController/updateArtist").accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(artistToCreate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("updated name"));

        mockMvc.perform(delete("/artistRestController/deleteArtist")
                .accept(MediaType.APPLICATION_JSON).param("id", String.valueOf(id)))
                .andExpect(status().isAccepted());
    }


    /*@Test
    public void findAllArtist() throws Exception {
        MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();


        Artist a1 = new Artist();
        a1.setId(1);
        a1.setName("artist");

        Artist a2 = new Artist();
        a2.setId(2);
        a2.setName("kendrick");

        List<Artist> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);

        when(artistDao.findAll()).thenReturn(list);

        mockMvc.perform(get("/artistRestController/artists").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("artist"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("kendrick"));
    }
*/

    @Test
    public void testAlbumController() throws Exception {
        mockMvc.perform(get("/albumRestController/albums").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Kamikaze"))
                .andExpect(jsonPath("$[0].genre").value("rap"))
                .andExpect(jsonPath("$[0].artist.id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("MMLP"))
                .andExpect(jsonPath("$[1].genre").value("rap"))
                .andExpect(jsonPath("$[1].artist.id").value(1))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").value("Meteora"))
                .andExpect(jsonPath("$[2].genre").value("rock"))
                .andExpect(jsonPath("$[2].artist.id").value(2));

        mockMvc.perform(get("/albumRestController/album")
                .accept(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Kamikaze"))
                .andExpect(jsonPath("$.genre").value("rap"))
                .andExpect(jsonPath("$.artist.id").value(1));

        Artist artist = artistDao.getArtistById(1);
        Album album = new Album();
        album.setName("new Album");
        album.setGenre("rock");
        album.setArtist(artist);

        mockMvc.perform(post("/albumRestController/addAlbum").accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(album))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$.name").value("new Album"))
                .andExpect(jsonPath("$.genre").value("rock"))
                .andExpect(jsonPath("$.artist.id").value(artist.getId()));

        int id = albumDao.findAll().get(albumDao.findAll().size() - 1).getId();
        album.setId(id);
        mockMvc.perform(get("/albumRestController/album")
                .accept(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("new Album"))
                .andExpect(jsonPath("$.genre").value("rock"))
                .andExpect(jsonPath("$.artist.id").value(artist.getId()));


        album.setName("updated name");
        mockMvc.perform(put("/albumRestController/updateAlbum").accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(album))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("updated name"));

        mockMvc.perform(delete("/albumRestController/deleteAlbum")
                .accept(MediaType.APPLICATION_JSON).param("id", String.valueOf(id)))
                .andExpect(status().isAccepted());
    }


    @Test
    public void testTrackController() throws Exception {
        mockMvc.perform(get("/trackRestController/tracks").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("The Ringer"))
                .andExpect(jsonPath("$[0].year").value(2018))
                .andExpect(jsonPath("$[0].album.id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Numb"))
                .andExpect(jsonPath("$[1].year").value(2004))
                .andExpect(jsonPath("$[1].album.id").value(3))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").value("faint"))
                .andExpect(jsonPath("$[2].year").value(2004))
                .andExpect(jsonPath("$[2].album.id").value(3));

        mockMvc.perform(get("/trackRestController/track")
                .accept(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("The Ringer"))
                .andExpect(jsonPath("$.year").value(2018))
                .andExpect(jsonPath("$.album.id").value(1));

        Album album = albumDao.getAlbumById(1);
        Track track = new Track();
        track.setName("new Track");
        track.setYear(2019);
        track.setAlbum(album);

        mockMvc.perform(post("/trackRestController/addTrack").accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(track))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$.name").value("new Track"))
                .andExpect(jsonPath("$.year").value(2019))
                .andExpect(jsonPath("$.album.id").value(album.getId()));

        int id = trackDao.findAll().get(trackDao.findAll().size() - 1).getId();
        track.setId(id);
        mockMvc.perform(get("/trackRestController/track")
                .accept(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("new Track"))
                .andExpect(jsonPath("$.year").value(2019))
                .andExpect(jsonPath("$.album.id").value(album.getId()));


        track.setName("updated name");
        mockMvc.perform(put("/trackRestController/updateTrack").accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(track))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("updated name"));

        mockMvc.perform(delete("/trackRestController/deleteTrack")
                .accept(MediaType.APPLICATION_JSON).param("id", String.valueOf(id)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testArtistDao() {
        Artist artist = new Artist();
        artist.setName("new artist");
        artist.setLabelName("new label");
        artistDao.addArtist(artist);

        int id = artistDao.findAll().get(artistDao.findAll().size() - 1).getId();
        artist.setId(id);

        assertEquals("new artist", artistDao.getArtistById(id).getName());
        assertEquals("new label", artistDao.getArtistById(id).getLabelName());

        artist.setName("artist");
        artistDao.updateArtist(artist);
        assertEquals("artist", artistDao.getArtistById(id).getName());

        artistDao.deleteArtist(id);
        assertNull(artistDao.getArtistById(id));
    }

    @Test
    public void testAlbumDao() {
        Artist artist = artistDao.getArtistById(1);
        Album album = new Album();
        album.setName("new album");
        album.setGenre("rock");
        album.setArtist(artist);

        albumDao.addAlbum(album);

        int id = albumDao.findAll().get(albumDao.findAll().size() - 1).getId();
        album.setId(id);

        assertEquals("new album", albumDao.getAlbumById(id).getName());
        assertEquals("rock", albumDao.getAlbumById(id).getGenre());
        int actual = albumDao.getAlbumById(id).getArtist().getId();
        assertEquals(1, actual);

        album.setName("updated");
        albumDao.updateAlbum(album);
        assertEquals("updated", albumDao.getAlbumById(id).getName());

        albumDao.deleteAlbum(id);
        assertNull(albumDao.getAlbumById(id));
    }


    @Test
    public void testTrackDao() {
        Album album = albumDao.getAlbumById(1);
        Track track = new Track();
        track.setName("new track");
        track.setYear(2019);
        track.setAlbum(album);

        trackDao.addTrack(track);

        int id = trackDao.findAll().get(trackDao.findAll().size() - 1).getId();
        track.setId(id);

        assertEquals("new track", trackDao.getTrackById(id).getName());
        assertEquals(2019, trackDao.getTrackById(id).getYear());

        int actual = trackDao.getTrackById(id).getAlbum().getId();
        assertEquals(1, actual);

        track.setName("updated");
        trackDao.updateTrack(track);
        assertEquals("updated", trackDao.getTrackById(id).getName());

        trackDao.deleteTrack(id);
        assertNull(trackDao.getTrackById(id));
    }
}
