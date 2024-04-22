package com.example.dinodino;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class DinoGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture imgBackground, imgDino, imgStone;
	int stonePositionX, dinoPositionY;
	boolean dinoJump, dinoFall;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		imgBackground = new Texture("dino.jpg");
		imgDino = new Texture("dino.png");
		imgStone = new Texture("rock.png");

		stonePositionX = Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 40;
		dinoPositionY = 0;
		dinoJump = false;
		dinoFall = false;

	}

	@Override
	public void render () {

		moveOfStone();
		jumpDino();
		startPosition();
	}

	private void startPosition() {
		batch.begin();
		batch.draw(imgBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(imgDino, 0, dinoPositionY, Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 2);
		batch.draw(imgStone, stonePositionX, 0, Gdx.graphics.getWidth() / 5,
				Gdx.graphics.getHeight() / 4);

		batch.end();
	}

	private void jumpDino() {
		if (Gdx.input.justTouched()) {
			if (dinoPositionY == 0) {
				dinoJump = true;
			}
		}

		if (dinoJump && dinoPositionY < Gdx.graphics.getHeight() / 3) {
			dinoPositionY += Gdx.graphics.getWidth() / 100;
		}

		if (dinoPositionY > Gdx.graphics.getHeight() / 3) {
			dinoJump = false;
			dinoFall = true;
		}

		if (dinoPositionY == 0) {
			dinoJump = false;
			dinoFall = false;
		}

		if (dinoFall && dinoPositionY > 0) {
			dinoPositionY -= Gdx.graphics.getWidth() / 100;
		}
	}

	private void moveOfStone() {
		if (stonePositionX < - Gdx.graphics.getWidth() / 5) {
			stonePositionX = Gdx.graphics.getWidth();
		}
		stonePositionX = stonePositionX - Gdx.graphics.getWidth() / 100;
	}

	@Override
	public void dispose () {
		batch.dispose();
		imgBackground.dispose();
	}
}
