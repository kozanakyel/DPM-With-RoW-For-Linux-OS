import java.util.Random;

public class MetaPackageUtil {
    public static MetaPackage createRandomMetaPackage(Wallet creatorWallet) {
        String name = "package" + new Random().nextInt(1000);
        Integer score = new Random().nextInt(10);
        Integer version = new Random().nextInt(10);
        String instructions = "instructions" + new Random().nextInt(1000);
        String timestamp = Long.toString(System.currentTimeMillis());
        Integer envStatus = new Random().nextInt(4);

        return new MetaPackage(creatorWallet, name, score, version, instructions, timestamp, envStatus);
    }
}
