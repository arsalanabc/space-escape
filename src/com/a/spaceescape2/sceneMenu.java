package com.a.spaceescape2;

import java.util.LinkedList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;

public class sceneMenu extends BaseGameActivity {
	
	

	private int CAMERA_WIDTH;
	private int CAMERA_HEIGHT;
	private Camera mCamera;
	private BitmapTextureAtlas mBitmapTextureAtlas;

	private TextureRegion mTargetTextureRegion;
	private BitmapTextureAtlas mBitmapTextureAtlas2;
	private TiledTextureRegion mbombTextureRegion;
	private Scene mMainScene;
	//private AnimatedSprite ball;
	private TextureRegion mPlayerTextureRegion;
	private TextureRegion mbombTextureRegion_box;
	private Sprite bomb;
	
	// This one is for the font
	private BitmapTextureAtlas mFontTexture;
	private Font mFont;
	private ChangeableText score;
	
	
	// add
	private LinkedList<AnimatedSprite> Bomb_targets;
	private LinkedList<AnimatedSprite> Bomb_targetsToBeAdded;
	
	private float scale_factor =   1;
	///
	
	float t343twidth;
	
	private float centerX;
	private float centerY;
	
	private int col = 28;
	private int row  = 18;
	private TextureRegion mteleporterTextureRegion;
	private BitmapTextureAtlas mBitmapTextureAtlas_tele;



	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public Engine onLoadEngine() {
		// getting the device's screen size
				final Display display = getWindowManager().getDefaultDisplay();
				CAMERA_WIDTH = display.getWidth();
				CAMERA_HEIGHT = display.getHeight();

				// setting up the camera [AndEngine's camera , not the one you take
				// pictures with]
				mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
				
	
					

				// Engine with varius options
				return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
						new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera)
						.setNeedsSound(true).setNeedsMusic(true));
	}

	@Override
	public void onLoadResources() {

		// prepare a container for the font
		mFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		
		// prepare a container for the image
				mBitmapTextureAtlas = new BitmapTextureAtlas(512, 512,
						TextureOptions.BILINEAR_PREMULTIPLYALPHA);
				// prepare a container for the font
			
				// setting assets path for easy access
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

				// loading the image inside the container
				mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
						.createFromAsset(this.mBitmapTextureAtlas, this, "Player.png",
								0, 0);
				mbombTextureRegion_box = BitmapTextureAtlasTextureRegionFactory.
						createFromAsset(this.mBitmapTextureAtlas, this, "bomb.png", 0, 0);
			
				
				mTargetTextureRegion = BitmapTextureAtlasTextureRegionFactory
						.createFromAsset(this.mBitmapTextureAtlas, this, "box.png",
								128, 0);
				
				
				mBitmapTextureAtlas2 = new BitmapTextureAtlas(64, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
				mbombTextureRegion = BitmapTextureAtlasTextureRegionFactory.
						createTiledFromAsset(this.mBitmapTextureAtlas2, this, "flag2.png", 0, 0, 2, 1);
				
				mBitmapTextureAtlas_tele = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
				mteleporterTextureRegion = BitmapTextureAtlasTextureRegionFactory
						.createFromAsset(this.mBitmapTextureAtlas_tele, this, "face_box.png",
								128, 0);
				
				mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas2);
				 mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
				 mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas_tele);
				 
				// preparing the font
					mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT,
							Typeface.BOLD), 40, true, Color.YELLOW);
					mEngine.getTextureManager().loadTexture(mFontTexture);
					mEngine.getFontManager().loadFont(mFont);
		
	}

	@Override
	public Scene onLoadScene() {

		centerX = (CAMERA_WIDTH - this.mbombTextureRegion.getWidth()) / 2;
		centerY = (CAMERA_HEIGHT) / 2 - this.mbombTextureRegion.getHeight();
		
		// set background color
				mMainScene = new Scene();
				mMainScene
						.setBackground(new ColorBackground(0f, 0f, 0f));
			
			///// addeddd
				// initializing variables
			Bomb_targets = new LinkedList<AnimatedSprite>();
			Bomb_targetsToBeAdded = new LinkedList<AnimatedSprite>();
			////
		
			bomb = new Sprite(centerX, centerY, this.mbombTextureRegion_box);
			scale_factor = Math.min((CAMERA_HEIGHT/(bomb.getHeight()*row )),(CAMERA_WIDTH/(bomb.getHeight()*col)));
			//scale_factor = (float) 0.6;
			bomb.setScale(scale_factor);
			t343twidth = bomb.getWidthScaled();
			
			// settings score to the value of the max score to make sure it appears
						// correctly on the screen
						score = new ChangeableText(0, 0, mFont, String.valueOf(scale_factor));
						// repositioning the score later so we can use the score.getWidth()
						score.setPosition(mCamera.getWidth() - score.getWidth() - 5, 5);
						
						mMainScene.attachChild(score);
			
			

		

			//	ball = new AnimatedSprite(centerX - this.mbombTextureRegion.getHeight(), 410  , this.mbombTextureRegion);
			//	ball.animate(100);
						
						
			///////////////////////////
			//======================//
			//======================//
			//======================//
			///////////////////////////
						
						
			bomb_target();				
			addTarget();	
			addteteporter();
		
				mMainScene.attachChild(bomb);
			//	mMainScene.attachChild(ball);
				
			return mMainScene;

		
		
	}
	
	
	
	
	
	private void bomb_target() {
		float x; // = (int) mCamera.getWidth() + mTargetTextureRegion.getWidth();
		float y; //= rand.nextInt(rangeY) + minY;
		
		
		x = 200 ;
		y = CAMERA_HEIGHT / 4;
	//	bomb.setPosition(x,y);
		
		
		
		for(int i = 0; i < 1; i++) {
			
				switch(i){
				case 0:
					x = centerX - bomb.getWidthScaled()*2;
					y = centerY - bomb.getWidthScaled()*2;
					break;
					


				}	
		
			
			AnimatedSprite bomb_target = new AnimatedSprite(x, y  , this.mbombTextureRegion.deepCopy());

			mMainScene.attachChild(bomb_target);
			Bomb_targetsToBeAdded.add(bomb_target);
			
			 
		}// bomb_target
	}

	// adds a target at a random location and let it move along the x-axis
	public void addTarget() {
	
		float x; // = (int) mCamera.getWidth() + mTargetTextureRegion.getWidth();
		float y; //= rand.nextInt(rangeY) + minY;
		
		
		
		x = centerX - t343twidth;
		y = centerY - t343twidth*2;
		bomb.setPosition(x,y);
				
		
		int[] array_col =new int[]{
				-14,-13,-12,-11,-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0,-1,-2,-2,-3,-5,-6,-7,-9,-9,-10,-11,-12,-14,
				0,0,0,0,-1,-3,-4,-5,-6,-7,-9,-9,-10,
				1,2,3,4,5,6,7,8,9,10,11,12,13,1,1,3,3,4,4,5,8,8,9,11,12,5,7,
				1,1,1,2,3,4,5,6,8,10,11,12,13,13
	};
		
		int[] array_row = new int[]{
				-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-3,-3,-2,-4,-6,-9,-7,-6,-5,-5,-8,-2,-3,
				1,2,6,8,1,5,1,3,7,1,1,8,4,
				-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-5,-4,-6,-5,-2,-5,-6,-3,-9,-5,-8,-4,0,0,
				1,4,6,1,8,5,1,4,5,8,4,1,6,7

		};

					for (int c = 0; c < array_col.length ; c++){ // col
					x = centerX + t343twidth*array_col[c];   
					y = centerY + t343twidth*array_row[c];
		
				
				Sprite target = new Sprite(x, y, mTargetTextureRegion.deepCopy());

				target.setScale(scale_factor);
				
				mMainScene.attachChild(target);

									 
		}// for example
		
	
	}
	
	public void addteteporter(){
		float x ,y;
	
		x = centerX + t343twidth*-14 ;   
		y = centerY - t343twidth*0;
	Sprite teleport_1 = new Sprite(x, y, this.mteleporterTextureRegion.deepCopy());
		
	
			x = centerX + t343twidth*13;   
			y = centerY + t343twidth*-2;
		
	Sprite teleport_2 = new Sprite(x, y, this.mteleporterTextureRegion.deepCopy());

		teleport_1.setScale(scale_factor);
		teleport_2.setScale(scale_factor);
		
		mMainScene.attachChild(teleport_1);
		mMainScene.attachChild(teleport_2);

		
	}
	
}

// for time handler
// http://www.andengine.org/forums/tutorials/using-timer-s-sprite-spawn-example-t463.html