package engine;

import java.util.function.Supplier;

public class GameEngine implements Runnable
{
	private final Window window;
	private final Timer timer;
	private final IGameLogic gameLogic;

	private final int targetTps; // TODO: split into ups and fps

	public GameEngine(String title, int width, int height, Supplier<IGameLogic> gameLogicFactory, int targetTps)
	{
		this.window = new Window(title, width, height);
		this.timer = new Timer();
		this.gameLogic = gameLogicFactory.get();
		this.targetTps = targetTps;
	}

	public Window getWindow()
	{
		return this.window;
	}

	public IGameLogic getGameLogic()
	{
		return this.gameLogic;
	}

	public void init()
	{

	}

	public void run()
	{
		this.loop();
		this.cleanup();
	}

	private void loop()
	{
		float accumulator = 0f;
		float interval = 1f / this.targetTps;

		while (!this.window.shouldClose())
		{
			float elapsedTime = this.timer.getElapsedTime();
			accumulator += elapsedTime;

			while(accumulator >= interval)
			{
				this.render();
				accumulator -= interval;
			}
		}
	}

	private void render()
	{
		this.gameLogic.render();
		this.window.update();
	}

	private void cleanup()
	{
		this.gameLogic.cleanUp();
	}
}