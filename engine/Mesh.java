package engine;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh
{
	private final int vaoId;
	private final int[] vboIds;

	private final int vertexCount;

	private final Texture texture;

	public Mesh(float[] positions, float[] vertexNormals, float[] texCoords, int[] indices, Texture texture) // TODO: handle no normals and no texture
	{
		this.vertexCount = indices.length;
		this.texture = texture;

		// Create VAO
		this.vaoId = GL33.glGenVertexArrays();
		GL33.glBindVertexArray(this.vaoId);

		this.vboIds = new int[]{
				createVbo(positions, 0, 3),
				createVbo(vertexNormals, 1, 3),
				createVbo(texCoords, 2, 2),
				createIbo(indices)
		};

		// Unbind VBO
		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);

		// Unbind VAO
		GL33.glBindVertexArray(0);
	}

	public void render()
	{
		// Activate firs texture bank
		GL33.glActiveTexture(GL33.GL_TEXTURE0);
		// Bind the texture
		this.texture.bind();

		// Bind VAO
		GL33.glBindVertexArray(this.vaoId);

		// Draw mesh
		GL33.glDrawElements(GL33.GL_TRIANGLES, this.vertexCount, GL33.GL_UNSIGNED_INT, 0);

		// Restore state
		GL33.glBindVertexArray(0);
	}

	public void cleanUp()
	{
		GL33.glDisableVertexAttribArray(0);

		// Delete all the VBOs
		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);

		for(int vboId : this.vboIds)
		{
			GL33.glDeleteBuffers(vboId);
		}

		// Delete the VAO
		GL33.glBindVertexArray(0);
		GL33.glDeleteVertexArrays(this.vaoId);

		this.texture.cleanUp();
	}



	private static int createVbo(float[] values, int index, int size) // TODO: why not just pass the array instead of doing off-heap allocation?
	{
		// Allocate buffer
		FloatBuffer buf = MemoryUtil.memAllocFloat(values.length);

		// Create and fill VBO
		int id = GL33.glGenBuffers();
		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, id);
		GL33.glBufferData(GL33.GL_ARRAY_BUFFER, buf.put(values).flip(), GL33.GL_STATIC_DRAW);

		// Free buffer
		MemoryUtil.memFree(buf);

		// Enable VBO
		GL33.glEnableVertexAttribArray(index);
		// Define data structure of VBO
		GL33.glVertexAttribPointer(index, size, GL33.GL_FLOAT, false, 0, 0);

		return id;
	}

	private static int createIbo(int[] values)
	{
		IntBuffer buf = MemoryUtil.memAllocInt(values.length);

		int id = GL33.glGenBuffers();
		GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, id);
		GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, buf.put(values).flip(), GL33.GL_STATIC_DRAW);

		MemoryUtil.memFree(buf);

		return id;
	}
}