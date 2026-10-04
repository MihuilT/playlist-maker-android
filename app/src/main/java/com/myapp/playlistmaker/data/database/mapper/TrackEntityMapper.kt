package com.myapp.playlistmaker.data.database.mapper

import com.myapp.playlistmaker.data.database.entity.TrackEntity
import com.myapp.playlistmaker.domain.Track

object TrackEntityMapper {
    fun TrackEntity.toTrack(): Track {
        return Track(
            id = this.id,
            trackName = this.trackName,
            artistName = this.artistName,
            trackTime = this.trackTime,
            image = this.image,
            favorite = this.favorite,
            playlistId = this.playlistId,
            collectionName = this.collectionName,
            primaryGenreName = this.primaryGenreName,
            releaseDate = this.releaseDate,
            trackTimeMillis = this.trackTimeMillis
        )
    }

    fun Track.toEntity(): TrackEntity {
        return TrackEntity(
            id = this.id,
            trackName = this.trackName,
            artistName = this.artistName,
            trackTime = this.trackTime,
            image = this.image,
            favorite = this.favorite,
            playlistId = this.playlistId,
            collectionName = this.collectionName,
            primaryGenreName = this.primaryGenreName,
            releaseDate = this.releaseDate,
            trackTimeMillis = this.trackTimeMillis
        )
    }
}
