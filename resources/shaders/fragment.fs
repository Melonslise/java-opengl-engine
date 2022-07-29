#version 330

in vec2 texCoord;
in float intensity;

out vec4 fragColor;

uniform sampler2D textureSampler;

void main()
{
	fragColor = texture(textureSampler, texCoord) * intensity;
}