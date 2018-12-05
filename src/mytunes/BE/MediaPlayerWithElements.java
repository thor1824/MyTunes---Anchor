/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

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
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class SongPlayer {

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
    private ProgressBar songProg;
    private Label lbltime;
    private final int PAUSED = 0;
    private final int PLAYING = 1;
    private int playState = PAUSED;
    private ImageView btnPlay;

    public SongPlayer(MediaPlayer mPlayer, ObservableList<Song> activeObvPlaylist, Song activeSong) {
        this.mPlayer = mPlayer;
        this.activeObvPlaylist = activeObvPlaylist;
        this.activeSong = activeSong;
        lbltime = new Label();
        sldProg = new Slider();
        songProg = new ProgressBar();
    }

    public void setSldProg(Slider sldProg) {
        this.sldProg = sldProg;
    }

    public void setSongProg(ProgressBar songProg) {
        this.songProg = songProg;
    }

    public void setLbltime(Label lbltime) {
        this.lbltime = lbltime;
    }

    public void setBtnPlay(ImageView btnPlay) {
        this.btnPlay = btnPlay;
    }
    

    public void shuffleWithButton(ImageView imageView) {
        switch (shuffleState) {
            case SHUFFLE_OFF:

                Image shuffleOn = new Image("mytunes/GUI/View/Resouces/icons/icons8-Ashuffle-26.png");
                imageView.setImage(shuffleOn);

                shuffleState = SHUFFLE_ON;
                checkShuffleState();

                break;
            case SHUFFLE_ON:
                Image shuffleOff = new Image("mytunes/GUI/View/Resouces/icons/icons8-shuffle-26.png");
                imageView.setImage(shuffleOff);

                shuffleState = SHUFFLE_OFF;
                checkShuffleState();
                break;
        }
    }

    private void checkShuffleState() {
        switch (shuffleState) {
            case SHUFFLE_OFF:
                mPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        NextSong();
                    }
                });
                break;

            case SHUFFLE_ON:
                mPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        Random random = new Random(System.currentTimeMillis());
                        int nextIndex = random.nextInt(activeObvPlaylist.size());
                        playSong(activeObvPlaylist.get(nextIndex));
                    }
                });
                break;
        }
    }

    private void NextSong() {
        int nextIndex = activeObvPlaylist.indexOf(activeSong) + 1;
        if ((activeObvPlaylist.size() - 1) < nextIndex) {
            nextIndex = 0;
        }
        activeSong = activeObvPlaylist.get(nextIndex);
        playSong(activeSong);
    }

    private void mediaPlay() {
        Image pause = new Image("mytunes/GUI/View/Resouces/icons/icons8-pause-30.png");
        btnPlay.setImage(pause);
        mPlayer.play();
        playState = PLAYING;

    }

    private void mediaPause() {
        Image play = new Image("mytunes/GUI/View/Resouces/icons/icons8-play-30.png");
        btnPlay.setImage(play);
        mPlayer.pause();
        playState = PAUSED;

    }

    private void playSong(Song song) {
        mPlayer.stop();
        activeSong = song;
        setSongElements(song);
        mediaPlay();

    }

    private void setSongElements(Song song) {
        String path = new File(song.getFilePath()).getAbsolutePath();
        path.replace("\\", "/").replaceAll(" ", "%20");
        Media media = new Media(new File(path).toURI().toString());

        mPlayer = new MediaPlayer(media);

//        lblPlaying.setText("now Playing...  " + song.getTitle()); //efter metoden i mtController
        //volSlider(null); // i GUI
//        updateValues(); // til GUI
//        updateSlide();
//        updateTimer();
//        checkState();
    }

    private void checkState() {
        switch (repeatState) {
            case repeat_OFF:
                checkShuffleState();
                break;

            case repeat_ON:
                mPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        playSong(activeSong);
                    }
                });
                break;
        }
    }

    private void updateSlide(Slider slider) {
        slider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (slider.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                    if (duration != null) {
                        mPlayer.seek(duration.multiply(slider.getValue() / 100.0));
                    }
                    updateValues();

                }
            }
        });

        mPlayer.currentTimeProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                updateValues();
            }
        });

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                songProg.setProgress(new_val.doubleValue() / 100);
            }
        });

    }

    private void updateTimer() {
        mPlayer.currentTimeProperty().addListener((Observable ov) -> {
            updateValues();
        });

        mPlayer.setOnReady(() -> {
            duration = mPlayer.getMedia().getDuration();
            updateValues();
        });
    }

    private void updateValues() {
        if (lbltime != null && sldProg != null && duration != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Duration currentTime = mPlayer.getCurrentTime();
                    lbltime.setText(formatTime(currentTime, duration));
                    sldProg.setDisable(duration.isUnknown());
                    if (!sldProg.isDisabled() && duration.greaterThan(Duration.ZERO) && !sldProg.isValueChanging()) {
                        sldProg.setValue(currentTime.divide(duration).toMillis() * 100.0);
                    }

                }
            });
        }
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

}
