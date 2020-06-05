package it.cubicworldsimulator.engine.graphic.light;

import java.util.Objects;
import org.joml.Vector3f;


public class SceneLight {

    private final DirectionalLight directionalLight;
    private final PointLight[] pointLights;
    private final SpotLight[] spotLights;
    private final float specularPower;
    private Vector3f ambientLight;

    public SceneLight(DirectionalLight directionalLight, PointLight[] pointLights, SpotLight[] spotLights, Vector3f ambientLight, float specularPower) {
        this.directionalLight = directionalLight;
        this.pointLights = pointLights;
        this.spotLights = spotLights;
        this.ambientLight = ambientLight;
        this.specularPower = specularPower;
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public PointLight[] getPointLights() {
        return pointLights;
    }

    public SpotLight[] getSpotLights() {
        return spotLights;
    }

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public float getSpecularPower() {
        return specularPower;
    }
    
    @Override
    public boolean equals(Object o) {
    	if(this == o) return true;
    	if(o == null || getClass() != o.getClass()) return false;
    	SceneLight sl = (SceneLight) o;
    	return this.directionalLight == sl.getDirectionalLight() &&
    			this.ambientLight == sl.getAmbientLight() &&
    			this.pointLights == sl.getPointLights() &&
    			this.spotLights == sl.getSpotLights() &&
    			this.specularPower == sl.getSpecularPower();	
    }
    
    public static class Builder implements SceneLightBuilder{
    	
    	private DirectionalLight directionalLight;
    	private PointLight[] pointLights = new PointLight[5];
    	private SpotLight[] spotLights = new SpotLight[5];    	
    	private Vector3f ambientLight;
    	private float specularPower;
    	
		@Override
		public SceneLightBuilder addDirectionalLight(DirectionalLight light) throws IllegalStateException {
			Objects.requireNonNull(light);
			if(this.directionalLight != null) {
				throw new IllegalStateException();
			}
			this.directionalLight = light;
			return this;
		}

		@Override
		public SceneLightBuilder addPointLight(PointLight light) {
			Objects.requireNonNull(light);
			this.pointLights[this.pointLights.length] = light;
			return this;
		}

		@Override
		public SceneLightBuilder addSpotLight(SpotLight light) {
			Objects.requireNonNull(light);
			this.spotLights[this.spotLights.length] = light;
			return this;
		}

		@Override
		public SceneLightBuilder addAmbientLight(Vector3f light) throws IllegalStateException {
			Objects.requireNonNull(light);
			if(this.ambientLight != null) {
				throw new IllegalStateException();
			}
			this.ambientLight = light;
			return this;
		}

		@Override
		public SceneLightBuilder addSpecularPower(float specularPower) {
			Objects.requireNonNull(specularPower);
			this.specularPower = specularPower;
			return this;
		}

		@Override
		public SceneLight build() throws IllegalStateException {
			if( this.ambientLight == null || this.directionalLight == null ) {
				throw new IllegalStateException();
			}
			if(this.pointLights[0] == null) {
				this.pointLights = new PointLight[0];
			}
			
			if(this.spotLights[0] == null) {
				this.spotLights = new SpotLight[0];
			}
			
			return new SceneLight(this.directionalLight, this.pointLights, this.spotLights, this.ambientLight, this.specularPower);
		}
    	
    }
}
