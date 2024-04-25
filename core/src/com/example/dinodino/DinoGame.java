package com.example.dinodino;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Map;
import java.util.Random;

public class DinoGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture imgBackground, imgDino, imgStone;
    int stonePositionX, dinoPositionY;
    boolean dinoJump, dinoFall, gameIsRunning, gameIsOver;
	ShapeRenderer stoneShapeRender, dinoShapeRender;
    int speed, points;
	Rectangle stoneRectangle, dinoRectangle;
	BitmapFont font;
	BitmapFont.BitmapFontData fontData;

    @Override
    public void create() {
        batch = new SpriteBatch();
        imgBackground = new Texture("dino.jpg");
        imgDino = new Texture("dino.png");
        imgStone = new Texture("rock.png");
        speed = Gdx.graphics.getWidth() / 100;

        stonePositionX = Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4;
        dinoPositionY = 0;
        dinoJump = false;
        dinoFall = false;
        gameIsRunning = false;
		gameIsOver = false;
		points = 0;

		font = new BitmapFont();
		fontData = font.getData();
//
//		stoneShapeRender = new ShapeRenderer();
//		dinoShapeRender = new ShapeRenderer();
//

    }

    @Override
    public void render() {
        startPosition();
        moveOfStone();
        jumpDino();
        startOfTheGame();
		checkGameOver();
		scoringDisplay();
    }

	private void scoringDisplay() {
		if (gameIsRunning) {
			points++;
		}

		batch.begin();
		font.setColor(Color.RED);
		font.draw(batch, "" + points, Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 6), Gdx.graphics.getHeight() * 0.9f);
		fontData.setScale(4);
		batch.end();
	}

	private void checkGameOver() {
//
//		stoneShapeRender.begin(ShapeRenderer.ShapeType.Filled);
//		stoneShapeRender.setColor(1,0,0,1);
//		stoneShapeRender.rect(stonePositionX + Gdx.graphics.getWidth() / 50, 0, Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 6);
//		stoneShapeRender.end();
//

		stoneRectangle = new Rectangle(stonePositionX + Gdx.graphics.getWidth() / 50, 0, Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 6);

//
//		dinoShapeRender.begin(ShapeRenderer.ShapeType.Filled);
//		dinoShapeRender.setColor(1, 0, 0, 1);
//		dinoShapeRender.rect(Gdx.graphics.getWidth() / 8, dinoPositionY + Gdx.graphics.getHeight() / 25, Gdx.graphics.getWidth() / 20, Gdx.graphics.getHeight() / 5);
//		dinoShapeRender.end();
//
		dinoRectangle = new Rectangle(Gdx.graphics.getWidth() / 8, dinoPositionY + Gdx.graphics.getHeight() / 25, Gdx.graphics.getWidth() / 20, Gdx.graphics.getHeight() / 7);

		if (Intersector.overlaps(stoneRectangle, dinoRectangle)) {
			gameIsRunning = false;
			gameIsOver = true;
		}
	}

	private void startOfTheGame() {
        if (Gdx.input.justTouched() && !gameIsRunning && !gameIsOver) {
            gameIsRunning = true;
        }

		if (Gdx.input.justTouched() && gameIsOver) {
			gameIsOver = false;
			stonePositionX = Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4;
			dinoPositionY = 0;

			startPosition();
			points = 0;
		}
    }

    private void startPosition() {
        batch.begin();
        batch.draw(imgBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(imgDino, 0, dinoPositionY, Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 2);
        batch.draw(imgStone, stonePositionX, 0, Gdx.graphics.getWidth() / 6,
                Gdx.graphics.getHeight() / 4);

        batch.end();
    }

    private void jumpDino() {
        if (gameIsRunning) {
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
    }

    private void moveOfStone() {
        if (gameIsRunning) {
            if (stonePositionX < -Gdx.graphics.getWidth() / 5) {
                stonePositionX = Gdx.graphics.getWidth();
                speed = Gdx.graphics.getWidth() / (int) (Math.random() * 50 + 80);
            }
            stonePositionX = stonePositionX - speed;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        imgBackground.dispose();
    }
}
