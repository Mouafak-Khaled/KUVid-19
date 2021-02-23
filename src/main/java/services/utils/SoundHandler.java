package services.utils;

import model.game_running.CollisionHandler;
import model.game_running.RunningMode;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundHandler implements CollisionHandler.OnCollisionListener, RunningMode.OnGameStateListener {
    // For sound effects.
    private Clip soundEffectsClip;
    private AudioInputStream soundEffectAudioInputStream;
    private String soundEffectFilePath;

    // For game music.
    private Clip gameStateClip;
    private AudioInputStream gameStateAudioInputStream;
    private String gameStateFilePath;

    // Default sound effects.
    private final String COLLECT_MOLECULE = "assets/Sounds/SoundEffects/collect.wav";
    private final String GET_DAMAGE = "assets/Sounds/SoundEffects/hurt.wav";
    private final String COLLECT_POWERUP = "assets/Sounds/SoundEffects/powerup.wav";
    private final String DESTROY_BLOCKER = "assets/Sounds/SoundEffects/explosion.wav";
    private final String ATOM_BLOCKED = "assets/Sounds/SoundEffects/atom_blocked.wav";
    private final String PROJECTILE_SHOT = "assets/Sounds/SoundEffects/shoot.wav";

    // Game music
    private final String BACKGROUND_MUSIC = "assets/Sounds/GameMusic/background_music.wav";
    private final String GAME_OVER_MUSIC = "assets/Sounds/GameMusic/game_over.wav";

    @Override
    public void onShooterBlockerCollision() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        this.soundEffectFilePath = GET_DAMAGE;
        soundEffectsClip = getSoundEffectClip(this.soundEffectFilePath);
        soundEffectsClip.open(soundEffectAudioInputStream);
        soundEffectsClip.start();
    }

    @Override
    public void onAtomMoleculeCollision() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        this.soundEffectFilePath = COLLECT_MOLECULE;
        soundEffectsClip = getSoundEffectClip(this.soundEffectFilePath);
        soundEffectsClip.open(soundEffectAudioInputStream);
        soundEffectsClip.start();
    }

    @Override
    public void onShooterPowerupCollision() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.soundEffectFilePath = COLLECT_POWERUP;
        soundEffectsClip = getSoundEffectClip(this.soundEffectFilePath);
        soundEffectsClip.open(soundEffectAudioInputStream);
        soundEffectsClip.start();
    }

    @Override
    public void onPowerupBlockerCollision() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.soundEffectFilePath = DESTROY_BLOCKER;
        soundEffectsClip = getSoundEffectClip(this.soundEffectFilePath);
        soundEffectsClip.open(soundEffectAudioInputStream);
        soundEffectsClip.start();
    }

    @Override
    public void onAtomBlockerCollision() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.soundEffectFilePath = ATOM_BLOCKED;
        soundEffectsClip = getSoundEffectClip(this.soundEffectFilePath);
        soundEffectsClip.open(soundEffectAudioInputStream);
        soundEffectsClip.start();
    }

    @Override
    public void onGameStart() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.gameStateFilePath = BACKGROUND_MUSIC;

        gameStateClip = getGameStateClip(this.gameStateFilePath);
        gameStateClip.open(gameStateAudioInputStream);

        FloatControl volumeControl = (FloatControl) gameStateClip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(-28.0f);

        this.gameStateClip.loop(Clip.LOOP_CONTINUOUSLY);
        gameStateClip.start();
    }

    @Override
    public void onGameOver() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.gameStateFilePath = GAME_OVER_MUSIC;
        gameStateClip.close();
        gameStateClip = getGameStateClip(this.gameStateFilePath);
        gameStateClip.open(gameStateAudioInputStream);

        FloatControl volumeControl = (FloatControl) gameStateClip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(-20.0f);

        this.gameStateClip.loop(Clip.LOOP_CONTINUOUSLY);
        gameStateClip.start();
    }

    @Override
    public void onGamePaused() {
        gameStateClip.stop();
    }

    @Override
    public void onGameResume() {
        gameStateClip.start();
    }

    @Override
    public void onShoot() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.soundEffectFilePath = PROJECTILE_SHOT;
        soundEffectsClip = getSoundEffectClip(this.soundEffectFilePath);
        soundEffectsClip.open(soundEffectAudioInputStream);
        soundEffectsClip.start();
    }

    /**
     * Given a file path, returns a clip taken from the given file path.
     * @param filePath The file path.
     * @return A Clip object provided with the desired file path.
     * @throws UnsupportedAudioFileException If the audio type is not supported.
     * @throws IOException If the audio file was not imported properly.
     * @throws LineUnavailableException
     */
    private Clip getSoundEffectClip(String filePath) throws UnsupportedAudioFileException, IOException,
            LineUnavailableException{
        soundEffectAudioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
        return AudioSystem.getClip();
    }
    /**
     * Given a file path, returns a clip taken from the given file path.
     * @param filePath The file path.
     * @return A Clip object provided with the desired file path.
     * @throws UnsupportedAudioFileException If the audio type is not supported.
     * @throws IOException If the audio file was not imported properly.
     * @throws LineUnavailableException
     */
    private Clip getGameStateClip(String filePath) throws UnsupportedAudioFileException, IOException,
            LineUnavailableException{
        gameStateAudioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
        return AudioSystem.getClip();
    }
}
