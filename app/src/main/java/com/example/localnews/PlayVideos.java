package com.example.localnews;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PlayVideos extends Fragment {

    Button btnPlay;
    VideoView videoPlayer;
    MediaController mediaController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_player, container, false);

        btnPlay = (Button) view.findViewById(R.id.btnPlay);
        videoPlayer = (VideoView) view.findViewById(R.id.videoPlayer);

        mediaController = new MediaController(view.getContext());

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoPath = "https://www.dw.com/en/media-center/live-tv/s-100825";
                Uri uri = Uri.parse(videoPath);
                getDataSource(videoPath);
            }
        });

        return view;
    }

    private void getDataSource(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream stream = urlConnection.getInputStream();
                File temp = File.createTempFile("mediaplayertmp", "dat");
                temp.deleteOnExit();
                String tempPath = temp.getAbsolutePath();
                FileOutputStream out = new FileOutputStream(temp);
                byte buf[] = new byte[128];
                do {
                    int numread = stream.read(buf);
                    if (numread <= 0)
                        break;
                    out.write(buf, 0, numread);
                } while (true);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
