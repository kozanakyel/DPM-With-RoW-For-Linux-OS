import java.util.Random;
import java.security.*;

public class MetaPackageUtil {
    // uncompleted randomly created metapackages
    // after createing randomly packages you should sign wwith peers wallet.
    public static MetaPackage createRandomMetaPackage(Wallet creatorWallet) {
        String name = "package" + new Random().nextInt(1000);
        Integer score = new Random().nextInt(10);
        Integer version = new Random().nextInt(10);
        String instructions = "instructions" + new Random().nextInt(1000);
        String timestamp = Long.toString(System.currentTimeMillis());

        return new MetaPackage(creatorWallet, name, score, version, instructions, timestamp);
    }
}
