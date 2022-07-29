#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 vertexNormal;
layout (location = 2) in vec2 inTexCoord;

out vec2 texCoord;
out float intensity;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat3 normalMatrix;

uniform vec3 lightPosition;

void main()
{
	vec3 posES = (modelViewMatrix * vec4(position, 1.0)).xyz;
	vec3 normalES = normalMatrix * vertexNormal;

	vec3 posToLight = lightPosition - posES;
	vec3 lightDir = normalize(posToLight);
	float distToLight = length(posToLight);

	float cos = clamp(dot(normalES, lightDir), 0.0, 1.0);

	intensity = cos / (distToLight);

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0); // posES?
	texCoord = inTexCoord;
}