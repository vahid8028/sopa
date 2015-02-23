package de.sopa.scene.score;

import de.sopa.model.Level;
import de.sopa.model.StarCalculator;
import de.sopa.scene.BaseScene;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

/**
 * @author Raphael Schilling
 */
public class ScoreScreen extends BaseScene {
    private StarCalculator starCalculator;
    public ScoreScreen(Level level) {
        super(level);
    }

    @Override
    public void createScene(final Object o) {
        final Level level = (Level) o;
        starCalculator = new StarCalculator();
        int stars = starCalculator.getStars(level.getMovesCount(), level.getMinimumMovesToSolve());
        String levelCompleteString = level.getLevelInfo().getLevelId() + ". Level\nComplete";
        Text levelCompleteTextShape = new Text((float) (camera.getWidth() * 0.05), (float) (camera.getHeight() * 0.05), resourcesManager.movesScoreFont, levelCompleteString, vbom);
        levelCompleteTextShape.setScaleCenter(0, 0);
        levelCompleteTextShape.setScale(2);
        attachChild(levelCompleteTextShape);
        attachChild(new Text((float) (camera.getWidth() * 0.05), (float) (camera.getHeight() * 0.4), resourcesManager.movesScoreFont, "You're moves: \t\t\t" + level.getMovesCount() + "\nMoves for 3 Stars: " + level.getMinimumMovesToSolve(), vbom));
        attachChild(new Sprite(0, (float) (camera.getHeight() * 0.55), 400, 400, resourcesManager.starRegion, vbom));
        attachChild(new Sprite((float) (camera.getWidth() * 0.64), (float) (camera.getHeight() * 0.55), 400, 400, (stars == 3) ? resourcesManager.starRegion : resourcesManager.starSWRegion, vbom));
        attachChild(new Sprite((camera.getWidth() / 2 - 200), (float) (camera.getHeight() * 0.6), 400, 400, (stars >= 2) ? resourcesManager.starRegion : resourcesManager.starSWRegion, vbom));
        ButtonSprite choiceLevelButton = new ButtonSprite(0, (camera.getHeight() - 400), resourcesManager.backToChoiceRegion, vbom, new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                sceneService.loadLevelChoiceSceneFromScoreScene();
            }
        });
        ButtonSprite nextLevelButton = new ButtonSprite((float) (camera.getWidth() * 0.64), (float) (camera.getHeight() - 400), resourcesManager.nextLevelRegion, vbom, new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                int nextLevelId = level.getId() + 1;
                if (nextLevelId > levelService.getLevelCount()) {
                    sceneService.loadLevelChoiceSceneFromScoreScene();
                } else {
                    sceneService.loadGameSceneFromScoreScene(levelService.getLevelById(nextLevelId));
                }
            }
        });

        ButtonSprite backToMenuButton = new ButtonSprite((float) (camera.getWidth() / 2 - 200), (float) (camera.getHeight() - 400), resourcesManager.backToMenuRegionA, vbom, new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                sceneService.loadMenuSceneFromScoreScene();
            }
        });
        registerTouchArea(choiceLevelButton);
        registerTouchArea(nextLevelButton);
        registerTouchArea(backToMenuButton);
        attachChild(choiceLevelButton);
        attachChild(backToMenuButton);
        attachChild(nextLevelButton);
    }

    @Override
    public void onBackKeyPressed() {
        sceneService.loadMenuSceneFromGameScene();
    }

    @Override
    public void disposeScene() {
        final ScoreScreen scoreScreen = this;
        engine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                engine.unregisterUpdateHandler(pTimerHandler);
                scoreScreen.detachChildren();
            }
        }));
    }
}
