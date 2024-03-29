package de.davidkupper.CubeTimer.cubemodel;

public class Cube {
    private final int size;
    private final Part[][][] parts;
    public enum Side {UP, DOWN, LEFT, RIGHT, FRONT, BACK, NONE;}
    public static final String[][] SCRAMBLE_POOL = {{"U", "U'", "U2", "D", "D'", "D2", "Uw", "Uw'", "Uw2", "Dw", "Dw'", "Dw2"}, {"L", "L'", "L2", "R", "R'", "R2", "Lw", "Lw'", "Lw2", "Rw", "Rw'", "Rw2"}, {"F", "F'", "F2", "B", "B'", "B2", "Fw", "Fw'", "Fw2", "Bw", "Bw'", "Bw2"}};

    public Cube(int size) {
        if(size < 2 || size > 4)
            throw new IllegalArgumentException("Size of Cube is not valid: 2 <= size <= 4");

        this.size = size;

        parts = new Part[size][size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
                    parts[x][y][z] = new Part();
                }
            }
        }

        reset();
    }

    public void reset() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
                    parts[x][y][z].reset();
                    if (x == 0)
                        parts[x][y][z].setSide(Side.LEFT, Side.LEFT);
                    if (x == size - 1)
                        parts[x][y][z].setSide(Side.RIGHT, Side.RIGHT);
                    if (y == 0)
                        parts[x][y][z].setSide(Side.UP, Side.UP);
                    if (y == size - 1)
                        parts[x][y][z].setSide(Side.DOWN, Side.DOWN);
                    if (z == 0)
                        parts[x][y][z].setSide(Side.BACK, Side.BACK);
                    if (z == size - 1)
                        parts[x][y][z].setSide(Side.FRONT, Side.FRONT);
                }
            }
        }
    }

    public Side[][] getSideMatrix(Side side) {
        int x, y, z;
        Side[][] matrix = new Side[getSize()][getSize()];

        switch (side) {
            case UP:
                x = 0;
                y = 0;
                z = 0;
                for (int i = 0; i < getSize(); i++) {
                    for (int j = 0; j < getSize(); j++) {
                        matrix[i][j] = parts[x][y][z].getSideOfColor(side);
                        x++;
                    }
                    x = 0;
                    z++;
                }
                break;
            case LEFT:
                x = 0;
                y = 0;
                z = 0;
                for (int i = 0; i < getSize(); i++) {
                    for (int j = 0; j < getSize(); j++) {
                        matrix[i][j] = parts[x][y][z].getSideOfColor(side);
                        z++;
                    }
                    z = 0;
                    y++;
                }
                break;
            case FRONT:
                x = 0;
                y = 0;
                z = getSize() - 1;
                for (int i = 0; i < getSize(); i++) {
                    for (int j = 0; j < getSize(); j++) {
                        matrix[i][j] = parts[x][y][z].getSideOfColor(side);
                        x++;
                    }
                    x = 0;
                    y++;
                }
                break;
            case RIGHT:
                x = getSize() - 1;
                y = 0;
                z = getSize() - 1;
                for (int i = 0; i < getSize(); i++) {
                    for (int j = 0; j < getSize(); j++) {
                        matrix[i][j] = parts[x][y][z].getSideOfColor(side);
                        z--;
                    }
                    z = getSize() - 1;
                    y++;
                }
                break;

            case BACK:
                x = getSize() - 1;
                y = 0;
                z = 0;
                for (int i = 0; i < getSize(); i++) {
                    for (int j = 0; j < getSize(); j++) {
                        matrix[i][j] = parts[x][y][z].getSideOfColor(side);
                        x--;
                    }
                    x = getSize() - 1;
                    y++;
                }
                break;
            case DOWN:
                x = 0;
                y = getSize() - 1;
                z = getSize() - 1;
                for (int i = 0; i < getSize(); i++) {
                    for (int j = 0; j < getSize(); j++) {
                        matrix[i][j] = parts[x][y][z].getSideOfColor(side);
                        x++;
                    }
                    x = 0;
                    z--;

                }
                break;
        }
        return matrix;
    }

    public void turn(String move) {
        switch (move) {
            case "D":
                rotateY(getSize() - 1);
                break;
            case "D'":
                rotateNegY(getSize() - 1);
                break;
            case "D2":
                rotateY(getSize() - 1);
                rotateY(getSize() - 1);
                break;
            case "U":
                rotateNegY(0);
                break;
            case "U'":
                rotateY(0);
                break;
            case "U2":
                rotateY(0);
                rotateY(0);
                break;
            case "R":
                rotateX(getSize() - 1);
                break;
            case "R'":
                rotateNegX(getSize() - 1);
                break;
            case "R2":
                rotateX(getSize() - 1);
                rotateX(getSize() - 1);
                break;
            case "L":
                rotateNegX(0);
                break;
            case "L'":
                rotateX(0);
                break;
            case "L2":
                rotateX(0);
                rotateX(0);
                break;
            case "F":
                rotateZ(getSize() - 1);
                break;
            case "F'":
                rotateNegZ(getSize() - 1);
                break;
            case "F2":
                rotateZ(getSize() - 1);
                rotateZ(getSize() - 1);
                break;
            case "B":
                rotateNegZ(0);
                break;
            case "B'":
                rotateZ(0);
                break;
            case "B2":
                rotateZ(0);
                rotateZ(0);
                break;
            // with inner layer:
            case "Dw":
                rotateY(getSize() - 1);
                rotateY(getSize() - 2);
                break;
            case "Dw'":
                rotateNegY(getSize() - 1);
                rotateNegY(getSize() - 2);
                break;
            case "Dw2":
                rotateY(getSize() - 1);
                rotateY(getSize() - 2);
                rotateY(getSize() - 1);
                rotateY(getSize() - 2);
                break;
            case "Uw":
                rotateNegY(0);
                rotateNegY(1);
                break;
            case "Uw'":
                rotateY(0);
                rotateY(1);
                break;
            case "Uw2":
                rotateY(0);
                rotateY(1);
                rotateY(0);
                rotateY(1);
                break;
            case "Rw":
                rotateX(getSize() - 1);
                rotateX(getSize() - 2);
                break;
            case "Rw'":
                rotateNegX(getSize() - 1);
                rotateNegX(getSize() - 2);
                break;
            case "Rw2":
                rotateX(getSize() - 1);
                rotateX(getSize() - 2);
                rotateX(getSize() - 1);
                rotateX(getSize() - 2);
                break;
            case "Lw":
                rotateNegX(0);
                rotateNegX(1);
                break;
            case "Lw'":
                rotateX(0);
                rotateX(1);
                break;
            case "Lw2":
                rotateX(0);
                rotateX(1);
                rotateX(0);
                rotateX(1);
                break;
            case "Fw":
                rotateZ(getSize() - 1);
                rotateZ(getSize() - 2);
                break;
            case "Fw'":
                rotateNegZ(getSize() - 1);
                rotateNegZ(getSize() - 2);
                break;
            case "Fw2":
                rotateZ(getSize() - 1);
                rotateZ(getSize() - 2);
                rotateZ(getSize() - 1);
                rotateZ(getSize() - 2);
                break;
            case "Bw":
                rotateNegZ(0);
                rotateNegZ(1);
                break;
            case "Bw'":
                rotateZ(0);
                rotateZ(1);
                break;
            case "Bw2":
                rotateZ(0);
                rotateZ(1);
                rotateZ(0);
                rotateZ(1);
                break;
            default:
                throw new IllegalArgumentException(move + " is not a valid move");
        }
    }

    // restriction: only 2x2 3x3 4x4

    public String getRandomScramble() {
        if(size == 2)
            return getRandomScramble(12, false);
        else if(size == 3)
            return getRandomScramble(25, false);
        else
            return getRandomScramble(20, false) + " " + getRandomScramble(15, true);
    }
    private String getRandomScramble(int moves, boolean withInner) {
        String s = "";
        int maxInPool = SCRAMBLE_POOL[0].length;
        if(!withInner)
            maxInPool = SCRAMBLE_POOL[0].length / 2;
        int rand, temp = -1;
        for(int i = 0; i < moves; i++) {
            do {
                rand = (int) (Math.random() * 3);
            } while(rand == temp);
            temp = rand;
            int innerRand = (int) (Math.random() * maxInPool);
            if(i > 0)
                s += " ";
            s += SCRAMBLE_POOL[rand][innerRand];
        }
        return s;
    }

    public void scramble(String moves) {
        String[] move = moves.split(" ");
        for (String s : move) {
            turn(s);
        }
    }

    public void rotateX(int x) {
        Part[][] rotatedLayer = new Part[getSize()][getSize()]; // rotated layer [x][y] --> valid; [y][x] --> invalid!
        for (int y = 0; y < getSize(); y++) {
            for (int z = 0; z < getSize(); z++) {
                parts[x][y][z].rotateX();
                double translation = (getSize() - 1) / 2.0;
                Vector3 v = new Vector3(x - translation, y - translation, z - translation);
                v.rotateX();
                int y2 = (int) (v.getY() + translation);
                int z2 = (int) (v.getZ() + translation);
                rotatedLayer[y2][z2] = parts[x][y][z];
            }
        }
        insertLayerX(x, rotatedLayer);
    }

    public void rotateNegX(int x) {
        Part[][] rotatedLayer = new Part[getSize()][getSize()]; // rotated layer [x][y] --> valid; [y][x] --> invalid!
        for (int y = 0; y < getSize(); y++) {
            for (int z = 0; z < getSize(); z++) {
                parts[x][y][z].rotateNegX();
                double translation = (getSize() - 1) / 2.0;
                Vector3 v = new Vector3(x - translation, y - translation, z - translation);
                v.rotateNegX();
                int y2 = (int) (v.getY() + translation);
                int z2 = (int) (v.getZ() + translation);
                rotatedLayer[y2][z2] = parts[x][y][z];
            }
        }
        insertLayerX(x, rotatedLayer);
    }

    public void rotateY(int y) {
        Part[][] rotatedLayer = new Part[getSize()][getSize()]; // rotated layer [x][y] --> valid; [y][x] --> invalid!
        for (int x = 0; x < getSize(); x++) {
            for (int z = 0; z < getSize(); z++) {
                parts[x][y][z].rotateY();
                double translation = (getSize() - 1) / 2.0;
                Vector3 v = new Vector3(x - translation, y - translation, z - translation);
                v.rotateY();
                int x2 = (int) (v.getX() + translation);
                int z2 = (int) (v.getZ() + translation);
                rotatedLayer[x2][z2] = parts[x][y][z];
            }
        }
        insertLayerY(y, rotatedLayer);
    }

    public void rotateNegY(int y) {
        Part[][] rotatedLayer = new Part[getSize()][getSize()]; // rotated layer [x][y] --> valid; [y][x] --> invalid!
        for (int x = 0; x < getSize(); x++) {
            for (int z = 0; z < getSize(); z++) {
                parts[x][y][z].rotateNegY();
                double translation = (getSize() - 1) / 2.0;
                Vector3 v = new Vector3(x - translation, y - translation, z - translation);
                v.rotateNegY();
                int x2 = (int) (v.getX() + translation);
                int z2 = (int) (v.getZ() + translation);
                rotatedLayer[x2][z2] = parts[x][y][z];
            }
        }
        insertLayerY(y, rotatedLayer);
    }

    public void rotateZ(int z) {
        Part[][] rotatedLayer = new Part[getSize()][getSize()]; // rotated layer [x][y] --> valid; [y][x] --> invalid!
        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                parts[x][y][z].rotateZ();
                double translation = (getSize() - 1) / 2.0;
                Vector3 v = new Vector3(x - translation, y - translation, z - translation);
                v.rotateZ();
                int x2 = (int) (v.getX() + translation);
                int y2 = (int) (v.getY() + translation);
                rotatedLayer[x2][y2] = parts[x][y][z];
            }
        }
        insertLayerZ(z, rotatedLayer);
    }

    public void rotateNegZ(int z) {
        Part[][] rotatedLayer = new Part[getSize()][getSize()]; // rotated layer [x][y] --> valid; [y][x] --> invalid!
        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                parts[x][y][z].rotateNegZ();
                double translation = (getSize() - 1) / 2.0;
                Vector3 v = new Vector3(x - translation, y - translation, z - translation);
                v.rotateNegZ();
                int x2 = (int) (v.getX() + translation);
                int y2 = (int) (v.getY() + translation);
                rotatedLayer[x2][y2] = parts[x][y][z];
            }
        }
        insertLayerZ(z, rotatedLayer);
    }

    public void insertLayerX(int x, Part[][] layer) {
        for (int y = 0; y < getSize(); y++) {
            for (int z = 0; z < getSize(); z++) {
                parts[x][y][z] = layer[y][z];
            }
        }
    }

    public void insertLayerY(int y, Part[][] layer) {
        for (int x = 0; x < getSize(); x++) {
            for (int z = 0; z < getSize(); z++) {
                parts[x][y][z] = layer[x][z];
            }
        }
    }

    public void insertLayerZ(int z, Part[][] layer) {
        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                parts[x][y][z] = layer[x][y];
            }
        }
    }

    public int getSize() {
        return size;
    }

}
