package teamunc.defarmers2.utils.worldEdit;

import org.bukkit.Location;

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
}
