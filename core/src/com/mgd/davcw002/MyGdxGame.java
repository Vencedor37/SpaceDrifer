package com.mgd.davcw002;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	private float startRange = 300;
	Box2DDebugRenderer debugRenderer;
	
	@Override
	public void create () {
		allEntities = new ArrayList<Entity>();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		world = new World(new Vector2(0,0), true);
		debugRenderer = new Box2DDebugRenderer();
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
		player.setBaseSprite(new Sprite(new Texture("sprites/basic_player.png")));
		player.setBody(world, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		player.setLocation(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		player.setSize(80, 80, 5);

		allEntities.add(player);
		initialiseEnemies();
		initialiseRewards();

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		Gdx.input.setInputProcessor(this);
		shapeRenderer = new ShapeRenderer();
	}

	private void initialiseEnemies() {
		enemies = new ArrayList<Enemy>();
		int numberEnemies = 10;
		Random random = new Random();

		for (int i = 0; i < numberEnemies; i ++) {
			Enemy currentEnemy = new Enemy();
			float positionX = 0;
			float positionY = 0;
			float leftBoundary = player.getCentreX() - startRange;
			float rightBoundary = player.getCentreX() + startRange;
			float topBoundary = player.getCentreY() - startRange;
			float bottomBoundary = player.getCentreY() + startRange;
			boolean validLocationFound = false;

			currentEnemy.setBaseSprite(new Sprite(new Texture("sprites/basic_enemy.png")));

			while (!validLocationFound) {
				boolean left = random.nextBoolean();
				if (left) {
					positionX = (float) Math.random() * leftBoundary;
				} else {
					positionX = (float) (rightBoundary + (Math.random() * Gdx.graphics.getWidth()));
				}

				boolean top = random.nextBoolean();
				if (top) {
					positionY = (float) Math.random() * topBoundary;
				} else {
					positionY = (float) (bottomBoundary + (Math.random() * Gdx.graphics.getHeight()));
				}

				currentEnemy.setBody(world, positionX, positionY);
				currentEnemy.setSize(100, 100, 10);
				currentEnemy.setLocation(positionX, positionY);

				validLocationFound = true;
				for (Entity entity : allEntities) {
					Rectangle r1 = currentEnemy.getBaseSprite().getBoundingRectangle();
					Rectangle r2 = entity.getBaseSprite().getBoundingRectangle();
					if (r1.overlaps(r2)) {
						validLocationFound = false;
						break;
					}
				}
			}

			currentEnemy.addForce(new Vector2(positionX, positionY).scl(100));
			enemies.add(currentEnemy);
			allEntities.add(currentEnemy);
		}
	}

	private void initialiseRewards() {
		Random random = new Random();
		rewards = new ArrayList<Reward>();
		int numberRewards = 8;
		for (int i = 0; i < numberRewards; i ++) {
			Reward currentReward = new Reward();
			float positionX = 0;
			float positionY = 0;
			float leftBoundary = player.getCentreX() - startRange;
			float rightBoundary = player.getCentreX() + startRange;
			float topBoundary = player.getCentreY() - startRange;
			float bottomBoundary = player.getCentreY() + startRange;
			boolean validLocationFound = false;

			while (!validLocationFound) {
				boolean left = random.nextBoolean();
				if (left) {
					positionX = (float) Math.random() * leftBoundary + 100;
				} else {
					positionX = (float) (rightBoundary + (Math.random() * Gdx.graphics.getWidth())) - 100;
				}

				boolean top = random.nextBoolean();
				if (top) {
					positionY = (float) Math.random() * topBoundary + 100;
				} else {
					positionY = (float) (bottomBoundary + (Math.random() * Gdx.graphics.getHeight())) - 100;
				}
				currentReward.setBaseSprite(new Sprite(new Texture("sprites/basic_reward.png")));
				currentReward.setBody(world, positionX, positionY);
				currentReward.setLocation(positionX, positionY);
				currentReward.setSize(100, 100, 10);

				validLocationFound = true;
				for (Entity entity : allEntities) {
					Rectangle rect = currentReward.getBaseSprite().getBoundingRectangle();
					Rectangle otherRect = entity.getBaseSprite().getBoundingRectangle();
					boolean valid = (!rect.contains(otherRect));
					if (currentReward.getBaseSprite().getBoundingRectangle().overlaps(entity.getBaseSprite().getBoundingRectangle())) {
						validLocationFound = false;
					}
				}
			}

			currentReward.addForce(new Vector2(positionX, positionY).scl(180));
			rewards.add(currentReward);
			allEntities.add(currentReward);
		}
	}


	@Override
	public void render () {
		executeDraw();
		processLogic();
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

		float leftBoundary = player.getCentreX() - startRange;
		float rightBoundary = player.getCentreX() + startRange;
		float topBoundary = player.getCentreY() - startRange;
		float bottomBoundary = player.getCentreY() + startRange;
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.line(leftBoundary, 0, 0, leftBoundary, Gdx.graphics.getHeight(), 0);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.YELLOW);
		shapeRenderer.line(rightBoundary, 0, 0, rightBoundary, Gdx.graphics.getHeight(), 0);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.line(0, topBoundary, 0, Gdx.graphics.getWidth(), topBoundary, 0);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.line(0, bottomBoundary, 0, Gdx.graphics.getWidth(), bottomBoundary, 0);
		shapeRenderer.end();

		for (Entity e : allEntities) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(Color.CYAN);
			Rectangle r = e.getBaseSprite().getBoundingRectangle();
			shapeRenderer.box(r.x, r.y, 0, r.width, r.height, 0);
			shapeRenderer.end();
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
