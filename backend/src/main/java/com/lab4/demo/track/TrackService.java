package com.lab4.demo.track;

import com.lab4.demo.playlist.PlaylistRepository;
import com.lab4.demo.playlist.model.Playlist;
import com.lab4.demo.track.model.Track;
import com.lab4.demo.track.model.dto.TrackDTO;
import com.lab4.demo.user.UserRepository;
import com.lab4.demo.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackService {
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;

    public List<TrackDTO> findAll() {
        return trackRepository
                .findAll()
                .stream()
                .map(trackMapper::toDto)
                .collect(Collectors.toList());
    }

    public Track getTrackById(Long id) {
        return trackRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Track not found with id: " + id));
    }

    public TrackDTO create(TrackDTO trackDTO) {
        return trackMapper.toDto(trackRepository.save(
                trackMapper.fromDto(trackDTO)
        ));
    }

    public TrackDTO edit(Long id,TrackDTO trackDTO){
        Track track = getTrackById(id);
        track.setTitle(trackDTO.getTitle());
        track.setLink(trackDTO.getLink());
        track.setDuration(trackDTO.getDuration());
        track.setExplicit_lyrics(trackDTO.getExplicit_lyrics());
        track.setPreview(trackDTO.getPreview());
        track.setArtist(trackDTO.getArtist());
        track.setAlbum(trackDTO.getAlbum());
        return trackMapper.toDto(trackRepository.save(track));
    }

    public void delete(Long id) {
        List<Playlist> playlists = playlistRepository.findAll();
        for(Playlist playlist: playlists){
            List<Track> tracks = new ArrayList<>();
            for(Track track: playlist.getTracks()){
                if(!track.getId().equals(id)){
                    tracks.add(track);
                }
            }
            playlist.setTracks(tracks);
            playlistRepository.save(playlist);
        }
        List<User> users = userRepository.findAll();
        for(User user:users){
            List<Track> tracks = new ArrayList<>();
            for(Track track: user.getPurchasedTracks()){
                if(!track.getId().equals(id)){
                    tracks.add(track);
                }
            }
            user.setPurchasedTracks(new HashSet<>(tracks));
            userRepository.save(user);
        }
        trackRepository.deleteById(id);
    }

    public Optional<Track> findByTitle(String title){
        return trackRepository.findByTitle(title);
    }

}
