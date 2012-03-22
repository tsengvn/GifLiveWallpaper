package tsengvn.livewallpaper.waves;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;

public class WavesLiveWallpaperActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		GifView gif = new GifView(this);
//		setContentView(gif);
		
		InputStream stream = null;
		try {
		      stream = getAssets().open("waves.gif");
		   } catch (IOException e) {
		      e.printStackTrace();
		   }

		// GifMovieView view = new GifMovieView(this, stream);
		   GifDecoderView view = new GifDecoderView(this, stream);

		   setContentView(view);
		}
}
