import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

//We can think of transaction outputs as a check that can be turned into cash and spent later.
class TransactionOutput {
    public String id;           // if for referring
    public PublicKey recipient; // whom this TXO belongs to
    public int value;           // the value of the TXO
    public String parentTransactionId; // The tx in which this TXO is created

    public TransactionOutput(PublicKey recipient, int value, String parentTransactionId) throws NoSuchAlgorithmException {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = calculateHash();
    }

    //Calculates the hash by using recipient + value + parentTransactionId
    public String calculateHash() throws NoSuchAlgorithmException {
        // In fact, these three may not be unique!!! <- Problem to be solved...
        String dataToHash = "" + this.recipient + this.value + this.parentTransactionId;
        return StringUtil.hash(dataToHash);
    }

    // Finds out whether a TXO belongs to a particular public key
    // So checks whether the public key of the object (recipient)
    // is equal to the incoming parameter (publicKey) or not
    public boolean belongsTo(PublicKey publicKey) {
        return (publicKey.equals(recipient)); ////////////////////// Can be different objects, implement this properly!
    }
}