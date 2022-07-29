package engine;

import org.joml.Vector3f;

public class GameObject
{
	private final Mesh mesh;

	private final Vector3f position = new Vector3f();
	private final Vector3f rotation = new Vector3f();
	private final Vector3f scale = new Vector3f(1f, 1f, 1f);

	public GameObject(Mesh mesh)
	{
		this.mesh = mesh;
	}

	public Mesh getMesh()
	{
		return this.mesh;
	}

	public Vector3f getPosition()
	{
		return this.position;
	}

	public Vector3f getRotation()
	{
		return this.rotation;
	}

	public Vector3f getScale()
	{
		return this.scale;
	}
}