import java.util.ArrayList;
import java.security.*;
import java.util.Objects;

public class MetaPackage {

    private final PublicKey creator;
    private Integer creatorId;
    private String name;
    private Integer score;
    private Integer version;
    private String instructions;   // package install and setup instructions
    private String timestamp;
    private Integer dependencyEnvStatus;   // because any peer have some different env and dependencies
    private ArrayList<String> validators;

    public MetaPackage(
            Wallet creatorWallet,
            String name, Integer score,
            Integer version,
            String instructions,
            String timestamp,
            Integer dependencyEnvStatus
    ) {
        this.creator = creatorWallet.publicKey;
        this.name = name;
        this.score = score;
        this.version = version;
        this.instructions = instructions;
        this.timestamp = timestamp;
        this.dependencyEnvStatus = dependencyEnvStatus;
        this.validators = new ArrayList<>();
        this.creatorId = creatorWallet.peer.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetaPackage metaPackage)) return false;
        return this.creator.equals(metaPackage.creator) &&
                this.name.equals(metaPackage.name) &&
                this.timestamp.equals(metaPackage.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creator, name, timestamp);
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getDependencyEnvStatus() {
        return dependencyEnvStatus;
    }

    public void setDependencyEnvStatus(Integer dependencyEnvStatus) {
        this.dependencyEnvStatus = dependencyEnvStatus;
    }

    public ArrayList<String> getValidators() {
        return validators;
    }

    public void setValidators(ArrayList<String> validators) {
        this.validators = validators;
    }

    @Override
    public String toString() {
        return "MetaPackage{" +
                "creator='" + creator + '\'' +
                ", score=" + score +
                ", version=" + version +
                ", instructions='" + instructions + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", validators=" + validators +
                '}';
    }
}
