#version 330

in  vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;
out vec4 fragColor;

const int MAX_POINT_LIGHTS = 5;
const int MAX_SPOT_LIGHTS = 5;

uniform sampler2D texture_sampler;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];
uniform DirectionalLight directionalLight;


vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

struct Attenuation{
	float constant;
	float linear;
	float exponent;
};

struct PointLight{
	vec3 colour;
	vec3 position;
	float intensity;
	Attenuation att;
};

struct SpotLight
{
    PointLight pointLight;
    vec3 coneDirection;
    float cutoffCosine;
};


struct DirectionalLight{
	vec3 colour;
	vec3 direction;
	float intensity;
};

struct Material{
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	int hasTexture;
	float reflectance;
};

void setupColours(Material material, vec2 textCoord)
{
    if (material.hasTexture == 1)
    {
        ambientC = texture(texture_sampler, textCoord);
        diffuseC = ambientC;
        specularC = ambientC;
    }
    else
    {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
    }
}

vec4 calcLightColour(vec3 light_colour, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal)
{
    vec4 diffuseColour = vec4(0, 0, 0, 0);
    vec4 specColour = vec4(0, 0, 0, 0);

    // Diffuse Light
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColour = diffuseC * vec4(light_colour, 1.0) * light_intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(camera_pos - position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir , normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColour = speculrC * light_intensity  * specularFactor * material.reflectance * vec4(light_colour, 1.0);

    return (diffuseColour + specColour);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec3 light_direction = light.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec4 light_colour = calcLightColour(light.colour, light.intensity, position, to_light_dir, normal);

    // Apply Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance +
        light.att.exponent * distance * distance;
    return light_colour / attenuationInv;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal)
{
    return calcLightColour(light.colour, light.intensity, position, normalize(light.direction), normal);
}

vec4 calcSpotLight(SpotLight spotLight, vec3 position, vec3 normal){
	vec3 light_direction = light.pointLight.position - position;
	vec3 to_light_direction = normalize(light_direction);
	vec3 from_light_direction = -( to_light_direction );
	
	//i due vettori sono normalizzati quindi avendo modulo 1 in spot_alfa ci finisce il coseno
	float spot_alfa = dot(from_light_direction, normalize(spotLight.coneDirection));
	
	vec4 colour = vec4(0,0,0,0);
	
	if( spot_alfa > spotLight.cutoffCosine){
		colour = calcPointLight(spotLight.pointLight, position, normal);
		colour = colour * ( 1.0 - (1.0 - spot_alfa) / (1.0 - spotLight.cutoffCosine) );
	}
	
	return colour;
}

void main()
{
    setupColours(material, outTexCoord);

    vec4 diffuseSpecularComp = calcDirectionalLight(directionalLight, mvVertexPos, mvVertexNormal);

    for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
		if (pointLights[i].intensity > 0) {
			diffuseSpecularComp += calcPointLight(pointLights[i], mvVertexPos, mvVertexNormal);
		}
	}

	for (int i = 0; i < MAX_SPOT_LIGHTS; i++) {
		if (spotLights[i].pl.intensity > 0) {
			diffuseSpecularComp += calcSpotLight(spotLights[i], mvVertexPos,mvVertexNormal);
		}
	}

    fragColor = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp;
}
