package com.example.dinodino;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Map;
import java.util.Random;

public class DinoGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture imgBackground, imgDino, imgStone, imgBird;
    int stonePositionX, dinoPositionY;
    boolean dinoJump, dinoFall, gameIsRunning, gameIsOver, soundOffOrOn;
	ShapeRenderer stoneShapeRender, dinoShapeRender, birdShapeRender, dinoHeadShapeRender;
    int speed, points, positionOfBirdWings, correctionYBird, correctionYStone;
	Rectangle stoneRectangle, dinoRectangle;
    Circle birdCircle, dinoHeadCircle;
	BitmapFont font;
	BitmapFont.BitmapFontData fontData;

    TextureRegion[] textureRegionOfBird;

    Random randomBirdOrStone;
    Sound sound_jump, sound_end;

    @Override
    public void create() {
        batch = new SpriteBatch();
        sound_end = Gdx.audio.newSound(Gdx.files.internal("end.mp3"));
        sound_jump= Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
        positionOfBirdWings = 0;
        imgBackground = new Texture("dino.jpg");
        imgDino = new Texture("dino.png");
        imgStone = new Texture("rock.png");
        speed = Gdx.graphics.getWidth() / 100;
        imgBird = new Texture("IMG_3051.PNG");

        stonePositionX = Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4;
        dinoPositionY = 0;
        dinoJump = false;
        dinoFall = false;
        gameIsRunning = false;
		gameIsOver = false;
		points = 0;

		font = new BitmapFont();
		fontData = font.getData();

        textureRegionOfBird = new TextureRegion[20];
        setRegionImageOfBird();

		stoneShapeRender = new ShapeRenderer();
		dinoShapeRender = new ShapeRenderer();
		birdShapeRender = new ShapeRenderer();
		dinoHeadShapeRender = new ShapeRenderer();

    }

    private void setRegionImageOfBird() {
        for (int i = 0, row, column; i < textureRegionOfBird.length; i++) {
            textureRegionOfBird[i] = new TextureRegion(imgBird);
            row = i / 5;
            column = i % 5;

            textureRegionOfBird[i].setRegion(
                    imgBird.getWidth() / 5 * column,
                    imgBird.getHeight() / 4 * row,
                    imgBird.getWidth() / 5,
                    imgBird.getHeight() / 4);

        }
    }

    @Override
    public void render() {
        setYPositionStoneOrBird();
        setPositionOfBirdWings();
        startPosition();
        moveOfStone();
        jumpDino();
        startOfTheGame();
		checkGameOver();
		scoringDisplay();
    }

    private void setYPositionStoneOrBird() {
        if (!gameIsRunning) {
            correctionYBird = Gdx.graphics.getHeight();
        }

        if (stonePositionX < (0 - Gdx.graphics.getHeight() / 5)) {
            randomBirdOrStone = new Random();
            if (randomBirdOrStone.nextInt(2) == 0) {
                correctionYBird = Gdx.graphics.getHeight();
                correctionYStone = 0;
            } else {
                correctionYBird = 0;
                correctionYStone = Gdx.graphics.getHeight();
            }
        }
    }

    private void setPositionOfBirdWings() {
        if (gameIsRunning) {
            if (positionOfBirdWings == textureRegionOfBird.length - 1) {
                positionOfBirdWings = 0;
            } else {
                positionOfBirdWings++;
            }
        }

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

//        visualCheckingGameOver();

		stoneRectangle = new Rectangle(stonePositionX + Gdx.graphics.getWidth() / 50, 0 + correctionYStone, Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 6);
		dinoRectangle = new Rectangle(Gdx.graphics.getWidth() / 8, dinoPositionY + Gdx.graphics.getHeight() / 25, Gdx.graphics.getWidth() / 20, Gdx.graphics.getHeight() / 7);
        birdCircle = new Circle(stonePositionX + Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * 0.63f + correctionYBird, Gdx.graphics.getHeight() * 0.04f);
        dinoHeadCircle = new Circle(Gdx.graphics.getWidth() * 0.22f, dinoPositionY + Gdx.graphics.getHeight() * 0.375f, Gdx.graphics.getHeight() * 0.075f);


		if (Intersector.overlaps(stoneRectangle, dinoRectangle) || Intersector.overlaps(birdCircle, dinoHeadCircle)) {
			gameIsRunning = false;
			gameIsOver = true;
            if (soundOffOrOn) {
                sound_end.play();
            }
            soundOffOrOn = false;
		}
	}

    private void visualCheckingGameOver() {
        stoneShapeRender.begin(ShapeRenderer.ShapeType.Filled);
        stoneShapeRender.setColor(1,0,0,1);
        stoneShapeRender.rect(stonePositionX + Gdx.graphics.getWidth() / 50, 0 + correctionYStone, Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 6);
        stoneShapeRender.end();

        dinoShapeRender.begin(ShapeRenderer.ShapeType.Filled);
        dinoShapeRender.setColor(1, 0, 0, 1);
        dinoShapeRender.rect(Gdx.graphics.getWidth() / 8, dinoPositionY + Gdx.graphics.getHeight() / 25, Gdx.graphics.getWidth() / 20, Gdx.graphics.getHeight() / 5);
        dinoShapeRender.end();

        birdShapeRender.begin(ShapeRenderer.ShapeType.Filled);
        birdShapeRender.setColor(1,0,0,1);
        birdShapeRender.circle(stonePositionX + Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * 0.63f + correctionYBird, Gdx.graphics.getHeight() * 0.04f);
        birdShapeRender.end();

        dinoHeadShapeRender.begin(ShapeRenderer.ShapeType.Filled);
        dinoHeadShapeRender.setColor(1, 0, 0, 1);
        dinoHeadShapeRender.circle(Gdx.graphics.getWidth() * 0.22f, dinoPositionY + Gdx.graphics.getHeight() * 0.375f, Gdx.graphics.getHeight() * 0.075f);
        dinoHeadShapeRender.end();
    }

    private void startOfTheGame() {
        if (Gdx.input.justTouched() && !gameIsRunning && !gameIsOver) {
            gameIsRunning = true;
            soundOffOrOn = true;
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
        batch.draw(imgStone, stonePositionX, 0 + correctionYStone, Gdx.graphics.getWidth() / 6,
                Gdx.graphics.getHeight() / 4);

        batch.draw(textureRegionOfBird[positionOfBirdWings], stonePositionX,
                Gdx.graphics.getHeight() / 2 + correctionYBird,
                imgBird.getWidth() / 6,
                imgBird.getHeight() / 4);

        batch.end();
    }

    private void jumpDino() {
        if (gameIsRunning) {
            if (Gdx.input.justTouched()) {
                if (dinoPositionY == 0) {
                    dinoJump = true;
                    sound_jump.play();
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
