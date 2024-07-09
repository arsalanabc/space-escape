package com.a.spaceescape2;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.MoveXModifier;
import org.anddev.andengine.entity.modifier.MoveYModifier;
import org.anddev.andengine.entity.modifier.RotationAtModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.batch.SpriteBatch;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.ParallelModifier;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.Toast;

public class newgame extends BaseGameActivity implements
		IOnSceneTouchListener {

	private Camera mCamera;

	// This one is for the font
	private BitmapTextureAtlas mFontTexture;
	private Font mFont;
	private ChangeableText score;
	private ChangeableText bullets;

	// this one is for all other textures
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mPlayerTextureRegion;
	private TextureRegion mProjectileTextureRegion;
	private TextureRegion mTargetTextureRegion;
	private TextureRegion mPausedTextureRegion;
	private TextureRegion mWinTextureRegion;
	private TextureRegion mFailTextureRegion;

	// the main scene for the game
	private Scene mMainScene;
	private Sprite player;

	// win/fail sprites
	private Sprite winSprite;
	private Sprite failSprite;

	private LinkedList<Sprite> projectileLL;
	private LinkedList<Sprite> targetLL;
	private LinkedList<Sprite> projectilesToBeAdded;
	private LinkedList<Sprite> TargetsToBeAdded;
	
	private LinkedList<Sprite> specialLL;
	private LinkedList<Sprite> specialsToBeAdded;
	
	
	private Sound popingSound;
	private Music backgroundMusic;
	private boolean runningFlag = false;
	private boolean pauseFlag = false;
	private CameraScene mPauseScene;
	private CameraScene mResultScene;
	private int hitCount;
	private int num_bullets = 500;;
	private final int maxScore = 1000000;

	private int CAMERA_WIDTH;

	private int CAMERA_HEIGHT;

	private float centerX;

	private float centerY;

	private BitmapTextureAtlas mBitmapTextureAtlas_pin;

	private BitmapTextureAtlas mBitmapTextureAtlas_balloon;

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
		// prepare a container for the image
		mBitmapTextureAtlas = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		mBitmapTextureAtlas_pin = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mBitmapTextureAtlas_balloon = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		// prepare a container for the font
		mFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// loading the image inside the container
		mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas_pin, this, "pin4.png",
						0, 0);
		mProjectileTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"pin3.png", 64, 0);
		mTargetTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas_balloon, this, "balloon-white.png",
						128, 0);
		mPausedTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "paused.png",
						0, 64);
		mWinTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "win.png", 0,
						128);
		mFailTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "fail.png", 0,
						256);

		// preparing the font
		mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT,
				Typeface.BOLD), 40, true, Color.BLACK);

		// loading textures in the engine
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas_pin);
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas_balloon);
		mEngine.getTextureManager().loadTexture(mFontTexture);
		mEngine.getFontManager().loadFont(mFont);

		SoundFactory.setAssetBasePath("mfx/");
		try {
			popingSound = SoundFactory.createSoundFromAsset(mEngine
					.getSoundManager(), this, "pop.wav");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MusicFactory.setAssetBasePath("mfx/");

		try {
			backgroundMusic = MusicFactory.createMusicFromAsset(mEngine
					.getMusicManager(), this, "background_music.wav");
			backgroundMusic.setLooping(true);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Scene onLoadScene() {
		mEngine.registerUpdateHandler(new FPSLogger());

		// creating a new scene for the pause menu
		mPauseScene = new CameraScene(mCamera);
		/* Make the label centered on the camera. */
		final int x = (int) (mCamera.getWidth() / 2 - mPausedTextureRegion
				.getWidth() / 2);
		final int y = (int) (mCamera.getHeight() / 2 - mPausedTextureRegion
				.getHeight() / 2);
		final Sprite pausedSprite = new Sprite(x, y, mPausedTextureRegion);
		mPauseScene.attachChild(pausedSprite);
		// makes the scene transparent
		mPauseScene.setBackgroundEnabled(false);

		// the results scene, for win/fail
		mResultScene = new CameraScene(mCamera);
		winSprite = new Sprite(x, y, mWinTextureRegion);
		failSprite = new Sprite(x, y, mFailTextureRegion);
		mResultScene.attachChild(winSprite);
		mResultScene.attachChild(failSprite);
		// makes the scene transparent
		mResultScene.setBackgroundEnabled(false);

		winSprite.setVisible(false);
		failSprite.setVisible(false);

		// set background color
		mMainScene = new Scene();
		mMainScene
				.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		mMainScene.setOnSceneTouchListener(this);

		// set coordinates for the player
		final int PlayerX = this.mPlayerTextureRegion.getWidth() / 2;
		final int PlayerY = (int) ((mCamera.getHeight() - mPlayerTextureRegion
				.getHeight()) / 2);
		
		centerX = (CAMERA_WIDTH - this.mTargetTextureRegion.getWidth()) / 2;
		centerY = (CAMERA_HEIGHT - this.mTargetTextureRegion.getHeight()) / 2;
		
		

		// set the player on the scene
		player = new Sprite(0, 0, mPlayerTextureRegion);
	
		// for moving accros the screen
		//player.registerEntityModifier(new MoveXModifier(30, 0,mCamera.getWidth()));
		
		
		//player.setScale(2);
		mMainScene.attachChild(player);
		

		// initializing variables
		projectileLL = new LinkedList<Sprite>();
		targetLL = new LinkedList<Sprite>();
		projectilesToBeAdded = new LinkedList<Sprite>();
		TargetsToBeAdded = new LinkedList<Sprite>();
		specialLL = new LinkedList<Sprite>();
		specialsToBeAdded = new LinkedList<Sprite>();
		

		// settings score to the value of the max score to make sure it appears
		// correctly on the screen
		score = new ChangeableText(0, 0, mFont, String.valueOf(maxScore));
		// repositioning the score later so we can use the score.getWidth()
		score.setPosition(mCamera.getWidth() - score.getWidth() - 5, CAMERA_HEIGHT- score.getHeightScaled());
		mMainScene.attachChild(score);
		
		// settings bullets to the value of the max bullet to make sure it appears
				// correctly on the screen
		
				bullets = new ChangeableText(0, 0, mFont, String.valueOf(num_bullets));
				// repositioning the score later so we can use the score.getWidth()
				bullets.setPosition(bullets.getWidth() - 5, CAMERA_HEIGHT- score.getHeightScaled());
					
				mMainScene.attachChild(bullets);

		
	   createSpriteSpawnTimeHandler();
		mMainScene.registerUpdateHandler(detect);
		
		// starting background music
		backgroundMusic.play();
		// runningFlag = true;

	//	restart();
		//addTarget();
		//drawUsingSpritesWithSharedVertexBuffer(mMainScene);
		return mMainScene;
	}

	@Override
	public void onLoadComplete() {
	}

	// TimerHandler for collision detection and cleaning up
	IUpdateHandler detect = new IUpdateHandler() {
		@Override
		public void reset() {
		}

		@Override
		public void onUpdate(float pSecondsElapsed) {

			Iterator<Sprite> targets = targetLL.iterator();
			Sprite _target;
			boolean hit = false;

			// iterating over the targets
			while (targets.hasNext()) {
				_target = targets.next();

				// if target passed the left edge of the screen, then remove it
				// and call a fail
				if (_target.getX() <= -_target.getWidth()) {
					removeSprite(_target, targets);
					fail();
					break;
				}
				Iterator<Sprite> projectiles = projectileLL.iterator();
				Sprite _projectile;
				// iterating over all the projectiles (bullets)
				while (projectiles.hasNext()) {
					_projectile = projectiles.next();

					// in case the projectile left the screen
					if (_projectile.getX() >= CAMERA_WIDTH
							||
							_projectile.getX() < 0 
							||
							_projectile.getY() 	+ _projectile.getHeight() >= CAMERA_HEIGHT
								
							|| _projectile.getY() <= 0) {
						removeSprite(_projectile, projectiles);
						continue;
					}

					// if the targets collides with a projectile, remove the
					// projectile and set the hit flag to true
					if (_target.collidesWith(_projectile)) {
						
						removeSprite(_projectile, projectiles);
						removeSprite(_target, targets);
						// plays a poping sound
						popingSound.play();
						hit = true;
						break;						
					}
					
									
				}// end proj has next
			
				// player collides with target
				if(_target.collidesWith(player))
					{
					removeSprite(_target, targets);
					// plays a poping sound
					popingSound.play();
					hit = true;
					break;		
					}
				
				if (_target.getY() <= player.getY()) {
					
					//removeSprite(_projectile, projectiles);
					player.setPosition(player.getX(), player.getY()+_target.getHeightScaled());
					removeSprite(_target, targets);
					
					hit = true;
					break;
				}
				
				

				// if a projectile hit the target, remove the target, increment
				// the hit count, and update the score
				if (hit) {
				//	removeSprite(_target, targets);
					hit = false;
					hitCount++;
					change_text(score, hitCount);
					//score.setText(String.valueOf(hitCount));
				}
			}// while target has next
			
			
			///////////////////////////////////////////////
			// this is for the special sprites
			Iterator<Sprite> specials = specialLL.iterator();
			Sprite _special;
			
			// iterating over the targets
			while (specials.hasNext()) {
				_special = specials.next();
				
				if(_special.collidesWith(player))
					{//num_bullets += 100;
					
					if(_special.getZIndex() == 40)
						{change_text(bullets, -33);}
				
					if(_special.getZIndex() == 41)
					{change_text(bullets, 933);}
					removeSprite(_special, specials);
					//change_text(bullets, num_bullets);
					}// player collides special
				
			} // end special
			
			
			
			// if the player has reached the bottom
			if (player.getHeightScaled() + player.getY() > CAMERA_HEIGHT ) {
			//	player.clearEntityModifiers();
		//		player.setPosition(0,player.getY()+player.getHeightScaled());
				
			//	player.registerEntityModifier(new MoveXModifier(30, 0,CAMERA_WIDTH));
							// EaseBounceOut.getInstance()) );
				fail();
				}


			// if max score , then we are done
			if (hitCount >= maxScore) {
				win();
			}

			// a work around to avoid ConcurrentAccessException
			projectileLL.addAll(projectilesToBeAdded);
			projectilesToBeAdded.clear();

			targetLL.addAll(TargetsToBeAdded);
			TargetsToBeAdded.clear();
			
			specialLL.addAll(specialsToBeAdded);
			specialsToBeAdded.clear();
		}
	};

	

	/* safely detach the sprite from the scene and remove it from the iterator */
	public void removeSprite(final Sprite _sprite, Iterator<Sprite> it) {
		runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				mMainScene.detachChild(_sprite);
			}
		});
		it.remove();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

		// if the user tapped the screen
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN || pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
			final float touchX = pSceneTouchEvent.getX();
			final float touchY = pSceneTouchEvent.getY();
			
			
			
			if(player.getY() < touchY)
				
				//move the player with the touch
				player.setPosition(touchX, player.getY());
			
				if(num_bullets > 0)
				{
					num_bullets--;
					change_text(bullets, num_bullets);
					//bullets.setText(String.valueOf(num_bullets));
				
					shootProjectile(touchX, touchY);
					return true;
				}// end if num_nullets >0
		}
		return false;
	}

	/* shoots a projectile from the player's position along the touched area */
	private void shootProjectile(final float pX, final float pY) {

		int offX = (int) (pX );
		int offY = (int) (pY );
		if (offY <= 0)
			return;

		
		
		final Sprite projectile;
		// position the projectile on the player
		//projectile = new Sprite(player.getScaleX()+player.getWidthScaled()/2, player.getScaleY()+player.getHeightScaled()/2,
		//		mProjectileTextureRegion.deepCopy());
		
		projectile = new Sprite(player.getX()+player.getWidthScaled()/2, player.getY()+player.getHeightScaled()/2,
				mProjectileTextureRegion.deepCopy());
		
		projectile.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
	
			mMainScene.attachChild(projectile, 1);
			num_bullets--;
			

		
		int realX = (int) (mCamera.getWidth() + projectile.getWidth() / 2.0f);
		float ratio = (float) offY / (float) offX;
		int realY = (int) ((realX * ratio) + projectile.getY());

		int offRealX = (int) (realX - projectile.getX());
		int offRealY = (int) (realY - projectile.getY());
		float length = (float) Math.sqrt((offRealX * offRealX)
				+ (offRealY * offRealY));
		float velocity = 480.0f / 1.0f; // 480 pixels / 1 sec
		float realMoveDuration = (pY - player.getY()) / velocity;

		// defining a move modifier from the projectile's position to the
		// calculated one
		MoveModifier mod = new MoveModifier(realMoveDuration,
				projectile.getX(), pX , projectile.getY(), CAMERA_HEIGHT+10);
		
		MoveYModifier mod2 = new MoveYModifier(realMoveDuration,
				 projectile.getY(), CAMERA_HEIGHT+10);
	
		projectile.registerEntityModifier(mod.deepCopy());

		projectilesToBeAdded.add(projectile);
		
	}

	////////////////
	
	 private void drawUsingSpritesWithSharedVertexBuffer(final Scene scene) {
         /* As we are creating quite a lot of the same Sprites, we can let them share a VertexBuffer to significantly increase performance. */
         final RectangleVertexBuffer sharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_STATIC_DRAW, true);
         sharedVertexBuffer.update(this.mTargetTextureRegion.getWidth(), this.mTargetTextureRegion.getHeight());
         
         for(int i = 0; i < 100; i++) {
                 final Sprite face = new Sprite(centerX * (CAMERA_WIDTH - 32), (CAMERA_HEIGHT - 32), this.mTargetTextureRegion, sharedVertexBuffer);
                 face.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
                 face.setIgnoreUpdate(true);
                 scene.attachChild(face);
         }
 }
	
	/////////////////
	// adds a target at a random location and let it move along the x-axis
	public void addTarget() {
		
		Random rand = new Random();
		int minDuration = 2;
		int maxDuration = 4;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;
		int randpos_x = rand.nextInt( (int) (mCamera.getWidth() - mTargetTextureRegion.getWidth()));

		float x = 0, y = 0;
	//	for (int r = 0; r < 2 ; r++){ // row
		
			x = 400; 
		    y = CAMERA_HEIGHT - player.getWidthScaled();
		    
		    
		 Sprite target = new Sprite ( x, y, mTargetTextureRegion.deepCopy());
		// target.setScale(2);
		
		 // call ran_color
			float[] rc_code = rand_color.rand_color_code();
			
			// for the colour
			 target.setColor(rc_code[0], rc_code[1], rc_code[2]);
         /////////////////
         
         
         
		 mMainScene.attachChild(target);
		 MoveYModifier mod = new MoveYModifier(4,target.getY(),-1);
		 target.registerEntityModifier(mod.deepCopy());
		 
		 target.registerEntityModifier(
					
	                new RotationModifier(
	                    new Random().nextInt(10)+1,                 // duration
	                    0,                  // angle start                
	                    -360                // angle end
	                )
		);
		 target.registerEntityModifier(
		 new MoveXModifier(4,target.getX(),new Random().nextInt(CAMERA_WIDTH-(int)target.getWidthScaled()))
		);

		 TargetsToBeAdded.add(target);
		//}// end col
		
		
		
		/*SpriteBatch spriteBatch = new SpriteBatch(this.mBitmapTextureAtlas, 100);
		  spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		  for(int i = 0; i < 100; i++) {
		      x = i* (CAMERA_WIDTH - 32);
		      y =  (CAMERA_HEIGHT - 32);
		    spriteBatch.draw(this.mTargetTextureRegion, x, y, player.getWidth(),player.getHeight());
		  }
		  spriteBatch.submit();
		 
	//	  mMainScene.attachChild(spriteBatch);
	 
	 */
		 
	}

/////////////////
// adds a special with small probability
public void addspecial() {

Random rand = new Random();
int minDuration = 2;
int maxDuration = 4;
int rangeDuration = maxDuration - minDuration;
int actualDuration = rand.nextInt(rangeDuration) + minDuration;
int randpos_x = rand.nextInt( (int) (mCamera.getWidth() - mTargetTextureRegion.getWidth()));

float x = 0, y = 0;
//	for (int r = 0; r < 2 ; r++){ // row

x = 600; 
y = CAMERA_HEIGHT - player.getWidthScaled();

Sprite special;

Random r = new Random();
int val = r.nextInt(4);
int dir = 1;
if(val%2 ==0){dir = -1;}


	if(val > 2){
		special = new Sprite ( x, y, mPlayerTextureRegion.deepCopy());
				special.setZIndex(40);
	}// end of if(val)

	else{
		special = new Sprite ( x, y, mTargetTextureRegion.deepCopy());
		special.setZIndex(41);
	
		special.registerEntityModifier(
				
			                new RotationModifier(
			                    10,                 // duration
			                    0,                  // angle start                
			                    -360*dir                // angle end
			                )
				);
		special.registerEntityModifier(
				 new MoveXModifier(4,special.getX(),new Random().nextInt(CAMERA_WIDTH-(int)special.getWidthScaled()))
				);
		
	}// end of if(val)
	
	
mMainScene.attachChild(special);
MoveYModifier mod = new MoveYModifier(4,special.getY(),-1);
special.registerEntityModifier(mod.deepCopy());

specialsToBeAdded.add(special);

//}// end col


}// add special

	
	// a Time Handler for spawning targets, triggers every 1 second
	private void createSpriteSpawnTimeHandler() {
		TimerHandler spriteTimerHandler;
		float mEffectSpawnDelay = 1f;

		spriteTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {

            //   addTarget();
               addspecial();
           
               // creating special sprite with prob 1/25
               Random rand = new Random();
               boolean val = rand.nextInt(25)==0;
               if(val && false)
            	   {addspecial();}
					}
				});

		getEngine().registerUpdateHandler(spriteTimerHandler);
	}

	/* to restart the game and clear the whole screen */
	public void restart() {

		runOnUpdateThread(new Runnable() {

			@Override
			// to safely detach and re-attach the sprites
			public void run() {
				mMainScene.detachChildren();
				mMainScene.attachChild(player, 0);
				mMainScene.attachChild(score);
			}
		});

		// resetting everything
		hitCount = 0;
		change_text(score, hitCount);
		//score.setText(String.valueOf(hitCount));
		projectileLL.clear();
		projectilesToBeAdded.clear();
		TargetsToBeAdded.clear();
		targetLL.clear();
		
		specialsToBeAdded.clear();
		specialLL.clear();
	}

	@Override
	// pauses the music and the game when the game goes to the background
	protected void onPause() {
		if (runningFlag) {
			pauseMusic();
			if (mEngine.isRunning()) {
				pauseGame();
				pauseFlag = true;
			}
		}
		super.onPause();
	}

	
	@Override
	public void onResumeGame() {
		super.onResumeGame();
		// shows this Toast when coming back to the game
		if (runningFlag) {
			if (pauseFlag) {
				pauseFlag = false;
				Toast.makeText(this, "Menu button to resume",
						Toast.LENGTH_SHORT).show();
			} else {
				// in case the user clicks the home button while the game on the
				// resultScene
				resumeMusic();
				mEngine.stop();
			}
		} else {
			runningFlag = true;
		}
	}
	
	

	public void pauseMusic() {
		if (runningFlag)
			if (backgroundMusic.isPlaying())
				backgroundMusic.pause();
	}

	public void resumeMusic() {
		if (runningFlag)
			if (!backgroundMusic.isPlaying())
				backgroundMusic.resume();
	}

	public void fail() {
		if (mEngine.isRunning()) {
			winSprite.setVisible(false);
			failSprite.setVisible(true);
			mMainScene.setChildScene(mResultScene, false, true, true);
			mEngine.stop();
			finish();
		}
	}

	public void win() {
		if (mEngine.isRunning()) {
			failSprite.setVisible(false);
			winSprite.setVisible(true);
			mMainScene.setChildScene(mResultScene, false, true, true);
			mEngine.stop();
		}
	}

	public void pauseGame() {
		if (runningFlag) {
			mMainScene.setChildScene(mPauseScene, false, true, true);
			mEngine.stop();
		}
	}
	
	public void unPauseGame(){
		mMainScene.clearChildScene();
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		// if menu button is pressed
		if (pKeyCode == KeyEvent.KEYCODE_MENU
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if (mEngine.isRunning() && backgroundMusic.isPlaying()) {
				pauseMusic();
				pauseFlag = true;
				pauseGame();
				Toast.makeText(this, "Menu button to resume",
						Toast.LENGTH_SHORT).show();
			} else {
				if (!backgroundMusic.isPlaying()) {
					unPauseGame();
					pauseFlag = false;
					resumeMusic();
					mEngine.start();
				}
				return true;
			}
			// if back key was pressed
		} else if (pKeyCode == KeyEvent.KEYCODE_BACK
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {

			if (!mEngine.isRunning() && backgroundMusic.isPlaying()) {
				mMainScene.clearChildScene();
				mEngine.start();
				restart();
				return true;
			}
			return super.onKeyDown(pKeyCode, pEvent);
		}
		return super.onKeyDown(pKeyCode, pEvent);
	}
	
	////////////////////////////////
	///////////////////////////////
	public void change_text(ChangeableText text2change, int value)
	{
		text2change.setText(String.valueOf(value));
	}
	
	public  float[] rand_color_code(){
		
		
		float[] array = {0.09804f,1f,0.6274f};
		
		return array;
	}// end rand_color_code
}

// for time handler
// http://www.andengine.org/forums/tutorials/using-timer-s-sprite-spawn-example-t463.html