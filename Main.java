import engine.GameEngine;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;

public class Main
{
	private static GameEngine engine;

	public static void main(String[] args)
	{
		final JFrame f = new JFrame();
		f.setVisible(false);

		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("C:/"));
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileFilter()
		{
			@Override
			public boolean accept(File f)
			{
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".obj");
			}

			@Override
			public String getDescription()
			{
				return "Wavefront (.obj)";
			}
		});

		int result = fc.showOpenDialog(f);

		if(result == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();

			engine = new GameEngine("Test", 800, 600, () -> new TestGame(file.getAbsolutePath()), 60);
			engine.run();
		}

		System.exit(0);
	}

	public static GameEngine getEngine()
	{
		return engine;
	}



	/*
	public void init()
	{
		GLFW.glfwSetErrorCallback(this.errorCallback = GLFWErrorCallback.createPrint());

		GLFW.glfwInit();
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

		this.window = GLFW.glfwCreateWindow(this.width, this.height, "Hello world", MemoryUtil.NULL, MemoryUtil.NULL);
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GLFW.glfwSwapInterval(1);
	}

	public void initGl()
	{
		GL33.glEnable(GL33.GL_TEXTURE_2D);

		//GL33.glShadeModel(GL33.GL_SMOOTH);

		GL33.glClearColor(0f, 0f, 0f, 0f);
		GL33.glClearDepth(1f);

		GL33.glEnable(GL33.GL_DEPTH_TEST);
		GL33.glDepthFunc(GL33.GL_LEQUAL);

		GL33.glMatrixMode(GL33.GL_PROJECTION);
		GL33.glLoadIdentity();
		Util.perspective(45f, (float) this.width / this.height, 0.1f, 100f);

		//GL33.glHint(GL33.GL_PERSPECTIVE_CORRECTION_HINT, GL33.GL_NICEST);
	}

	public void loop()
	{
		while (!GLFW.glfwWindowShouldClose(this.window))
		{
			GLFW.glfwPollEvents();
			GLFW.glfwSwapBuffers(this.window);
		}
	}

	public void render()
	{
		GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);

		GL33.glMatrixMode(GL33.GL_MODELVIEW);
		GL33.glLoadIdentity();

		GL33.glBegin(GL33.GL_QUADS);

		GL33.glColor3f(1f,1f,0f);
		GL33.glVertex3f( 1f, 1f,-1f);
		GL33.glVertex3f(-1f, 1f,-1f);
		GL33.glVertex3f(-1f, 1f, 1f);
		GL33.glVertex3f( 1f, 1f, 1f);
		GL33.glColor3f(1f,0.5f,0f);
		GL33.glVertex3f( 1f,-1f, 1f);
		GL33.glVertex3f(-1f,-1f, 1f);
		GL33.glVertex3f(-1f,-1f,-1f);
		GL33.glVertex3f( 1f,-1f,-1f);
		GL33.glColor3f(1f,0f,0f);
		GL33.glVertex3f( 1f, 1f, 1f);
		GL33.glVertex3f(-1f, 1f, 1f);
		GL33.glVertex3f(-1f,-1f, 1f);
		GL33.glVertex3f( 1f,-1f, 1f);
		GL33.glColor3f(1f,1f,0f);
		GL33.glVertex3f( 1f,-1f,-1f);
		GL33.glVertex3f(-1f,-1f,-1f);
		GL33.glVertex3f(-1f, 1f,-1f);
		GL33.glVertex3f( 1f, 1f,-1f);
		GL33.glColor3f(0f,0f,1f);
		GL33.glVertex3f(-1f, 1f, 1f);
		GL33.glVertex3f(-1f, 1f,-1f);
		GL33.glVertex3f(-1f,-1f,-1f);
		GL33.glVertex3f(-1f,-1f, 1f);
		GL33.glColor3f(1f,0f,1f);
		GL33.glVertex3f( 1f, 1f,-1f);
		GL33.glVertex3f( 1f, 1f, 1f);
		GL33.glVertex3f( 1f,-1f, 1f);
		GL33.glVertex3f( 1f,-1f,-1f);

		GL33.glEnd();
	}

	public void dispose()
	{
		GLFW.glfwDestroyWindow(this.window);
		GLFW.glfwTerminate();
		this.errorCallback.free();
	}
	*/
}