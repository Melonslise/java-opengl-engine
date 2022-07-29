package engine;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera
{
	public static final float Z_NEAR = 0.01f;
	public static final float Z_FAR = 1000f;

	private final Matrix4f projectionMatrix = new Matrix4f();
	private final Matrix4f viewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();
	private final Matrix4f modelViewMatrix = new Matrix4f();
	private final Matrix3f normalMatrix = new Matrix3f();

	private float fov;
	private float aspectRatio;

	private final Vector3f position = new Vector3f();
	private final Vector3f rotation = new Vector3f();

	public Vector3f getPosition()
	{
		return this.position;
	}

	public Vector3f getRotation()
	{
		return this.rotation;
	}

	public Matrix4f getProjectionMatrix(float fov, float aspectRatio)
	{
		if(this.fov == fov && this.aspectRatio == aspectRatio)
		{
			return this.projectionMatrix;
		}

		this.fov = fov;
		this.aspectRatio = aspectRatio;

		return this.projectionMatrix
				.perspective(this.fov, this.aspectRatio, Z_NEAR, Z_FAR);
	}

	public Matrix4f getViewMatrix() // TODO: don't recompute unless changed
	{
		return this.viewMatrix.identity()
				.rotateXYZ(this.rotation)
				.translate(-this.position.x, -this.position.y, -this.position.z);
	}

	public Matrix4f getModelViewMatrix(Vector3f position, Vector3f rotation, Vector3f scale)
	{
		this.modelMatrix.identity()
				.translate(position)
				.rotateXYZ(-rotation.x, -rotation.y, -rotation.z) // TODO: why do we need this?
				.scale(scale);

		return this.modelViewMatrix.set(this.getViewMatrix()).mul(this.modelMatrix);
	}

	public Matrix3f getNormalMatrix() // TODO: assumes modelview is already calculated
	{
		return this.modelViewMatrix.normal(this.normalMatrix);
	}
}