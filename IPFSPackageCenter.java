import java.util.ArrayList;
import java.util.Random;

public class IPFSPackageCenter {
    /**
     * This class for peers add packages and get randomly packages from
     * our central IPFS file storage repo, actually we want to develop
     * a distrubuted package manager for peers
     * but we dont forget all the packages need a storage area
     * */
    public static ArrayList<MetaPackage> allPackages = new ArrayList<>();  // Static list to store all packages

    // Method to add package to the list
    public static void addPackage(MetaPackage packageToAdd) {
        allPackages.add(packageToAdd);
    }

    // Method to randomly select and send a package from the list
    public static MetaPackage sendRandomPackage() {
        if (allPackages.size() > 0) {
            Random random = new Random();
            int randomPackageIndex = random.nextInt(allPackages.size());
            return allPackages.get(randomPackageIndex);
        } else {
            // Return null if there are no packages in the list
            return null;
        }
    }

    public static Integer getSize(){
        return allPackages.size();
    }


}
