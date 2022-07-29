package engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

public class Window
{
	private final long handle;

	private final String title;
	private int width;
	private int height;

	public Window(String title, int width, int height)
	{
		this.title = title;
		this.width = width;
		this.height = height;
		this.handle = createWindow(title, width, height);
	}

	public String getTitle()
	{
		return this.title;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public float getAspectRatio()
	{
		return (float) this.width / this.height;
	}

	public void update()
	{
		GLFW.glfwSwapBuffers(this.handle);
		GLFW.glfwPollEvents();
	}

	public boolean shouldClose()
	{
		return GLFW.glfwWindowShouldClose(this.handle);
	}




	private static long createWindow(String title, int width, int height) // GLFWFramebufferSizeCallbackI onResize
	{
		GLFWErrorCallback.createPrint(System.err).set();

		GLFW.glfwInit();
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL33.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL33.GL_TRUE);

		long handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

		/*
		GLFW.glfwSetFramebufferSizeCallback(handle, onResize);
		 */

		/*
		GLFW.glfwSetKeyCallback(handle, (window, key, scancode, action, mods) ->
		{
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
			{
				GLFW.glfwSetWindowShouldClose(window, true);
			}
		});
		 */

		GLFW.glfwMakeContextCurrent(handle);
		GLFW.glfwSwapInterval(1);
		GL.createCapabilities();

		GL33.glClearColor(0f, 0f, 0f, 0f);
		GL33.glEnable(GL33.GL_DEPTH_TEST);

		return handle;
	}
}