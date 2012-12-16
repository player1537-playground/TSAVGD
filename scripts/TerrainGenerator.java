import java.io.*;
import java.util.ArrayList;

public class TerrainGenerator {
  
  static String path = "island";
  static int sectionx = 9, sectionz = 9;
  
  public static void main(String[] args) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(path + ".obj"));
      BufferedWriter writer = new BufferedWriter(new FileWriter(path + "_fixed.obj"));
      String currentLine;
      ArrayList<Vector3f> verts = new ArrayList<Vector3f>();
      ArrayList<int[]> faces = new ArrayList<int[]>();
      ArrayList<String> faceLines = new ArrayList<String>();
      float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
      float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;
      int vertCount = 0, normCount = 0, textCount = 0, faceCount = 0;
      while ((currentLine = reader.readLine()) != null) {
        if (currentLine.startsWith("v ")) {
          vertCount++;
          String[] vertString = currentLine.split(" ");
          float x = Float.valueOf(vertString[1]);
          float y = Float.valueOf(vertString[2]);
          float z = Float.valueOf(vertString[3]);
          verts.add(new Vector3f(x, y, z));
          
          if (x < minX) {
            minX = x;
          }
          if (y < minY) {
            minY = y;
          }
          if (z < minZ) {
            minZ = z;
          }
          
          if (x > maxX) {
            maxX = x;
          }
          if (y > maxY) {
            maxY = y;
          }
          if (z > maxZ) {
            maxZ = z;
          }
          
        }
        if (currentLine.startsWith("f ")) {
          faceCount++;
          String[] faceString = currentLine.split(" ");
          faces.add(new int[]{
            Integer.parseInt(faceString[1].split("/")[0]) - 1,
              Integer.parseInt(faceString[2].split("/")[0]) - 1,
              Integer.parseInt(faceString[3].split("/")[0]) - 1
          });
          faceLines.add(currentLine);
        } else {
          writer.write(currentLine);
          writer.newLine();
        }
        if (currentLine.startsWith("vn ")) {
          normCount++;
        }
        if (currentLine.startsWith("vt ")) {
          textCount++;
        }
      }
      ArrayList<Point> grid = new ArrayList<Point>();
      float multX = sectionx / (maxX - minX);
      float multZ = sectionz / (maxZ - minZ);
      for (Vector3f v : verts) {
        int x = (int) ((v.getX() - minX) * multX);
        int z = (int) ((v.getZ() - minZ) * multZ);
        if(x == sectionx) {
          x = sectionx - 1;
        }
        if(z == sectionz) {
          z = sectionz - 1;
        }
        Point p = new Point(x, z);
        grid.add(p);
      }
      
      ArrayList[][] faceIndices = new ArrayList[sectionx][sectionz];
      for (int i = 0; i < faceIndices.length; i++) {
        for (int j = 0; j < faceIndices[i].length; j++) {
          faceIndices[i][j] = new ArrayList();
        }
      }
      for (int i = 0; i < faces.size(); i++) {
        int[] vertecies = faces.get(i);
        ArrayList<Point> face = new ArrayList<Point>();
        for (int index : vertecies) {
          Point p = grid.get(index);
          if (!face.contains(p)) {
            int x = p.getX();
            int z = p.getZ();
            faceIndices[x][z].add(i);
            face.add(p);
          }
        }
      }
      
      for (int i = 0; i < faceIndices.length; i++) {
        for (int j = 0; j < faceIndices[i].length; j++) {
          writer.write("#grid " + i + " " + j);
          writer.newLine();
          for (int k = 0; k < faceIndices[i][j].size(); k++) {
            int faceIndex = (Integer) faceIndices[i][j].get(k);
            String line = faceLines.get(faceIndex);
            writer.write(line);
            writer.newLine();
          }
        }
      }
      writer.write("#min " + minX + " " + minY + " " + minZ);
      writer.newLine();
      writer.write("#max " + maxX + " " + maxY + " " + maxZ);
      writer.newLine();
      writer.write("#vertexeCount " + vertCount);
      writer.newLine();
      writer.write("#normalCount " + normCount);
      writer.newLine();
      writer.write("#textureCount " + textCount);
      writer.newLine();
      writer.write("#faceCount " + faceCount);
      writer.newLine();
      reader.close();
      writer.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
