package it.cubicworldsimulator.engine;

import it.cubicworldsimulator.engine.graphic.Camera;
import it.cubicworldsimulator.engine.graphic.Mesh;
import it.cubicworldsimulator.engine.graphic.SkyBox;

import java.util.*;

public class Scene {

    private Map<Mesh, List<GameItem>> opaqueMeshMap = new HashMap<>();
    private Map<Mesh, List<GameItem>> transparentMeshMap = new HashMap<>();
    private final ShaderProgram shaderProgram;
    private final SkyBox skyBox;
    private final Camera camera;
    private SceneLight sceneLight; //ci andrebbe


    public Scene(Map<Mesh, List<GameItem>> opaqueMeshMap, Map<Mesh, List<GameItem>> transparentMeshMap, ShaderProgram shaderProgram, SkyBox skyBox, Camera camera) {
        this.opaqueMeshMap = opaqueMeshMap;
        this.transparentMeshMap = transparentMeshMap;
        this.shaderProgram = shaderProgram;
        this.skyBox = skyBox;
        this.camera = camera;
    }

    public Scene(ShaderProgram shaderProgram, SkyBox skyBox, Camera camera, GameItem... gameItems){
        this.shaderProgram = shaderProgram;
        setGameItems(gameItems);
        this.skyBox = skyBox;
        this.camera = camera;
    }

    public Scene(GameItem[] gameItems, ShaderProgram shaderProgram, SkyBox skyBox, Camera camera){
        this.shaderProgram = shaderProgram;
        setGameItems(gameItems);
        this.skyBox = skyBox;
        this.camera = camera;
    }

    public void setGameItems(GameItem[] gameItems) {
        int numGameItems = gameItems != null ? gameItems.length : 0;
        for (int i=0; i<numGameItems; i++) {
            GameItem gameItem = gameItems[i];
            Mesh mesh = gameItem.getMesh();
            List<GameItem> list = opaqueMeshMap.get(mesh);
            if ( list == null ) {
                list = new ArrayList<>();
                opaqueMeshMap.put(mesh, list);
            }
            list.add(gameItem);
        }
    }

    public void cleanUp(){
        opaqueMeshMap.keySet().forEach(Mesh::cleanUp);
        shaderProgram.cleanup();
        skyBox.getMesh().cleanUp();
        skyBox.getShaderProgram().cleanup();
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }
    
    public SceneLight getSceneLight() {
    	return this.sceneLight;
    }
    
    public void setSceneLight(SceneLight sceneLight) {
		this.sceneLight = sceneLight;
	}

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public Map<Mesh, List<GameItem>> getOpaqueMeshMap() {
        return Collections.unmodifiableMap(opaqueMeshMap);
    }

    public Map<Mesh, List<GameItem>> getTransparentMeshMap() {
        return Collections.unmodifiableMap(transparentMeshMap);
    }

    public Camera getCamera() {
        return camera;
    }

	
}
