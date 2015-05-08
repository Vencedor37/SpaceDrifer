package com.mgd.davcw002;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;
	private Player player;
	private List<Enemy> enemies;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private Vector2 touchStart;
	private Vector2 dragPoint;
	private Vector2 touchEnd;
	private boolean vectorDisplayed = true;
	boolean drawLine = false;
	private OrthographicCamera camera;
	private World world;
	private Vector2 forceToApply;
	private List<Entity> allEntities;
	private List<Reward> rewards;
	
	@Override
	public void create () {
		allEntities = new ArrayList<Entity>();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		world = new World(new Vector2(0,0), true);
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Body bodyA = contact.getFixtureA().getBody();
				Body bodyB = contact.getFixtureB().getBody();
				int removeIndex = -9;
				if (bodyA.equals(player.getBody())) {

					for (Reward reward : rewards) {
						if (bodyB.equals(reward.getBody())) {
							removeIndex = rewards.indexOf(reward);
						}
					}

				}

				if (bodyB.equals(player.getBody())) {
					for (Reward reward : rewards) {
						if (bodyA.equals(reward.getBody())) {
							removeIndex = rewards.indexOf(reward);
						}
					}
				}
				if (removeIndex != -9) {
					rewards.remove(removeIndex);
				}
			}

			@Override
			public void endContact(Contact contact) {

			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		});
		player = new Player();
		player.setBody(world, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		player.setBaseSprite(new Sprite("sprites/basic_player.png"));
		player.setSize(80, 80, 5);

		allEntities.add(player);
		initialiseEnemies();
		initialiseRewards();
		allEntities.addAll(enemies);
		batch = new SpriteBatch();

		font = new BitmapFont();
		font.setColor(Color.BLACK);
		Gdx.input.setInputProcessor(this);
		shapeRenderer = new ShapeRenderer();
	}

	private void initialiseEnemies() {
		enemies = new ArrayList<Enemy>();
		int numberEnemies = 20;

		for (int i = 0; i < numberEnemies; i ++) {
			Enemy currentEnemy = new Enemy();
			float positionX = (float) Math.random() * Gdx.graphics.getWidth();
			float positionY = (float) Math.random() * Gdx.graphics.getHeight();
			currentEnemy.setBody(world, positionX, positionY);
			currentEnemy.setBaseSprite(new Sprite("sprites/basic_enemy.png"));
			currentEnemy.setSize(100, 100, 10);

			currentEnemy.addForce(new Vector2(positionX, positionY).scl(100));
			enemies.add(currentEnemy);
		}
	}

	private void initialiseRewards() {
		rewards = new ArrayList<Reward>();
		int numberRewards = 5;
		for (int i = 0; i < numberRewards; i ++) {
			Reward currentReward = new Reward();
			float positionX = (float) Math.random() * Gdx.graphics.getWidth();
			float positionY = (float) Math.random() * Gdx.graphics.getHeight();
			currentReward.setBody(world, positionX, positionY);
			currentReward.setBaseSprite(new Sprite("sprites/basic_reward.png"));
			currentReward.setSize(100, 100, 10);

			currentReward.addForce(new Vector2(positionX, positionY).scl(180));
			rewards.add(currentReward);
		}
	}


	@Override
	public void render () {
		processLogic();
		executeDraw();
	}

	private void executeDraw() {
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		player.render(batch);
		for (Enemy enemy : enemies) {
			enemy.render(batch);
		}
		for (Reward reward : rewards) {
			reward.render(batch);
		}
		if (drawLine) {
			drawToPoint();
		}
	}

	private void processLogic() {
		if (forceToApply != null) {
			player.getBody().applyForceToCenter(forceToApply, true);
			forceToApply = null;
		}

		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		player.updateLocation();
		for (Enemy enemy : enemies) {
			enemy.updateLocation();
		}
		for (Reward reward : rewards) {
			reward.updateLocation();
		}
	}

	private void drawToPoint() {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.line(touchStart.x, touchStart.y, 0, dragPoint.x, dragPoint.y, 0, Color.WHITE, Color.WHITE);
		shapeRenderer.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (touchStart == null) {
			touchStart = new Vector2(screenX, screenY);
			dragPoint = new Vector2(screenX, screenY);
		}
		drawLine = (touchStart != null && dragPoint != null);
		touchEnd = null;

		return false;
	}

	private void log(String message) {
		Gdx.app.log("debug", message);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touchEnd = new Vector2(screenX, screenY);
		if (touchStart != null) {
			forceToApply = touchStart.sub(touchEnd).scl(30);
		}

		touchStart = null;
		dragPoint = null;
		drawLine = false;

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		dragPoint = new Vector2(screenX, screenY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
