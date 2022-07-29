package engine;

public class Timer
{
	private double lastLoopTime = this.getTime();

	public double getTime()
	{
		return System.nanoTime() / 1_000_000_000.0d;
	}

	public float getElapsedTime()
	{
		double time = this.getTime();
		float elapsedTime = (float) (time - this.lastLoopTime);
		this.lastLoopTime = time;
		return elapsedTime;
	}
}