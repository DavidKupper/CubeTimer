package de.davidkupper.CubeTimer;

public class Vector3 {
    private double x;
    private double y;
    private double z;

    public Vector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void rotateX() {
        double y2 = -z;
        z = y;
        y = y2;
    }

    public void rotateNegX() {
        double y2 = z;
        z = -y;
        y = y2;
    }

    public void rotateY() {
        double x2 = z;
        z = -x;
        x = x2;
    }

    public void rotateNegY() {
        double x2 = -z;
        z = x;
        x = x2;
    }

    public void rotateZ() {
        double x2 = -y;
        y = x;
        x = x2;
    }

    public void rotateNegZ() {
        double x2 = y;
        y = -x;
        x = x2;
    }

    public static Vector3 createUnitVectorX() {
        return new Vector3(1,0,0);
    }

    public static Vector3 createUnitVectorY() {
        return new Vector3(0,1,0);
    }

    public static Vector3 createUnitVectorZ() {
        return new Vector3(0,0,1);
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public double getZ() {
        return z;
    }

}
