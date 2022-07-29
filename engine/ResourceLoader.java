package engine;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;

// TODO 1024 buffers?
public final class ResourceLoader
{
	private ResourceLoader() {}

	public static String loadTextFile(String fileName) throws Exception
	{
		InputStream in = ResourceLoader.class.getResourceAsStream(fileName);

		// https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];

		for (int length; (length = in.read(buf)) != -1; )
		{
			result.write(buf, 0, length);
		}

		return result.toString(StandardCharsets.UTF_8);
	}

	public static Mesh loadObj(String fileName, Texture tex) throws IOException
	{
		List<Vector3f> positions = new ArrayList<>();
		List<Vector2f> texCoords = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();

		Map<Vertex, Integer> vertices = new HashMap<>();
		List<Integer> indices = new ArrayList<>();

		boolean hasTextures = true;
		boolean hasNormals = true;

		//InputStream in = ResourceLoader.class.getResourceAsStream(fileName);
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

		String line;
		while ((line = r.readLine()) != null)
		{
			String[] tokens = line.split("\\s+");

			if(tokens.length == 0)
			{
				continue;
			}

			switch(tokens[0])
			{
				case "v":
					positions.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
					break;
				case "vt":
					texCoords.add(new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
					break;
				case "vn":
					normals.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
					break;
				case "f":
					for(int i = 1; i < tokens.length; ++i)
					{
						String[] tokens1 = tokens[i].split("/");

						if(hasTextures)
						{
							hasTextures = tokens1.length > 1 && tokens1[1].length() > 0;
						}

						if(hasNormals)
						{
							hasNormals = tokens1.length > 2 && tokens1[2].length() > 0;
						}

						Vertex vertex = new Vertex(
								positions.get(Integer.parseInt(tokens1[0]) - 1),
								hasTextures ? texCoords.get(Integer.parseInt(tokens1[1]) - 1) : null,
								hasNormals ? normals.get(Integer.parseInt(tokens1[2]) - 1) : null
						);

						Integer vertexIndex = vertices.get(vertex);

						if(vertexIndex == null)
						{
							vertexIndex = vertices.size();
							vertices.put(vertex, vertexIndex);
						}

						indices.add(vertexIndex);
					}
					break;
				default:
					break;
			}
		}

		int vertexCount = vertices.size();

		float[] positionsArray = new float[vertexCount * 3];
		float[] texCoordsArray = hasTextures ? new float[vertexCount * 2] : null;
		float[] normalsArray = hasNormals ? new float[vertexCount * 3] : null;

		int[] indicesArray = indices.stream().mapToInt(i -> i).toArray();

		for(var entry : vertices.entrySet())
		{
			Vertex vertex = entry.getKey();
			int i = entry.getValue();

			positionsArray[i * 3 + 0] = vertex.position.x;
			positionsArray[i * 3 + 1] = vertex.position.y;
			positionsArray[i * 3 + 2] = vertex.position.z;

			if(hasTextures)
			{
				texCoordsArray[i * 2 + 0] = vertex.texCoord.x;
				texCoordsArray[i * 2 + 1] = 1f - vertex.texCoord.y; // flip y
			}

			if(hasNormals)
			{
				normalsArray[i * 3 + 0] = vertex.normal.x;
				normalsArray[i * 3 + 1] = vertex.normal.y;
				normalsArray[i * 3 + 2] = vertex.normal.z;
			}
		}

		return new Mesh(positionsArray, normalsArray, texCoordsArray, indicesArray, tex);
	}

	private static final record Vertex(Vector3f position, Vector2f texCoord, Vector3f normal) {}
}