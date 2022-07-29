package engine;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

public class ShaderProgram
{
	private final int id;

	private final Map<String, Integer> uniforms = new HashMap<>();

	public ShaderProgram(String vertexFileName, String fragmentFileName) throws Exception
	{
		this.id = GL33.glCreateProgram();

		if (this.id == 0)
		{
			throw new Exception("Could not create Shader");
		}

		int vertexShaderId = createShader(this.id, ResourceLoader.loadTextFile(vertexFileName), GL33.GL_VERTEX_SHADER);
		int fragmentShaderId = createShader(this.id, ResourceLoader.loadTextFile(fragmentFileName), GL33.GL_FRAGMENT_SHADER);
		link(this.id, vertexShaderId, fragmentShaderId);
	}

	public void createUniform(String uniformName) throws Exception
	{
		int uniformLocation = GL33.glGetUniformLocation(this.id, uniformName);

		if (uniformLocation < 0)
		{
			throw new Exception("Could not find uniform:" + uniformName);
		}

		this.uniforms.put(uniformName, uniformLocation);
	}

	public void setUniform(String uniformName, int value)
	{
		GL33.glUniform1i(uniforms.get(uniformName), value);
	}

	public void setUniform(String uniformName, Vector3f value)
	{
		GL33.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
	}

	public void setUniform(String uniformName, Matrix3f value)
	{
		// Dump the matrix into a float buffer
		try (MemoryStack stack = MemoryStack.stackPush())
		{
			GL33.glUniformMatrix3fv(this.uniforms.get(uniformName), false, value.get(stack.mallocFloat(9)));
		}
	}

	public void setUniform(String uniformName, Matrix4f value)
	{
		// Dump the matrix into a float buffer
		try (MemoryStack stack = MemoryStack.stackPush())
		{
			GL33.glUniformMatrix4fv(this.uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
		}
	}

	public void bind()
	{
		GL33.glUseProgram(this.id);
	}

	public void cleanUp()
	{
		GL33.glUseProgram(0);

		if (this.id != 0)
		{
			GL33.glDeleteProgram(id);
		}
	}




	private static int createShader(int programId, String shaderCode, int shaderType) throws Exception
	{
		int shaderId = GL33.glCreateShader(shaderType);

		if (shaderId == 0)
		{
			throw new Exception("Error creating shader. Type: " + shaderType);
		}

		GL33.glShaderSource(shaderId, shaderCode);
		GL33.glCompileShader(shaderId);

		if (GL33.glGetShaderi(shaderId, GL33.GL_COMPILE_STATUS) == 0)
		{
			throw new Exception("Error compiling Shader code: " + GL33.glGetShaderInfoLog(shaderId, 1024));
		}

		GL33.glAttachShader(programId, shaderId);

		return shaderId;
	}

	private static void link(int programId, int vertexShaderId, int fragmentShaderId) throws Exception
	{
		GL33.glLinkProgram(programId);

		if (GL33.glGetProgrami(programId, GL33.GL_LINK_STATUS) == 0)
		{
			throw new Exception("Error linking Shader code: " + GL33.glGetProgramInfoLog(programId, 1024));
		}

		if (vertexShaderId != 0)
		{
			GL33.glDetachShader(programId, vertexShaderId);
		}

		if (fragmentShaderId != 0)
		{
			GL33.glDetachShader(programId, fragmentShaderId);
		}

		GL33.glValidateProgram(programId);

		if (GL33.glGetProgrami(programId, GL33.GL_VALIDATE_STATUS) == 0)
		{
			System.err.println("Warning validating Shader code: " + GL33.glGetProgramInfoLog(programId, 1024));
		}
	}
}