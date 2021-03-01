package de.davidkupper.CubeTimer;
import java.util.Arrays;

public class Part {
    private final Vector3 xVec;
    private final Vector3 yVec;
    private final Vector3 zVec;
    private final Cube.Side[] sides;

    public Part() {
        xVec = Vector3.createUnitVectorX();
        yVec = Vector3.createUnitVectorY();
        zVec = Vector3.createUnitVectorZ();
        sides = new Cube.Side[6];
        Arrays.fill(sides, Cube.Side.NONE);
    }

    public void setSide(Cube.Side side, Cube.Side colorOfSide) {
        sides[side.ordinal()] = colorOfSide;
    }

    public Cube.Side getSideOfColor(Cube.Side side) {
        switch (side) {
            case RIGHT:
                if (xVec.getX() == 1)
                    return sides[Cube.Side.RIGHT.ordinal()];
                if (xVec.getX() == -1)
                    return sides[Cube.Side.LEFT.ordinal()];
                if (yVec.getX() == 1)
                    return sides[Cube.Side.DOWN.ordinal()];
                if (yVec.getX() == -1)
                    return sides[Cube.Side.UP.ordinal()];
                if (zVec.getX() == 1)
                    return sides[Cube.Side.FRONT.ordinal()];
                if (zVec.getX() == -1)
                    return sides[Cube.Side.BACK.ordinal()];
                throw new RuntimeException("rotation vectors of part " + this + " is not valid");
            case LEFT :
                if (xVec.getX() == 1)
                    return sides[Cube.Side.LEFT.ordinal()];
                if (xVec.getX() == -1)
                    return sides[Cube.Side.RIGHT.ordinal()];
                if (yVec.getX() == 1)
                    return sides[Cube.Side.UP.ordinal()];
                if (yVec.getX() == -1)
                    return sides[Cube.Side.DOWN.ordinal()];
                if (zVec.getX() == 1)
                    return sides[Cube.Side.BACK.ordinal()];
                if (zVec.getX() == -1)
                    return sides[Cube.Side.FRONT.ordinal()];
                throw new RuntimeException("rotation vectors of part " + this + " is not valid");
            case DOWN :
                if (xVec.getY() == 1)
                    return sides[Cube.Side.RIGHT.ordinal()];
                if (xVec.getY() == -1)
                    return sides[Cube.Side.LEFT.ordinal()];
                if (yVec.getY() == 1)
                    return sides[Cube.Side.DOWN.ordinal()];
                if (yVec.getY() == -1)
                    return sides[Cube.Side.UP.ordinal()];
                if (zVec.getY() == 1)
                    return sides[Cube.Side.FRONT.ordinal()];
                if (zVec.getY() == -1)
                    return sides[Cube.Side.BACK.ordinal()];
                throw new RuntimeException("rotation vectors of part " + this + " is not valid");
            case UP :
                if (xVec.getY() == 1)
                    return sides[Cube.Side.LEFT.ordinal()];
                if (xVec.getY() == -1)
                    return sides[Cube.Side.RIGHT.ordinal()];
                if (yVec.getY() == 1)
                    return sides[Cube.Side.UP.ordinal()];
                if (yVec.getY() == -1)
                    return sides[Cube.Side.DOWN.ordinal()];
                if (zVec.getY() == 1)
                    return sides[Cube.Side.BACK.ordinal()];
                if (zVec.getY() == -1)
                    return sides[Cube.Side.FRONT.ordinal()];
                throw new RuntimeException("rotation vectors of part " + this + " is not valid");
            case FRONT :
                if (xVec.getZ() == 1)
                    return sides[Cube.Side.RIGHT.ordinal()];
                if (xVec.getZ() == -1)
                    return sides[Cube.Side.LEFT.ordinal()];
                if (yVec.getZ() == 1)
                    return sides[Cube.Side.DOWN.ordinal()];
                if (yVec.getZ() == -1)
                    return sides[Cube.Side.UP.ordinal()];
                if (zVec.getZ() == 1)
                    return sides[Cube.Side.FRONT.ordinal()];
                if (zVec.getZ() == -1)
                    return sides[Cube.Side.BACK.ordinal()];
                throw new RuntimeException("rotation vectors of part " + this + " is not valid");
            case BACK :
                if (xVec.getZ() == 1)
                    return sides[Cube.Side.LEFT.ordinal()];
                if (xVec.getZ() == -1)
                    return sides[Cube.Side.RIGHT.ordinal()];
                if (yVec.getZ() == 1)
                    return sides[Cube.Side.UP.ordinal()];
                if (yVec.getZ() == -1)
                    return sides[Cube.Side.DOWN.ordinal()];
                if (zVec.getZ() == 1)
                    return sides[Cube.Side.BACK.ordinal()];
                if (zVec.getZ() == -1)
                    return sides[Cube.Side.FRONT.ordinal()];
                throw new RuntimeException("rotation vectors of part " + this + " is not valid");
            default:
                throw new IllegalArgumentException("side should never be 'NONE'!");
        }
    }

    public void rotateX() {
        xVec.rotateX();
        yVec.rotateX();
        zVec.rotateX();
    }

    public void rotateNegX() {
        xVec.rotateNegX();
        yVec.rotateNegX();
        zVec.rotateNegX();
    }

    public void rotateY() {
        xVec.rotateY();
        yVec.rotateY();
        zVec.rotateY();
    }

    public void rotateNegY() {
        xVec.rotateNegY();
        yVec.rotateNegY();
        zVec.rotateNegY();
    }

    public void rotateZ() {
        xVec.rotateZ();
        yVec.rotateZ();
        zVec.rotateZ();
    }

    public void rotateNegZ() {
        xVec.rotateNegZ();
        yVec.rotateNegZ();
        zVec.rotateNegZ();
    }

}
