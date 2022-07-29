import engine.*;

public class TestGame implements IGameLogic
{
	private Renderer renderer;

	public TestGame(String fileName)
	{
		this.renderer = new Renderer(fileName);
	}

	@Override
	public void render()
	{
		this.renderer.render();
	}

	@Override
	public void cleanUp()
	{
		this.renderer.cleanUp();
	}
}