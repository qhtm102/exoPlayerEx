package mnex.app.broadcast;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayer;

import com.google.android.exoplayer2.ExoPlayerFactory;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.io.IOException;

import javax.sql.DataSource;

public class Main extends Activity {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private String videoPath1 = "/mnt/sdcard/Content/893";
    private String videoPath2 = "/mnt/sdcard/Content/894";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exoplyer_main);
        playerView = findViewById(R.id.player_views);

        initializePlayer();

// 데이터 소스 팩토리 생성
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "your_app_name"));

        // 여러 개의 비디오 URI 설정
        Uri videoUri1 = Uri.parse(videoPath1);
        Uri videoUri2 = Uri.parse(videoPath2);
        Uri videoUri3 = Uri.parse(videoPath1);
        Uri videoUri4 = Uri.parse(videoPath2);

        // 각각의 미디어 소스 생성
        MediaSource mediaSource1 = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUri1);
        MediaSource mediaSource2 = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUri2);
        MediaSource mediaSource3 = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUri3);
        MediaSource mediaSource4 = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUri4);


        // ConcatenatingMediaSource 생성
        ConcatenatingMediaSource concatenatedSource = new ConcatenatingMediaSource();
        concatenatedSource.addMediaSource(mediaSource1);
        concatenatedSource.addMediaSource(mediaSource2);
        concatenatedSource.addMediaSource(mediaSource3);
        concatenatedSource.addMediaSource(mediaSource4);

        // 플레이어에 미디어 소스 설정
        player.prepare(concatenatedSource);
        player.setPlayWhenReady(true);
    }

    private void initializePlayer() {
        // SimpleExoPlayer 인스턴스 생성
        player = ExoPlayerFactory.newSimpleInstance(this);
        // PlayerView에 플레이어 설정
        playerView.setPlayer(player);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            player.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            player.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
        player = null;
    }
}
