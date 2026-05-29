package com.lab4.demo.playlist;

import com.lab4.demo.playlist.model.Playlist;
import com.lab4.demo.playlist.model.dto.PlaylistDTO;
import com.lab4.demo.track.TrackMapper;
import com.lab4.demo.track.TrackService;
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
public class PlaylistService {
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;
    private final TrackService trackService;
    private final TrackMapper trackMapper;

    public Playlist findById(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found: " + id));
    }

    public List<PlaylistDTO> findAll() {
        return playlistRepository.findAll().stream()
                .map(playlistMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PlaylistDTO create(PlaylistDTO playlistDTO) {
        playlistDTO.setDuration();

        return playlistMapper.toDTO(playlistRepository.save(
                playlistMapper.fromDTO(playlistDTO)
        ));
    }

    public PlaylistDTO update(Long id, PlaylistDTO playlistDTO) {
        Playlist actPlaylist = findById(id);
        actPlaylist.setTitle(playlistDTO.getTitle());
        actPlaylist.setTracks(playlistDTO.getTracks().stream().map(trackMapper::fromDto).collect(Collectors.toList()));
        actPlaylist.setDuration();
        return playlistMapper.toDTO(
                playlistRepository.save(actPlaylist)
        );
    }

    public void delete(Long id) {
        List<User> users = userRepository.findAll();
        for(User user:users){
            List<Playlist> playlists = new ArrayList<>();
            for(Playlist playlist: user.getPlaylistList()){
                if(!playlist.getId().equals(id)){
                    playlists.add(playlist);
                }
            }
            user.setPlaylistList(playlists);
            userRepository.save(user);
        }
        playlistRepository.deleteById(id);
    }

    public PlaylistDTO addTrackInPlaylist(Long playlist_id,TrackDTO trackDTO){
        Playlist actPlaylist = findById(playlist_id);
        Optional<Track> track = trackService.findByTitle(trackDTO.getTitle());
        if(track.isEmpty()) {
            TrackDTO trackDTO1 = trackService.create(trackDTO);
            actPlaylist.getTracks().add(trackMapper.fromDto(trackDTO1));
        }
        else{
            actPlaylist.getTracks().add(track.get());
        }
        if(actPlaylist != null){
            actPlaylist.setDuration();
        }
        return playlistMapper.toDTO(playlistRepository.save(actPlaylist));
    }
}
