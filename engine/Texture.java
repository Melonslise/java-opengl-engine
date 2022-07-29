package engine;

import org.lwjgl.opengl.GL33;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture
{
	private final int id;

	public Texture(String fileName) throws Exception
	{
		this.id = loadTexture(fileName);
	}

	public void bind()
	{
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, this.id);
	}

	public void cleanUp()
	{
		GL33.glDeleteTextures(id);
	}



	private static int loadTexture(String fileName) throws Exception
	{
		int width;
		int height;
		ByteBuffer buf;

		try(MemoryStack stack = MemoryStack.stackPush())
		{
			IntBuffer widthBuf = stack.mallocInt(1);
			IntBuffer heightBuf = stack.mallocInt(1);
			IntBuffer channelsBuf = stack.mallocInt(1);

			buf = STBImage.stbi_load(fileName, widthBuf, heightBuf, channelsBuf, 4);

			if (buf == null)
			{
				throw new Exception("Image file [" + fileName  + "] not loaded: " + STBImage.stbi_failure_reason());
			}

			width = widthBuf.get();
			height = heightBuf.get();
		}

		// Create a new OpenGL texture
		int textureId = GL33.glGenTextures();

		// Bind the texture
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);

		// Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
		GL33.glPixelStorei(GL33.GL_UNPACK_ALIGNMENT, 1);

		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		// Upload the texture data
		GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA, width, height, 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, buf);

		// Generate Mip Map
		GL33.glGenerateMipmap(GL33.GL_TEXTURE_2D);

		STBImage.stbi_image_free(buf);

		return textureId;
	}
}