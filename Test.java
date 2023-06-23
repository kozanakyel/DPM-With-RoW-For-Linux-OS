//import java.util.Random;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class Test {
    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {

        //Random rand = new Random(System.currentTimeMillis());

        // Creating peers.
        Peer.peers = new Peer[4];
        for (int i = 0; i < 4; i++) {
            Peer.peers[i] = new Peer();
            System.out.println("Get peer environment dependency status: " + Peer.peers[i].getEnvStatus());
        }

        // One of the peers creates the genesis block
        // Here, peers[0] is our Satoshi Nakamoto...
        // Already run automatically now when the bc is created.
        //Peer.peers[0].blockchain.createGenesisBlock();

        // Now, our blockchain is ready for new blocks being added
        // so all peers start sending transactions back and forth,
        // mining, etc.
        for (int i = 0; i < 4; i++) {
            Peer.peers[i].startConsumer();
            Peer.peers[i].startServer();
            Peer.peers[i].startPeerTransactions();
        }

        // Wait to make sure servers are properly opened
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


    }
}
