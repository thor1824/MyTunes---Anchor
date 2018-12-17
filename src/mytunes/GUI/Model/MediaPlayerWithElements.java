/*
 * this media play connects GUI elemets with diffrent mediaplayer oparations
 * none of the elemets have to be connectet
 */
package mytunes.GUI.Model;

import java.io.File;
import static java.lang.Math.floor;
import static java.lang.String.format;
import java.util.Random;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import mytunes.BE.Song;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class MediaPlayerWithElements
{

    private final int SHUFFLE_ON = 1;
    private final int SHUFFLE_OFF = 0;
    private int shuffleState = SHUFFLE_OFF;
    private final int repeat_ON = 1;
    private final int repeat_OFF = 0;
    private int repeatState = repeat_OFF;
    private MediaPlayer mPlayer;
    private ObservableList<Song> activeObvPlaylist;
    private Song activeSong;
    private Duration duration;
    private Slider sldProg;
    private Slider sldVol;
    private ProgressBar songProg;
    private Label lbltime;
    private final int PAUSED = 0;
    private final int PLAYING = 1;
    private int playState = PAUSED;
    private ImageView btnPlay;
    private ImageView btnRepeat;
    private ImageView btnShuffle;
    private Label lblPlaying;

    public MediaPlayerWithElements(MediaPlayer mPlayer, ObservableList<Song> activeObvPlaylist, Song activeSong)
    {
        this.mPlayer = mPlayer;
        this.activeObvPlaylist = activeObvPlaylist;
        this.activeSong = activeSong;
        lbltime = new Label();
        sldProg = new Slider();
        sldVol = new Slider();
        songProg = new ProgressBar();
        btnRepeat = new ImageView();
        btnShuffle = new ImageView();
        btnPlay = new ImageView();
        lblPlaying = new Label();
    }

    /**
     * set the volume slider
     *
     * @param sldVol
     */
    public void setVolumeSlider(Slider sldVol)
    {
        this.sldVol = sldVol;
    }

    /**
     * set the volume playing label
     *
     * @param lblPlaying
     */
    public void setSongPlayingLabel(Label lblPlaying)
    {
        this.lblPlaying = lblPlaying;
    }

    /**
     * set the volume Repeat toggle button
     *
     * @param btnRepeat
     */
    public void setRepeatButton(ImageView btnRepeat)
    {
        this.btnRepeat = btnRepeat;
    }

    /**
     * set the volume shuffle toggle button
     *
     * @param btnShuffle
     */
    public void setShuffleButton(ImageView btnShuffle)
    {
        this.btnShuffle = btnShuffle;
    }

    /**
     * set the Song progress slider
     *
     * @param sldProg
     */
    public void setSongProgressSlider(Slider sldProg)
    {
        this.sldProg = sldProg;
    }

    /**
     * set the Song progress bar
     *
     * @param songProg
     */
    public void setSongProgressBar(ProgressBar songProg)
    {
        this.songProg = songProg;
    }

    /**
     * set the time label
     *
     * @param lbltime
     */
    public void setTimeLabel(Label lbltime)
    {
        this.lbltime = lbltime;
    }

    /**
     * set the playbutton
     *
     * @param btnPlay
     */
    public void setPlayButton(ImageView btnPlay)
    {
        this.btnPlay = btnPlay;
    }

    /**
     * set the first song
     *
     * @param song
     */
    public void setInitialSong(Song song)
    {

        activeSong = song;
        setSongElements(activeSong);

    }

    /**
     * toggles shuffle song
     */
    public void shuffleSongs()
    {
        switch (shuffleState)
        {
            case SHUFFLE_OFF:

                Image shuffleOn = new Image("Resouces/icons/icons8-Ashuffle-26.png");
                btnShuffle.setImage(shuffleOn);

                shuffleState = SHUFFLE_ON;
                checkShuffleState();

                break;
            case SHUFFLE_ON:
                Image shuffleOff = new Image("Resouces/icons/icons8-shuffle-26.png");
                btnShuffle.setImage(shuffleOff);

                shuffleState = SHUFFLE_OFF;
                checkShuffleState();
                break;
        }
    }

    /**
     * checks if the shuffle is on or off
     */
    private void checkShuffleState()
    {
        switch (shuffleState)
        {
            case SHUFFLE_OFF:
                mPlayer.setOnEndOfMedia(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        playNextSong();
                    }
                });
                break;

            case SHUFFLE_ON:
                mPlayer.setOnEndOfMedia(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Random random = new Random(System.currentTimeMillis());
                        int nextIndex = random.nextInt(activeObvPlaylist.size());
                        playSong(activeObvPlaylist.get(nextIndex));
                    }
                });
                break;
        }
    }

    /**
     * play the next song
     */
    public void playNextSong()
    {
        int nextIndex = activeObvPlaylist.indexOf(activeSong) + 1;
        if ((activeObvPlaylist.size() - 1) < nextIndex)
        {
            nextIndex = 0;
        }
        activeSong = activeObvPlaylist.get(nextIndex);
        playSong(activeSong);
    }

    /**
     * play previous song
     */
    public void playPreviousSong()
    {
        int previousIndex = activeObvPlaylist.indexOf(activeSong) - 1;
        if (previousIndex < 0)
        {
            previousIndex = activeObvPlaylist.size() - 1;
        }
        activeSong = activeObvPlaylist.get(previousIndex);
        playSong(activeSong);
    }

    /**
     * handles play oparations
     */
    private void mediaPlay()
    {
        Image pause = new Image("Resouces/icons/icons8-pause-30.png");
        btnPlay.setImage(pause);
        mPlayer.play();
        playState = PLAYING;

    }

    /**
     * handles pause oparations
     */
    public void mediaPause()
    {
        Image play = new Image("Resouces/icons/icons8-play-30.png");
        btnPlay.setImage(play);
        mPlayer.pause();
        playState = PAUSED;

    }

    /**
     * plays the given song
     *
     * @param song
     */
    public void playSong(Song song)
    {
        mPlayer.stop();
        activeSong = song;
        setSongElements(song);
        mediaPlay();

    }

    /**
     * sets the diffrent song elemets
     *
     * @param song
     */
    private void setSongElements(Song song)
    {
        String path = new File(song.getFilePath()).getAbsolutePath();
        // the mediaplayer cant read slashes so it replases them with emty and %20
        path.replace("\\", "/").replaceAll(" ", "%20");
        Media media = new Media(new File(path).toURI().toString());

        mPlayer = new MediaPlayer(media);

        lblPlaying.setText("now Playing...  " + song.getTitle()); //efter metoden i mtController
        ChangeVolume();
        updateValues();
        updateSlide();
        updateTimer();
        checkState();
    }

    /**
     * checks the state of the player ad sets the coreponding shuffle or repeat
     * state
     */
    private void checkState()
    {
        switch (repeatState)
        {
            case repeat_OFF:
                checkShuffleState();
                break;

            case repeat_ON:
                mPlayer.setOnEndOfMedia(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        playSong(activeSong);
                    }
                });
                break;
        }
    }
    /*
    *get the time of the songe and sets the sliter as the curent value
    */
    private void updateSlide() {
        sldProg.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable)
            {
                if (sldProg.isValueChanging())
                {
                    // multiply duration by percentage calculated by slider position
                    if (duration != null)
                    {
                        mPlayer.seek(duration.multiply(sldProg.getValue() / 100.0));
                    }
                    updateValues();

                }
            }
        });

        mPlayer.currentTimeProperty().addListener(new ChangeListener()
        {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                updateValues();
            }
        });

        sldProg.valueProperty().addListener(new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val)
            {
                songProg.setProgress(new_val.doubleValue() / 100);
            }
        });

    }

    /**
     * update time of the new song
     */
    private void updateTimer()
    {
        mPlayer.currentTimeProperty().addListener((Observable ov) ->
        {
            updateValues();
        });

        mPlayer.setOnReady(() ->
        {
            duration = mPlayer.getMedia().getDuration();
            updateValues();
        });
    }

    /**
     * updates values of the elemets connected to the mediaplayer
     */
    private void updateValues()
    {
        if (lbltime != null && sldProg != null && duration != null)
        {
            Platform.runLater(new Runnable()
            {
                public void run()
                {
                    Duration currentTime = mPlayer.getCurrentTime();
                    lbltime.setText(formatTime(currentTime, duration));
                    sldProg.setDisable(duration.isUnknown());
                    if (!sldProg.isDisabled() && duration.greaterThan(Duration.ZERO) && !sldProg.isValueChanging())
                    {
                        sldProg.setValue(currentTime.divide(duration).toMillis() * 100.0);
                    }

                }
            });
        }
    }

    /**
     * formats the time to the format Hours:Minutes:seconds
     *
     * @param elapsed
     * @param duration
     * @return
     */
    private static String formatTime(Duration elapsed, Duration duration)
    {
        int intElapsed = (int) floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0)
        {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO))
        {
            int intDuration = (int) floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0)
            {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0)
            {
                return format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else
            {
                return format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else
        {
            if (elapsedHours > 0)
            {
                return format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else
            {
                return format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

    /**
     * toggles the play/pause state
     */
    public void playPauseSwitch()
    {
        switch (playState)
        {
            case PAUSED:
                mediaPlay();
                break;
            case PLAYING:
                mediaPause();
                break;
        }
    }

    /**
     * toggles the repeat on/off state
     */
    public void RepeatSongs()
    {
        switch (repeatState)
        {
            case repeat_OFF:

                Image repeatOn = new Image("Resouces/icons/icons8-Arefresh-96.png");
                btnRepeat.setImage(repeatOn);

                repeatState = repeat_ON;
                checkState();

                break;

            case repeat_ON:
                Image repeatOFF = new Image("Resouces/icons/icons8-refresh-96.png");
                btnRepeat.setImage(repeatOFF);
                repeatState = repeat_OFF;
                checkState();

                break;

        }
    }

    /**
     * get the active list og song
     *
     * @return
     */
    public ObservableList<Song> getActivelistOfSongs()
    {
        return activeObvPlaylist;
    }

    /**
     * jumps the speciefic place in the song
     */
    public void jumpinSong()
    {

        Duration jumpToTime = new Duration(duration.toMillis() / 100 * sldProg.getValue());
        mPlayer.seek(jumpToTime);

    }

    /**
     * changes the music list
     *
     * @param list
     */
    public void changeMusicList(ObservableList list)
    {
        activeObvPlaylist.setAll(list);
    }

    /**
     * changes the volume of the mediaplayer
     */
    public void ChangeVolume()
    {
        if (mPlayer != null)
        {
            double volume = sldVol.getValue();
            double max = sldVol.getMax();
            mPlayer.setVolume((volume / max * 100) / 100);
        }

    }
}
