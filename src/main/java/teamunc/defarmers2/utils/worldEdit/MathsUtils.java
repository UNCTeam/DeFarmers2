package teamunc.defarmers2.utils.worldEdit;

import com.sk89q.worldedit.util.Direction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class MathsUtils {
    public static double distance(Location loc1, Location loc2) {
        return Math.sqrt(Math.pow(loc1.getX() - loc2.getX(), 2) + Math.pow(loc1.getY() - loc2.getY(), 2) + Math.pow(loc1.getZ() - loc2.getZ(), 2));
    }

    // calculate from a center point all other points in circle with radius and number of points
    public static Location[] getCircle(Location center, double radius, int numberOfPoints) {
        Location[] locations = new Location[numberOfPoints];
        double angle = 2 * Math.PI / numberOfPoints;
        for (int i = 0; i < numberOfPoints; i++) {
            double x = center.getX() + radius * Math.cos(i * angle);
            double z = center.getZ() + radius * Math.sin(i * angle);
            double y = center.getY();
            locations[i] = new Location(center.getWorld(), x, y, z);
        }
        return locations;
    }

    public static Location getRandomLocation(Location location, int radius) {
        Location randomLocation = location.clone();
        randomLocation.setX(randomLocation.getX() + (Math.random() * radius * 2) - radius);
        randomLocation.setZ(randomLocation.getZ() + (Math.random() * radius * 2) - radius);
        return randomLocation;
    }

    public static Direction getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 45) {
            return Direction.NORTH;
        } else if (45 <= rotation && rotation < 135) {
            return Direction.WEST;
        } else if (135 <= rotation && rotation < 225) {
            return Direction.SOUTH;
        } else if (225 <= rotation && rotation < 315) {
            return Direction.EAST;
        } else if (315 <= rotation && rotation < 360.0) {
            return Direction.NORTH;
        } else {
            return null;
        }
    }

    /**
     * give the next location in the direction if encounter a specific material
     * if material is null, return the next not air block
     */
    public static Location getNextLocation(Direction direction, Location location, int max, @Nullable Material material) {
        Location nextLocation = location.clone();
        switch (direction) {
            case NORTH:
                nextLocation.setZ(nextLocation.getZ() + 1);
                break;
            case SOUTH:
                nextLocation.setZ(nextLocation.getZ() - 1);
                break;
            case EAST:
                nextLocation.setX(nextLocation.getX() + 1);
                break;
            case WEST:
                nextLocation.setX(nextLocation.getX() - 1);
                break;
            case UP:
                nextLocation.setY(nextLocation.getY() + 1);
                break;
            case DOWN:
                nextLocation.setY(nextLocation.getY() - 1);
                break;
        }
        if (material == null && nextLocation.getBlock().getType() != Material.AIR) {
            return nextLocation;
        } else if (nextLocation.getBlock().getType() == material) {
            return nextLocation;
        } else if (max == 0) {
            return null;
        }
        return getNextLocation(direction, nextLocation, max - 1, material);
    }
}
