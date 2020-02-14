package it.cubicworldsimulator.game;

import it.cubicworldsimulator.engine.GameLogic;
import it.cubicworldsimulator.engine.renderer.RendererImpl;
import it.cubicworldsimulator.engine.Window;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

public class Game implements GameLogic {
    private int direction = 0;

    private float color = 0.0f;

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private Matrix4f projectionMatrix;


    private final RendererImpl renderer;

    public Game() {
        renderer = new RendererImpl();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        float aspectRatio = (float) window.getWidth() / window.getHeight();
        projectionMatrix = new Matrix4f().setPerspective(FOV, aspectRatio,
                Z_NEAR, Z_FAR);
    }

    @Override
    public void input(Window window) {
        if ( window.isKeyPressed(GLFW_KEY_UP) ) {
            direction = 1;
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update(float interval) {
    }

    @Override
    public void render(Window window) {

        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, null);
    }

    @Override
    public void cleanUp() {

    }
}
