import com.fasterxml.jackson.databind.ObjectMapper;
import homeTask.controller.AlbumController;
import homeTask.controller.ArtistController;
import homeTask.controller.TrackController;
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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml",
        "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
@ActiveProfiles("local")
public class SpringTest2 {
    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Mock
    ArtistDao artistDaoMock;

    @Mock
    AlbumDao albumDaoMock;

    @Mock
    TrackDao trackDaoMock;

    @InjectMocks
    ArtistController artistController;

    @InjectMocks
    AlbumController albumController;

    @InjectMocks
    TrackController trackController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testArtistControllerGetMethods() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();


        Artist a1 = new Artist();
        a1.setId(1);
        a1.setLabelName("label1");
        a1.setName("artist");

        Artist a2 = new Artist();
        a2.setId(2);
        a2.setLabelName("label2");
        a2.setName("kendrick");

        List<Artist> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);

        when(artistDaoMock.findAll()).thenReturn(list);

        mockMvc.perform(get("/artistRestController/artists").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("artist"))
                .andExpect(jsonPath("$[0].labelName").value("label1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("kendrick"))
                .andExpect(jsonPath("$[1].labelName").value("label2"));


        int id = 1;

        when(artistDaoMock.getArtistById(id)).thenReturn(a1);

        mockMvc.perform(get("/artistRestController/artist")
                .accept(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("artist"));

    }

    @Test
    public void testAlbumControllerGetMethods() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(albumController).build();

        Artist a1 = new Artist();
        a1.setId(1);
        a1.setName("artist");
        a1.setLabelName("label1");

        Album album = new Album();
        album.setId(1);
        album.setName("name");
        album.setGenre("hip-hop");
        album.setArtist(a1);

        List<Album> albums = new ArrayList<>();
        albums.add(album);

        when(albumDaoMock.findAll()).thenReturn(albums);

        mockMvc.perform(get("/albumRestController/albums").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].genre").value("hip-hop"))
                .andExpect(jsonPath("$[0].artist.id").value(1))
                .andExpect(jsonPath("$[0].artist.name").value("artist"))
                .andExpect(jsonPath("$[0].artist.labelName").value("label1"));


        int id = 1;

        when(albumDaoMock.getAlbumById(id)).thenReturn(album);

        mockMvc.perform(get("/albumRestController/album")
                .accept(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.genre").value("hip-hop"))
                .andExpect(jsonPath("$.artist.id").value(1))
                .andExpect(jsonPath("$.artist.name").value("artist"))
                .andExpect(jsonPath("$.artist.labelName").value("label1"));

    }

    @Test
    public void testTrackControllerGetMethods() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(trackController).build();

        Artist a1 = new Artist();
        a1.setId(1);
        a1.setName("artist");
        a1.setLabelName("label1");

        Album album = new Album();
        album.setId(1);
        album.setName("name");
        album.setGenre("hip-hop");
        album.setArtist(a1);

        Track track = new Track();
        track.setId(1);
        track.setName("track");
        track.setYear(2008);
        track.setAlbum(album);

        List<Track> tracks = new ArrayList<>();
        tracks.add(track);

        when(trackDaoMock.findAll()).thenReturn(tracks);

        mockMvc.perform(get("/trackRestController/tracks").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("track"))
                .andExpect(jsonPath("$[0].year").value(2008))
                .andExpect(jsonPath("$[0].album.id").value(1))
                .andExpect(jsonPath("$[0].album.name").value("name"))
                .andExpect(jsonPath("$[0].album.genre").value("hip-hop"))
                .andExpect(jsonPath("$[0].album.artist.id").value(1))
                .andExpect(jsonPath("$[0].album.artist.name").value("artist"))
                .andExpect(jsonPath("$[0].album.artist.labelName").value("label1"));


        int id = 1;

        when(trackDaoMock.getTrackById(id)).thenReturn(track);

        mockMvc.perform(get("/trackRestController/track")
                .accept(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("track"))
                .andExpect(jsonPath("$.year").value(2008))
                .andExpect(jsonPath("$.album.id").value(1))
                .andExpect(jsonPath("$.album.name").value("name"))
                .andExpect(jsonPath("$.album.genre").value("hip-hop"))
                .andExpect(jsonPath("$.album.artist.id").value(1))
                .andExpect(jsonPath("$.album.artist.name").value("artist"))
                .andExpect(jsonPath("$.album.artist.labelName").value("label1"));
    }
}