package net.sileht.lullaby;
/* Copyright (c) 20010 ABAAKOUK Mehdi  <theli48@gmail.com>
*
* +------------------------------------------------------------------------+
* | This program is free software; you can redistribute it and/or          |
* | modify it under the terms of the GNU General Public License            |
* | as published by the Free Software Foundation; either version 2         |
* | of the License, or (at your option) any later version.                 |
* |                                                                        |
* | This program is distributed in the hope that it will be useful,        |
* | but WITHOUT ANY WARRANTY; without even the implied warranty of         |
* | MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the          |
* | GNU General Public License for more details.                           |
* |                                                                        |
* | You should have received a copy of the GNU General Public License      |
* | along with this program; if not, write to the Free Software            |
* | Foundation, Inc., 59 Temple Place - Suite 330,                         |
* | Boston, MA  02111-1307, USA.                                           |
* +------------------------------------------------------------------------+
*/
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ViewUtils implements OnItemLongClickListener, OnItemClickListener,
		OnCreateContextMenuListener {
	public static final String ARTIST_ID = "_id";
	public static final String ARTIST_NAME = "ARTIST_NAME";
	public static final String ARTIST_ALBUMS = "ARTIST_ALBUMS";
	public static final String ARTIST_TRACKS = "ARTIST_TRACKS";

	public static final String ALBUM_ID = "_id";
	public static final String ALBUM_NAME = "ALBUM_NAME";
	public static final String ALBUM_ARTIST = "ALBUM_ARTIST";
	public static final String ALBUM_TRACKS = "ALBUM_TRACKS";
	public static final String ALBUM_ART = "ALBUM_ART";

	public static final String SONG_ID = "_id";
	public static final String SONG_NAME = "SONG_NAME";
	public static final String SONG_ALBUM = "SONG_ALBUM";
	public static final String SONG_ARTIST = "SONG_ARTIST";
	public static final String SONG_URL = "SONG_URL";
	public static final String SONG_DURATION = "SONG_DURATION";
	public static final String SONG_EXTRA = "SONG_EXTRA";

	public static final String PLAYLIST_ID = "_id";
	public static final String PLAYLIST_NAME = "PLAYLIST_NAME";
	public static final String PLAYLIST_TRACKS = "PLAYLIST_TRACKS";
	public static final String PLAYLIST_OWNER = "PLAYLIST_OWNER";
	
	public static final String[] mArtistColumnName = new String[] { ARTIST_ID,
			ARTIST_NAME, ARTIST_ALBUMS, ARTIST_TRACKS };

	public static final String[] mAlbumsColumnName = new String[] { ALBUM_ID,
			ALBUM_NAME, ALBUM_ARTIST, ALBUM_TRACKS, ALBUM_ART };

	public static final String[] mSongsColumnName = new String[] { SONG_ID,
			SONG_NAME, SONG_ALBUM, SONG_ARTIST, SONG_URL, SONG_DURATION,
			SONG_EXTRA };
	
	public static final String[] mPlaylistsColumnName = new String[] { PLAYLIST_ID,
		PLAYLIST_NAME, PLAYLIST_TRACKS, PLAYLIST_OWNER};

	private Activity mCurrentActivity;

	public ViewUtils(Activity activity) {
		mCurrentActivity = activity;
	}

	public void startSongsActivity(String type, String id, String title) {
		Intent intent = new Intent().setClass(mCurrentActivity,
				SongsActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("id", id);
		intent.putExtra("title", title);
		mCurrentActivity.startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		Cursor cursor = (Cursor) l.getItemAtPosition(position);
		if (cursor.getColumnName(1) == ARTIST_NAME) {

			String artistId = cursor.getString(cursor
					.getColumnIndexOrThrow(ARTIST_ID));
			String artist = cursor.getString(cursor
					.getColumnIndexOrThrow(ARTIST_NAME));
			
			startSongsActivity("artist_songs", artistId, artist);

		} else if (cursor.getColumnName(1) == ALBUM_NAME) {

			String albumId = cursor.getString(cursor
					.getColumnIndexOrThrow(ALBUM_ID));
			String album = cursor.getString(cursor
					.getColumnIndexOrThrow(ALBUM_NAME));
			String artist = cursor.getString(cursor
					.getColumnIndexOrThrow(ALBUM_ARTIST));

			startSongsActivity("album_songs", albumId, album+" - "+artist);
			
		} else if (cursor.getColumnName(1) == PLAYLIST_NAME) {

			String playlistId = cursor.getString(cursor
					.getColumnIndexOrThrow(PLAYLIST_ID));
			String playlist = cursor.getString(cursor
					.getColumnIndexOrThrow(PLAYLIST_NAME));
			String author = cursor.getString(cursor
					.getColumnIndexOrThrow(PLAYLIST_OWNER));

			startSongsActivity("playlist_songs", playlistId, playlist + " - " + author);

		} else if (cursor.getColumnName(1) == SONG_NAME) {

			String songId = cursor.getString(cursor
					.getColumnIndexOrThrow(SONG_ID));

			Lullaby.pl.appendSongs(mCurrentActivity, new String[] { "song",
					songId });
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> l, View v, int position,
			long id) {
		Cursor cursor = (Cursor) l.getItemAtPosition(position);
		if (cursor.getColumnName(1) == ARTIST_NAME) {

			String artistId = cursor.getString(cursor
					.getColumnIndexOrThrow(ARTIST_ID));
			String artist = cursor.getString(cursor
					.getColumnIndexOrThrow(ARTIST_NAME));
			int tracks = cursor.getInt(cursor
					.getColumnIndexOrThrow(ARTIST_TRACKS));

			Lullaby.pl.appendSongs(mCurrentActivity, new String[] {
					"artist_songs", artistId });

			Toast.makeText(mCurrentActivity,
					"Enqueue " + artist + ": " + tracks + " songs",
					Toast.LENGTH_LONG).show();

		} else if (cursor.getColumnName(1) == ALBUM_NAME) {

			String albumId = cursor.getString(cursor
					.getColumnIndexOrThrow(ALBUM_ID));
			String album = cursor.getString(cursor
					.getColumnIndexOrThrow(ALBUM_NAME));
			int tracks = cursor.getInt(cursor
					.getColumnIndexOrThrow(ALBUM_TRACKS));

			Toast.makeText(mCurrentActivity,
					"Enqueue " + album + ": " + tracks + " songs",
					Toast.LENGTH_LONG).show();

			Lullaby.pl.appendSongs(mCurrentActivity, new String[] {
					"album_songs", albumId });

		} else if (cursor.getColumnName(1) == SONG_NAME) {

			String songId = cursor.getString(cursor
					.getColumnIndexOrThrow(SONG_ID));
			String song = cursor.getString(cursor
					.getColumnIndexOrThrow(SONG_NAME));
			String artist = cursor.getString(cursor
					.getColumnIndexOrThrow(SONG_ARTIST));

			Toast.makeText(mCurrentActivity,
					"Enqueue " + song + " - " + artist, Toast.LENGTH_LONG)
					.show();

			Lullaby.pl.appendSongs(mCurrentActivity, new String[] { "song",
					songId });

		} else if (cursor.getColumnName(1) == PLAYLIST_NAME) {

			String playlistId = cursor.getString(cursor
					.getColumnIndexOrThrow(PLAYLIST_ID));
			String playlistName = cursor.getString(cursor
					.getColumnIndexOrThrow(PLAYLIST_NAME));
			int tracks = cursor.getInt(cursor
					.getColumnIndexOrThrow(PLAYLIST_TRACKS));

			Toast.makeText(mCurrentActivity,
					"Enqueue " + playlistName + ": " + tracks + " songs",
					Toast.LENGTH_LONG).show();

			Lullaby.pl.appendSongs(mCurrentActivity, new String[] {
					"playlist_songs", playlistId });

		}
		return true;
	}

	private final static int MENU_PLAY = 0;
	private final static int MENU_ENQUEUE = 1;
	private final static int MENU_ENQUEUE_PLAY = 2;
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuinfo) {
		menu.add(0, MENU_PLAY, 0, "Play").setIcon(android.R.drawable.ic_media_play);
		menu.add(0, MENU_ENQUEUE, 0, "Enqueue").setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, MENU_ENQUEUE_PLAY, 0, "Enqueue and Play").setIcon(android.R.drawable.ic_menu_add);
		
	}
}