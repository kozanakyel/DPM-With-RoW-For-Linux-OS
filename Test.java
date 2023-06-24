import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class Test {
    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {

        // Creating peers.
        Peer.peers = new Peer[4];
        for (int i = 0; i < 4; i++) {
            Peer.peers[i] = new Peer();
            System.out.println("Get peer environment dependency status: " + Peer.peers[i].getEnvStatus());
        }

        // Now, our blockchain is ready for new blocks being added
        // so all peers start sending transactions back and forth, mining, etc.
        for (int i = 0; i < 4; i++) {
            Peer.peers[i].startConsumer();
            Peer.peers[i].startServer();
            Peer.peers[i].startPeerTransactions();
        }
    }
}
