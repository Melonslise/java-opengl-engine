import engine.*;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

public class Renderer
{
	public final static float FOV = (float) Math.toRadians(60d);

	private final Camera camera = new Camera();

	private final ShaderProgram defaultShader;

	private GameObject[] gameObjects;

	public Renderer(String fileName)
	{
		// Load shaders
		ShaderProgram shader = null;

		try
		{
			shader = new ShaderProgram("/resources/shaders/vertex.vs", "/resources/shaders/fragment.fs");
			shader.createUniform("projectionMatrix");
			shader.createUniform("modelViewMatrix");
			shader.createUniform("normalMatrix");
			shader.createUniform("textureSampler");
			shader.createUniform("lightPosition");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		this.defaultShader = shader;

		this.initGameObjects(fileName);
	}

	private void initGameObjects(String fileName)
	{
		Mesh mesh = null;

		try
		{
			Texture tex = new Texture("src/resources/textures/grassblock.png");
			mesh = ResourceLoader.loadObj(fileName, tex);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		GameObject testObject = new GameObject(mesh);
		testObject.getPosition().set(0f, 0f, -2f);
		this.gameObjects = new GameObject[] { testObject };
	}

	public void render()
	{
		GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);

		// TODO: screen resize

		this.defaultShader.bind();
		this.defaultShader.setUniform("projectionMatrix", this.camera.getProjectionMatrix(FOV, Main.getEngine().getWindow().getAspectRatio()));
		this.defaultShader.setUniform("textureSampler", 0);
		this.defaultShader.setUniform("lightPosition", this.camera.getViewMatrix().transformPosition(new Vector3f(0f, 0f, 0f)));

		for(GameObject o : this.gameObjects)
		{
			o.getRotation().add((float) Math.PI / 480f, (float) Math.PI / 240f, 0f);

			this.defaultShader.setUniform("modelViewMatrix", this.camera.getModelViewMatrix(o.getPosition(), o.getRotation(), o.getScale()));
			this.defaultShader.setUniform("normalMatrix", this.camera.getNormalMatrix());

			o.getMesh().render();
		}
	}

	public void cleanUp()
	{
		this.defaultShader.cleanUp();

		for(GameObject o : this.gameObjects)
		{
			o.getMesh().cleanUp();
		}
	}
}