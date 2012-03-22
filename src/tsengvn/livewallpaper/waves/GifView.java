package tsengvn.livewallpaper.waves;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GifView extends View{

	Movie movie;
	InputStream is=null,is1=null;
	long moviestart;
	public GifView(Context context) {
		super(context);
		try {
			is = context.getAssets().open("waves.gif");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		movie=Movie.decodeStream(is);
	}
	
	public GifView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GifView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		super.onDraw(canvas);
		
	    Paint p = new Paint();
	    p.setAntiAlias(true);

	    long now = android.os.SystemClock.uptimeMillis();
	    if (moviestart == 0) { // first time
	    	moviestart = now;
	    }
	    if (movie != null) {
	        int dur = movie.duration();
	        if (dur == 0) {
	            dur = 1000;
	        }
	        int relTime = (int) ((now - moviestart) % dur);
	        movie.setTime(relTime);
	        movie.draw(canvas, getWidth() / 2 - movie.width() / 2,
	                getHeight() / 2 - movie.height() / 2);
	        invalidate();
	    }
			
	}

}
