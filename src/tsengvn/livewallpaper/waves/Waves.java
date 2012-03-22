package tsengvn.livewallpaper.waves;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class Waves extends WallpaperService {
	private final Handler mHandler = new Handler();
	
	@Override
	public Engine onCreateEngine() {
		return new WavesEngine();
	}
	
	class WavesEngine extends Engine {
		private final Paint mPaint = new Paint();
        private float mOffset;
        private long mStartTime;
        private float mCenterX;
        private float mCenterY;
        private GifDecoder mGifDecoder;
        private boolean mVisible;
        Movie movie;
    	InputStream is;
    	private Bitmap mTmpBitmap;
    	int n ;
        int ntimes;
        int index;
        
        private final Runnable mDrawCube = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        
        public WavesEngine() {
            // Create a Paint to draw the lines for our cube
//            mStartTime = SystemClock.elapsedRealtime();
            try {
    			is = getApplicationContext().getAssets().open("waves.gif");
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
//    		movie=Movie.decodeStream(is);
    		mGifDecoder = new GifDecoder();
    		mGifDecoder.read(is);
    		n = mGifDecoder.getFrameCount();
    		ntimes = mGifDecoder.getLoopCount();
    		index = 0;
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawCube);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawCube);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // store the center of the surface, so we can draw the cube in the right spot
            mCenterX = width/2.0f;
            mCenterY = height/2.0f;
            drawFrame();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawCube);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
            mOffset = xOffset;
            drawFrame();
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                mTouchX = event.getX();
//                mTouchY = event.getY();
            } else {
//                mTouchX = -1;
//                mTouchY = -1;
            }
            super.onTouchEvent(event);
        }
        
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();
            int t = 0;
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
//                	long now = android.os.SystemClock.uptimeMillis();
//                	if (mStartTime == 0) { // first time
//                		 mStartTime = now;
//             	    }
//             	    if (movie != null) {
//             	        int dur = movie.duration();
//             	        if (dur == 0) {
//             	            dur = 1000;
//             	        }
//             	        int relTime = (int) ((now - mStartTime) % dur);
//             	        movie.setTime(relTime);
//             	        movie.draw(c, mCenterX - movie.width() / 2,
//             	                mCenterY - movie.height() / 2);
//             	    }
                	if (index >= n) index = 0;
                	mTmpBitmap = mGifDecoder.getFrame(index);
                	if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
                		c.drawBitmap(mTmpBitmap, -mOffset*100, mCenterY-mTmpBitmap.getHeight()/2, null);
                	}
                	t = mGifDecoder.getDelay(index);
                	Log.v("@nmh", "offset : " + mOffset);
                	index++;
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawCube);
            if (mVisible) {
                mHandler.postDelayed(mDrawCube, t);
            }
        }
	}

}
