import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.Base64;

public class Wallet {
    private PrivateKey privateKey;   // Private key associated with the wallet
    public PublicKey publicKey;     // Public key associated with the wallet
    public Peer peer;              // The id of the peer that has this wallet
    public HashMap<String, TransactionOutput> UTXOs; // Unspent txs available for this wallet to spend

    public Wallet(Peer p) {
        peer = p;
        generateKeyPair();
        UTXOs = new HashMap<String, TransactionOutput>(); // Only UTXOs owned by this wallet
    }


    // This method calculates the balance of this wallet while
    // also adding incoming UTXOs of this wallet to UTXO list 
    public int getBalance() {
        int total = 0;
        for (Map.Entry<String, TransactionOutput> item : peer.blockchain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.belongsTo(publicKey)) { // If output belongs to me (if coins belong to me)
                UTXOs.put(UTXO.id, UTXO); // Add it to our list of unspent transactions
                total += UTXO.value;
            }
        }
        return total;
    }

    // method to sign a message using the private key
    public byte[] sign(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(this.privateKey);
        signature.update(message.getBytes());
        return signature.sign();
    }


    // method to verify a message using the signature and the public key
    public boolean verify(PublicKey publicKey, String data, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature s = Signature.getInstance("SHA256withRSA");
        s.initVerify(publicKey);
        s.update(data.getBytes());
        return s.verify(signature);
    }


    // This method arranges Tx inputs and outputs in the Tx so that the amount will
    // be sent to the receiver and the change will be sent back to the sender
    public void arrangeFunds(PublicKey recipient, int value, List<TransactionInput> inputs, List<TransactionOutput> outputs, String txid) throws NoSuchAlgorithmException {


        // Find enough transaction inputs to afford the value
        int total = 0;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if (total >= value) break; // When we have enough of the UTXOs to afford value, stop
        }

        int creatorOver = total + value;  // because we use negative value with the transcaction so no need any extra extraction
        outputs.add(new TransactionOutput(recipient, value, txid)); //send value to recipient
        outputs.add(new TransactionOutput(publicKey, creatorOver, txid)); //send the left over 'change' back to sender

        // If a UTXO is spent with this tx, remove it from UTXOs list
        for (TransactionInput input : inputs) {
            UTXOs.remove(input.transactionOutputId);
        }
    }


    // This method arranges Tx outputs for a coinbase transaction
    // Just put a tx output and that's it.
    public void arrangeFunds(PublicKey recipient, int value, List<TransactionOutput> outputs, String txid) throws NoSuchAlgorithmException {
        outputs.add(new TransactionOutput(recipient, value, txid)); //send value to recipient
    }


    ///////////////////////////////// CRYPTOGRAPHIC OPERATIONS /////////////////////////////////

    // Generates a public-private RSA key pair for this wallet
    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            this.privateKey = pair.getPrivate();
            this.publicKey = pair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Return the private key of this wallet
    public String getPrivateKeyString() {
        return Base64.getEncoder().encodeToString(this.privateKey.getEncoded());
    }

    // Return the public key of this wallet
    public String getPublicKeyString() {
        return Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
    }


    // STATIC UTILITY FUNCTIONS REGARDING KEY-STRING TRANSFORMATIONS

    // A utility function to turn a private key into a string
    public static String getStringFromPrivateKey(PrivateKey pk) {
        return Base64.getEncoder().encodeToString(pk.getEncoded());
    }

    // A utility function to turn a public key into a string
    public static String getStringFromPublicKey(PublicKey pk) {
        return Base64.getEncoder().encodeToString(pk.getEncoded());
    }

    // A utility function to turn a string back into a private key
    public static PrivateKey getPrivateKeyFromString(String key) throws Exception {
        byte[] byteKey = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byteKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    // A utility function to turn a string back into a public key
    public static PublicKey getPublicKeyFromString(String key) throws Exception {
        byte[] byteKey = Base64.getDecoder().decode(key);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(byteKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
