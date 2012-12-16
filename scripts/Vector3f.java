public class Vector3f {

 public float x, y, z;

 public Vector3f(float x, float y, float z) {
  set(x, y, z);
 }

 public void set(float x, float y, float z) {
  this.x = x;
  this.y = y;
  this.z = z;
 }

 /**
  * @return x
  */
 public final float getX() {
  return x;
 }

 /**
  * @return y
  */
 public final float getY() {
  return y;
 }

 /**
  * Set X
  * @param x
  */
 public final void setX(float x) {
  this.x = x;
 }

 /**
  * Set Y
  * @param y
  */
 public final void setY(float y) {
  this.y = y;
 }

 /**
  * Set Z
  * @param z
  */
 public void setZ(float z) {
  this.z = z;
 }

 /* (Overrides)
  * @see org.lwjgl.vector.ReadableVector3f#getZ()
  */
 public float getZ() {
  return z;
 }
}
