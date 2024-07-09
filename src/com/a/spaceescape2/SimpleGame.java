package com.a.spaceescape2;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.MoveXModifier;
import org.anddev.andengine.entity.modifier.MoveYModifier;
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.ease.EaseBackIn;
import org.anddev.andengine.util.modifier.ease.EaseBounceOut;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.Toast;

public class SimpleGame extends BaseGameActivity implements
		IOnSceneTouchListener {

	private Camera mCamera;

	// This one is for the font
	private BitmapTextureAtlas mFontTexture;
	private Font mFont;
	private ChangeableText score;

	// this one is for all other textures
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mPlayerTextureRegion;
	private TextureRegion mProjectileTextureRegion;
	private TextureRegion mTargetTextureRegion;
	private TextureRegion mPausedTextureRegion;
	private TextureRegion mWinTextureRegion;
	private TextureRegion mFailTextureRegion;

	// for the ninja
	private TiledTextureRegion mFaceTextureRegion;

	private static int CAMERA_WIDTH = 720;
	private static int CAMERA_HEIGHT = 480;
	private static final float DEMO_VELOCITY = 100.0f;

	//
	private BitmapTextureAtlas mBitmapTextureAtlas2;

	//
	// **
	private TextureRegion mFaceTextureRegion_box;
	private BitmapTextureAtlas mBitmapTextureAtlas_box;
	private Sprite bomb;

	private int dir = 0;
	// private AnimatedSprite ball;

	private DigitalOnScreenControl mDigitalOnScreenControl;
	private BitmapTextureAtlas mOnScreenControlTexture;
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;

	private AnimatedSprite explosion;
	private BitmapTextureAtlas explosionBitmapTextureAtlas;
	private TiledTextureRegion mexplosionTextureRegion;

	private int number_of_blocks = 800;
	private int number_of_bombtarget = 1;

	private float scale_factor;

	private float centerX;
	private float centerY;

	private boolean move = true;

	// **

	// /=====
	private BitmapTextureAtlas splashTA;
	private TextureRegion splashTR;

	// ///===

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
	// added
	private LinkedList<AnimatedSprite> Bomb_targets;
	private LinkedList<AnimatedSprite> Bomb_targetsToBeAdded;
	// /

	private Sound shootingSound;
	private Music backgroundMusic;
	private boolean runningFlag = false;
	private boolean pauseFlag = false;
	private CameraScene mPauseScene;
	private CameraScene mResultScene;
	private int hitCount;
	private final int maxScore = 10000;

	private BitmapTextureAtlas mBitmapTextureAtlas_tele;

	private TextureRegion mteleporterTextureRegion;

	private float t343twidth;
	private int col = 28;
	private int row = 18;

	private Sprite teleport_1;

	private Sprite teleport_2;
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
		return new Engine(
				new EngineOptions(true, ScreenOrientation.LANDSCAPE,
						new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
						mCamera).setNeedsSound(true).setNeedsMusic(true));
	}

	@Override
	public void onLoadResources() {
		// prepare a container for the image
		mBitmapTextureAtlas = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// prepare a container for the font
		mFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// loading the image inside the container
		mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "ship.png",
						0, 0);
		mProjectileTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"Projectile.png", 64, 0);
		mTargetTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "box.png",
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

		mBitmapTextureAtlas_tele = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mteleporterTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas_tele, this,
						"face_box.png", 128, 0);

		// explosion
		explosionBitmapTextureAtlas = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mexplosionTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.explosionBitmapTextureAtlas, this,
						"explosion_new.png", 0, 0, 5, 5);

		mEngine.getTextureManager().loadTexture(explosionBitmapTextureAtlas);
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas_tele);

		// ///

		// preparing the font
		mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT,
				Typeface.BOLD), 40, true, Color.YELLOW);

		SoundFactory.setAssetBasePath("mfx/");
		try {
			shootingSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "Bomb.mp3");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MusicFactory.setAssetBasePath("mfx/");

		try {
			backgroundMusic = MusicFactory.createMusicFromAsset(
					mEngine.getMusicManager(), this, "background_music.wav");
			backgroundMusic.setLooping(true);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ////////////////////////////////////////
		mBitmapTextureAtlas2 = new BitmapTextureAtlas(64, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas2, this,
						"flag2.png", 0, 0, 2, 1);

		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas2);
		// /////////////////////////////////////////

		// **
		mFaceTextureRegion_box = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "bomb.png", 0,
						0);

		mOnScreenControlTexture = new BitmapTextureAtlas(256, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"onscreen_control_base.png", 0, 0);
		mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"onscreen_control_knob.png", 128, 0);

		mEngine.getTextureManager().loadTexture(mOnScreenControlTexture);
		// **

		// loading textures in the engine
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		mEngine.getTextureManager().loadTexture(mFontTexture);
		mEngine.getFontManager().loadFont(mFont);
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
		mMainScene.setBackground(new ColorBackground(0f, 0f, 0f));
		mMainScene.setOnSceneTouchListener(this);

		// set coordinates for the player
		final int PlayerX = this.mPlayerTextureRegion.getWidth() / 2;
		final int PlayerY = (int) ((mCamera.getHeight() - mPlayerTextureRegion
				.getHeight()) / 2);

		// set the player on the scene
		player = new Sprite(PlayerX, PlayerY, mPlayerTextureRegion);
		player.setScale(1);

		// initializing variables
		projectileLL = new LinkedList<Sprite>();
		targetLL = new LinkedList<Sprite>();
		projectilesToBeAdded = new LinkedList<Sprite>();
		TargetsToBeAdded = new LinkedList<Sprite>();

		// /// addeddd
		Bomb_targets = new LinkedList<AnimatedSprite>();
		Bomb_targetsToBeAdded = new LinkedList<AnimatedSprite>();
		// //

		// settings score to the value of the max score to make sure it appears
		// correctly on the screen
		score = new ChangeableText(0, 0, mFont, String.valueOf(maxScore));
		// repositioning the score later so we can use the score.getWidth()
		score.setPosition(mCamera.getWidth() - score.getWidth() - 5, 5);

		mMainScene.attachChild(score);

		// createSpriteSpawnTimeHandler();

		mMainScene.registerUpdateHandler(detect);

		// starting background music
		// backgroundMusic.play();
		// runningFlag = true;

		// /////////////////////////////////////////////////////////
		centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		/* Funny explosion. */
		explosion = new AnimatedSprite(centerX, 410,
				this.mexplosionTextureRegion);

		explosion.setVisible(false);
		// explosion.animate(150);
		explosion.setScale(9);

		mMainScene.attachChild(explosion);

		// ******

		final Sprite face = new Sprite(0, 0,
				this.mFaceTextureRegion_box);
		
		 final PhysicsHandler physicsHandler = new PhysicsHandler(face);
         face.registerUpdateHandler(physicsHandler);

		mDigitalOnScreenControl = new DigitalOnScreenControl(20, CAMERA_HEIGHT
				- this.mOnScreenControlBaseTextureRegion.getHeight() - 20,
				this.mCamera, this.mOnScreenControlBaseTextureRegion,
				this.mOnScreenControlKnobTextureRegion, 0.1f,
				new IOnScreenControlListener() {
					@Override
					public void onControlChange(
							final BaseOnScreenControl pBaseOnScreenControl,
							final float pValueX, final float pValueY) {
						// 1 = moving right
						// 2 = left
						// 3 = down
						// 4 = up

						
					//	 physicsHandler.setVelocity(pValueX * 100, pValueY * 100);
						
						
						if (pValueX == 1 && move) {
						//bomb.registerEntityModifier(new MoveXModifier(1f,bomb.getX(), CAMERA_WIDTH));
							dir = 1;
							bomb.setRotation(90);
							move = false;
						} else if (pValueX == -1 && move) {
						//	bomb.registerEntityModifier(new MoveXModifier(1f,bomb.getX(), -1));
							dir = 2;
							bomb.setRotation(-90);
							move = false;
						} else if (pValueY == 1 && move) {
							//bomb.registerEntityModifier(new MoveYModifier(1f,bomb.getY(), CAMERA_HEIGHT + 1));
							dir = 3;
							bomb.setRotation(180);
							move = false;
						} else if (pValueY == -1 && move) {
						//	bomb.registerEntityModifier(new MoveYModifier(1f,bomb.getY(), -1));
							dir = 4;
							bomb.setRotation(0);
							move = false;
						}


					}
					

				});
		mDigitalOnScreenControl.getControlBase().setBlendFunction(
				GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mDigitalOnScreenControl.getControlBase().setAlpha(0.5f);
		mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		mDigitalOnScreenControl.getControlBase().setScale(1.25f);
		mDigitalOnScreenControl.getControlKnob().setScale(1.25f);
		mDigitalOnScreenControl.refreshControlKnobPosition();

		mMainScene.setChildScene(this.mDigitalOnScreenControl);

		// / testing for collision off the screen

		// ***
		
		face.registerEntityModifier(new MoveXModifier(30, 0,CAMERA_WIDTH));
		
		

		mMainScene.attachChild(face);
		bomb = face;
		scale_factor = Math.min((CAMERA_HEIGHT / (bomb.getHeight() * row)),
				(CAMERA_WIDTH / (bomb.getHeight() * col)));
		
		face.setScale(scale_factor); // scaling face
		t343twidth = bomb.getWidthScaled();
		mMainScene.registerTouchArea(face);
		mMainScene.setTouchAreaBindingEnabled(true);
		// /*****

		
		
		bomb_target();
		createSpriteSpawnTimeHandler();
		addTarget();
	//	addteteporter();
		// restart();
		return mMainScene;
	}
	
	
	// adds a target at a random location and let it move along the x-axis
		public void addTarget_new() {
			Random rand = new Random();

			int x = (int) mCamera.getWidth() + mTargetTextureRegion.getWidth();
			int minY = mTargetTextureRegion.getHeight();
			int maxY = (int) (mCamera.getHeight() - mTargetTextureRegion
					.getHeight());
			int rangeY = maxY - minY;
			int y = rand.nextInt(rangeY) + minY;

			for (int i = 2; i<10;i++)
			{
			Sprite target = new Sprite(x, i,  mTargetTextureRegion.deepCopy());
			target.setScale(scale_factor);
			mMainScene.attachChild(target);

			int minDuration = 20;
			int maxDuration = 40;
			int rangeDuration = maxDuration - minDuration;
			int actualDuration = rand.nextInt(rangeDuration) + minDuration;

			MoveXModifier mod = new MoveXModifier(30, target.getX(),
					-target.getWidth());
			target.registerEntityModifier(mod.deepCopy());

			TargetsToBeAdded.add(target);
			}// end for

		}

	public void addteteporter() {
		float x, y;

		x = centerX + t343twidth * -4;
		y = centerY - t343twidth * 0;
		teleport_1 = new Sprite(x, y, this.mteleporterTextureRegion.deepCopy());

		x = centerX + t343twidth * 13;
		y = centerY + t343twidth * -2;

		teleport_2 = new Sprite(x, y, this.mteleporterTextureRegion.deepCopy());

		teleport_1.setScale(scale_factor);
		teleport_2.setScale(scale_factor);

		mMainScene.attachChild(teleport_1);
		mMainScene.attachChild(teleport_2);

	}

	private void bomb_target() {

		float x = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		float y = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;

		// ball = new AnimatedSprite(centerX -
		// this.mFaceTextureRegion.getHeight(), 410 , this.mFaceTextureRegion);
		// final PhysicsHandler physicsHandler = new PhysicsHandler(ball);
		// ball.registerUpdateHandler(physicsHandler);
		// physicsHandler.setVelocity(DEMO_VELOCITY, DEMO_VELOCITY);
		// ball.animate(100);
		// ball.setScale(scale_factor);
		// mMainScene.attachChild(ball);

		for (int i = 0; i < number_of_bombtarget; i++) {

			switch (i) {
			case 0:
				x = 200;
				y = 410 - bomb.getWidthScaled();
				break;

			case 1:
				x = CAMERA_WIDTH / 2 - bomb.getWidthScaled();
				y = CAMERA_HEIGHT - 200;
				break;

			case 3:
				x = (CAMERA_WIDTH / 2) + bomb.getWidthScaled();
				y = (CAMERA_HEIGHT / 4) - bomb.getHeightScaled();
				break;

			case 5:
				x = (CAMERA_WIDTH / 2);
				y = (CAMERA_HEIGHT / 4) - bomb.getHeightScaled();
				break;

			case 4:
				x = CAMERA_WIDTH / 2 - bomb.getWidthScaled();
				y = CAMERA_HEIGHT - 200 + bomb.getHeightScaled();
				break;

			case 6:
				x = CAMERA_WIDTH / 2;
				y = CAMERA_HEIGHT - 200 + bomb.getHeightScaled();
				break;
			}

			AnimatedSprite bomb_target = new AnimatedSprite(x, y,
					this.mFaceTextureRegion.deepCopy());

			bomb_target.animate(100);
			bomb_target.setScale(scale_factor);
			mMainScene.attachChild(bomb_target);
			Bomb_targetsToBeAdded.add(bomb_target);

		}// bomb_target
	}

	@Override
	public void onLoadComplete() {
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// gggggggggggggggggggg
	// gggggggggggggggggggg
	// ////////////////////////////////////

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
				// IMPRORTANT
				if (_target.getX() <= -_target.getWidth()) {
					removeSprite(_target, targets);
					fail();
					break;
				}

				// checking for target collision with new-player
				if (bomb.collidesWith(_target)) {

					float x_dir = bomb.getX(), y_dir = bomb.getY();

					bomb.clearEntityModifiers();

					// 1 = moving left 2 right
					// 2 = left
					// 3 = down
					// 4 = up

					switch (dir) {
					case 1:
						x_dir = _target.getX() - bomb.getWidthScaled() - 1;
						break;
					case 2:
						x_dir = _target.getX() + _target.getWidthScaled() + 1;
						break;
					case 3:
						y_dir = _target.getY() - _target.getHeightScaled() - 1;
						break;
					case 4:
						y_dir = _target.getY() + _target.getHeightScaled() + 1;
						break;
					}

					// ///=========================================

					bomb.setPosition(x_dir, y_dir);
					// bomb.setPosition(_target.getX() - new_width-1,
					// bomb.getY());
					move = true;

				}



				
				

				// ///=========================================

				Iterator<Sprite> projectiles = projectileLL.iterator();
				Sprite _projectile;
				// iterating over all the projectiles (bullets)
				while (projectiles.hasNext()) {
					_projectile = projectiles.next();

					// if the targets collides with a projectile, remove the
					// projectile and set the hit flag to true
					if (_target.collidesWith(_projectile)) {
						removeSprite(_projectile, projectiles);
						hit = true;
						break;
					}
					
					if(_projectile.getX() > CAMERA_WIDTH)
						{_projectile.detachSelf();
						removeSprite(_projectile,projectiles);
						}
					
					

				}

				// if a projectile hit the target, remove the target, increment
				// the hit count, and update the score
				if (hit) {
					removeSprite(_target, targets);
					hit = false;
					hitCount++;
					score.setText(String.valueOf(hitCount));
				}
			}

			// if max score , then we are done
			if (hitCount >= maxScore) {

				mMainScene.reset();
				// mMainScene = createSplashScene();
				win();
			}

			// testing if the new player is off the screen horizontal
			if (bomb.getWidth() + bomb.getX() > CAMERA_WIDTH || bomb.getX() < 0
					|| bomb.getY() + bomb.getHeight() > CAMERA_HEIGHT
					|| bomb.getY() < 0) {
				// bomb.clearEntityModifiers();
				bomb.setPosition(0,bomb.getY()*2);
				move = true;
				// bomb.registerEntityModifier(new MoveModifier(1f, bomb.getX(),
				// bomb.getX(),bomb.getY(), bomb.getY(),
				// EaseBounceOut.getInstance()) );
			}

			Iterator<AnimatedSprite> bomb_target_link = Bomb_targets.iterator();
			AnimatedSprite ball;

			// 34 iterating over all the projectiles (bullets)
			while (bomb_target_link.hasNext()) {
				ball = bomb_target_link.next();

				// if bomb collides with ball
				if (bomb.collidesWith(ball))

				{

					bomb.setInitialPosition();
					ball.setInitialPosition();
					bomb.detachSelf();
					mDigitalOnScreenControl.detachSelf();
					mDigitalOnScreenControl.setVisible(false);
					ball.detachSelf();

					explosion.animate(100);

					// plays a shooting sound
					shootingSound.play();

					explosion.setCurrentTileIndex(0);
					explosion.setVisible(true);

					// 7 this to add the falling effects
					Iterator<Sprite> projectiles = targetLL.iterator();
					Sprite _proj;
					// iterating over all the projectiles (bullets)
					while (projectiles.hasNext()) {
						_proj = projectiles.next();
						// _proj.registerEntityModifier(new MoveModifier(1f,
						// _proj.getX(), _proj.getX(),_proj.getY(),
						// CAMERA_HEIGHT - _proj.getHeight(),
						// EaseBounceOut.getInstance()) );
						mMainScene.detachChild(_proj);
					}
				}
				// /7
				// checking for target collision with teleport

				// 1 = moving left 2 right
				// 2 = left
				// 3 = down
				// 4 = up

				
				if (bomb.collidesWith(teleport_2)) {
					float x_dir = teleport_1.getScaleX(); 
						float y_dir = teleport_1.getScaleY();
					
									
					switch (dir) {
					case 1:
						x_dir = teleport_1.getScaleX() + bomb.getWidthScaled() + 1;
						
						bomb.clearEntityModifiers();
						bomb.setPosition(x_dir, y_dir);
              	    	bomb.registerEntityModifier(new MoveXModifier(1f, x_dir, CAMERA_WIDTH));
						
						break;
					case 2:
						x_dir = teleport_1.getScaleX() - teleport_1.getWidthScaled() - 1;
						bomb.clearEntityModifiers();
						bomb.setPosition(x_dir, y_dir);
              	    	bomb.registerEntityModifier(new MoveXModifier(1f,x_dir, -1));
						
						break;
					case 3:
						y_dir = teleport_1.getScaleY() + teleport_1.getHeightScaled() + 1;
						bomb.clearEntityModifiers();
						bomb.setPosition(x_dir, y_dir);
              	    	bomb.registerEntityModifier(new MoveYModifier(1f, y_dir, CAMERA_HEIGHT));
						
						break;
					case 4:
						y_dir = teleport_1.getScaleY() - teleport_1.getHeightScaled() - 1;

						bomb.clearEntityModifiers();
						bomb.setPosition(x_dir, y_dir);
              	    	bomb.registerEntityModifier(new MoveYModifier(1f, y_dir, -1));
						
						break;
					}
					
					//bomb.setPosition(x_dir, y_dir);
					
					//bomb.setPosition(x_dir, y_dir);
					
					// break;

				}


				if (bomb.collidesWith(teleport_1)) {

					float x_dir = teleport_2.getScaleX(); 
					float y_dir = teleport_2.getScaleY();
					//x = centerX + t343twidth * 13;
					//y = centerY + t343twidth * -2;
					//bomb.clearEntityModifiers();
					
					switch (dir) {
					case 1:
						x_dir = teleport_2.getScaleX() + bomb.getWidthScaled() + 1;
						
						bomb.clearEntityModifiers();
						bomb.setPosition(x_dir, y_dir);
              	    	bomb.registerEntityModifier(new MoveXModifier(1f, x_dir, CAMERA_WIDTH));
						
						break;
					case 2:
						x_dir = teleport_2.getScaleX() - bomb.getWidthScaled() - 1;
					    bomb.clearEntityModifiers();
						bomb.setPosition(x_dir, centerY + t343twidth * -2);
              	   // 	bomb.registerEntityModifier(new MoveXModifier(1f, x_dir, -1));
						
						break;
					case 3:
						y_dir = teleport_2.getScaleY() + teleport_2.getHeightScaled() + 1;
						bomb.clearEntityModifiers();
						bomb.setPosition(x_dir, y_dir);
              	    	bomb.registerEntityModifier(new MoveYModifier(1f, y_dir, CAMERA_HEIGHT));
						
						break;
					case 4:
						y_dir = teleport_2.getScaleY() - teleport_2.getHeightScaled() - 1;

						bomb.clearEntityModifiers();
						bomb.setPosition(x_dir, y_dir);
              	    	bomb.registerEntityModifier(new MoveYModifier(1f, y_dir, -1));
						
						break;
					}

					
				
				//	
				}

				if (explosion.getCurrentTileIndex() == 24
						&& explosion.isVisible()) {

					win();
				}
			} // 34
			

			// a work around to avoid ConcurrentAccessException
			projectileLL.addAll(projectilesToBeAdded);
			projectilesToBeAdded.clear();

			Bomb_targets.addAll(Bomb_targetsToBeAdded);
			Bomb_targetsToBeAdded.clear();

			targetLL.addAll(TargetsToBeAdded);
			TargetsToBeAdded.clear();
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
		//

		// trying to see if swipe can be done on screen
		float firstX = 0, firstY = 0;

		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			firstX = pSceneTouchEvent.getX(); // remember the x, on the first
												// touch
			firstY = pSceneTouchEvent.getY(); // remember the x, on the first
												// touch
			
			
		}
		
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE || pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			firstX = pSceneTouchEvent.getX(); 
			firstY = pSceneTouchEvent.getY(); 

		//	bomb.setPosition(bomb.getScaleX(), firstY);
		shootProjectile(bomb, firstX, firstY);
		}
		// this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2,
		// pSceneTouchEvent.getY() - this.getHeight() / 2);
		return true;
		// ////
	}

	/* shoots a projectile from the player's position along the touched area */
	private void shootProjectile(Sprite player, final float pX, final float pY) {

		int offX = (int) (pX - player.getX());
		int offY = (int) (pY - player.getY());
		//if (offX <= 0)
		//return;

		final Sprite projectile;
		// position the projectile on the player
		projectile = new Sprite(player.getX(), player.getY(),
				mProjectileTextureRegion.deepCopy());

		projectile.setScale(scale_factor);
		 
		mMainScene.attachChild(projectile, 1);

		int realX = (int) (mCamera.getWidth() + projectile.getWidth() / 2.0f);
		float ratio = (float) offY / (float) offX;
		int realY = (int) ((realX * ratio) + projectile.getY());

		int offRealX = (int) (realX - projectile.getX());
		int offRealY = (int) (realY - projectile.getY());
		float length = (float) Math.sqrt((offRealX * offRealX)
				+ (offRealY * offRealY));
		float velocity = 480.0f / 1.0f; // 480 pixels / 1 sec
		float realMoveDuration = length / velocity;

		// defining a move modifier from the projectile's position to the
		// calculated one
		MoveModifier mod = new MoveModifier(realMoveDuration,
				projectile.getX(), realX, projectile.getY(), realY);
		
		MoveModifier mod2 = new MoveModifier(realMoveDuration,
				projectile.getX(), projectile.getX(), projectile.getY(), CAMERA_HEIGHT);
		
		projectile.registerEntityModifier(mod2.deepCopy());

		projectilesToBeAdded.add(projectile);
		// plays a shooting sound
		// shootingSound.play();
	}

	// adds a target at a random location and let it move along the x-axis
	public void addTarget() {

		float x; // = (int) mCamera.getWidth() +
					// mTargetTextureRegion.getWidth();
		// int minY = mTargetTextureRegion.getHeight();
		// int maxY = (int) (mCamera.getHeight() -
		// mTargetTextureRegion.getHeight());
		// int rangeY = maxY - minY;
		float y; // = rand.nextInt(rangeY) + minY;

		// **
		x = (CAMERA_WIDTH - 60);
		y = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
		// **

		Sprite target2 = new Sprite(x, y, mTargetTextureRegion.deepCopy());

		// mMainScene.attachChild(target2);

		// target.registerEntityModifier(mod.deepCopy());
		target2.setScale(scale_factor);
		// TargetsToBeAdded.add(target2);

		for (int i = 0; i < number_of_blocks; i++) {

			// x = 60 ;
			// y = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) /2 ;

			switch (i) {
			case 0:
				x = 470;
				y = 410;// (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight())
						// /2 ;
				break;

			case 1:
				x = 470 + target2.getWidthScaled();
				y = 410;
				break;

			case 2:
				// x = x ;
				y = 410 - target2.getWidthScaled();

				// ball.setPosition(200 , y);
				break;

			case 3:

				y = 410 - target2.getWidthScaled() * 2;
				break;

			case 4:
				// x = 100*i; y = (CAMERA_HEIGHT -
				// this.mFaceTextureRegion.getHeight()) - 60;

				y = 410 - target2.getWidthScaled() * 3;
				break;

			case 5:
				x = 460 - target2.getWidthScaled();
				y = 22 + target2.getWidthScaled() * 3;
				break;
			case 6:
				x = (CAMERA_WIDTH - 60 - target2.getWidthScaled());
				y = 22 + target2.getWidthScaled() * 2;
				break;
			case 7:
				x = (CAMERA_WIDTH - 60);
				y = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
				break;

			case 8:
				x = x - target2.getWidthScaled() * 3;
				break;
			}

			// x = 100 ;
			// y = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) /2 ;

			// x = 60*i; // (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth())
			// / 2;
			// ;
			Sprite target = new Sprite(x, y, mTargetTextureRegion.deepCopy());

			mMainScene.attachChild(target);

			target.setScale(scale_factor);
			TargetsToBeAdded.add(target);
		}// for example

	}

	// a Time Handler for spawning targets, triggers every 1 second
	private void createSpriteSpawnTimeHandler() {
		TimerHandler spriteTimerHandler;
		float mEffectSpawnDelay = 0.5f;

		spriteTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {

					//	 addTarget_new();
					//	explosion.setVisible(false);
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
		score.setText(String.valueOf(hitCount));
		projectileLL.clear();
		projectilesToBeAdded.clear();
		TargetsToBeAdded.clear();
		Bomb_targetsToBeAdded.clear();
		Bomb_targets.clear();
		targetLL.clear();
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
		}
	}

	public void win() {
		if (mEngine.isRunning()) {

			failSprite.setVisible(false);
			winSprite.setVisible(true);
			mMainScene.setChildScene(mResultScene, false, true, true);

			mEngine.stop();
			finish();
			startActivity(new Intent(this, stage_2.class));

		}
	}

	public void pauseGame() {
		if (runningFlag) {
			mMainScene.setChildScene(mPauseScene, false, true, true);
			mEngine.stop();
		}
	}

	public void unPauseGame() {
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
}

// for time handler
// http://www.andengine.org/forums/tutorials/using-timer-s-sprite-spawn-example-t463.html