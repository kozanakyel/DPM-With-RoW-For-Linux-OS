import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Peer {

    public static Peer[] peers; // In a real system, there must be a listing of peer ids to broadcast, here we keep all peers in this array
    public static Random rand;

    private final Integer envStatus;  // for peers linux env type
    
    public int id;
    private static int countPeers = 0; //to assign an automatic id

    //Every peer has its own copy of the blockchain
    //Possibly can occasionally become a little different from others
    //but must eventually become the same as all others
    //which is the whole idea of the blockchain
    public Blockchain blockchain;

    //Every peer has a wallet.
    //In fact, a peer can have multiple wallets
    //But for this application, to keep it simple, we implement only one
    protected Wallet wallet;      // This is the wallet of this peer that holds the keys and UTXOs to be used later

    private BlockingQueue<String> queue;    //To keep incoming messages
    
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public ArrayList<MetaPackage> createdMetaPackages;   // for keeping peers created packages
    public ArrayList<MetaPackage> validatedMetaPackages;    // for keeping peers validated meta packages
    
    // Creates a peer, assigns the next available id, initializes random generator
    // Initializes transactions list and starts the server socket
    public Peer() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        id = countPeers++;
        queue = new LinkedBlockingQueue<>();
        rand = new Random(System.currentTimeMillis());
        wallet = new Wallet(this); // Normally a wallet should be anonymous, or may not be, but we add this for debugging
        blockchain = new Blockchain(this);

        this.envStatus = new Random().nextInt(4);   // for peers env status
        this.createdMetaPackages = new ArrayList<MetaPackage>();
        this.validatedMetaPackages = new ArrayList<MetaPackage>();
        try {
            serverSocket = new ServerSocket(6000 + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String s) {
        for(int i=0; i<id; i++) {
            System.out.print(" ");
        }
        System.out.println("PEER "+id+": ");
        System.out.println(s);
    }

    public Integer getEnvStatus() {
        return envStatus;
    }

    public ArrayList<MetaPackage> getCreatedMetaPackages() {
        return createdMetaPackages;
    }

    public void addPackageCreated(MetaPackage metaPackage) {
        createdMetaPackages.add(metaPackage);
    }

    public ArrayList<MetaPackage> getValidatedMetaPackages() {
        return validatedMetaPackages;
    }

    public void addPackageValidated(MetaPackage metaPackage) {
        validatedMetaPackages.add(metaPackage);
    }

    /*  // Method to add a transaction to the mempool
    public void addToMempool(Transaction tx) {
        this.blockchain.mempool.add(tx);
    }

    // Method to get the mempool
    public ArrayList<Transaction> getMempool() {
        return this.blockchain.mempool;
    }
*/
    public String flipRandomValidatorCreator() {
        Integer rand = new Random().nextInt(3); // Generate 0, 1, or 2
        switch(rand) {
            case 0: return "creator";
            case 1: return "validator";
            default: return "waiting"; // Case 2 will return "waiting"
        }
    }

    /////////////////////////////////////////////////////////////////////////////////
    ///////////////////////// THE LIVING SIMULATION OF PEER /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    // This is a user simulation (it randomly initializes some transactions from time to time)
    public void startPeerTransactions() {
        new Thread(() -> {
            while (true) {
                int waitForNextTransactionTime = rand.nextInt(10000)+3000;
                try {
                    //Wait for some random duration
                    Thread.sleep(waitForNextTransactionTime);
                    // Make a random transaction
                    
                    // Randomly choose "someone else" to send some money.
                    int receiver = rand.nextInt(peers.length);
                    while(receiver==this.id) receiver = rand.nextInt(peers.length); // I said "someone else", not myself

                    int thisPeer = this.id;    // becuase each peer role decided this way.
                                                // actually we should choose package from repo or from broadcast
                    String selectedRole = flipRandomValidatorCreator();
//                    selectedRole = "creator";
                    if(selectedRole.equals("creator")){
                        // 1-create package
                        // 2-send package to repo
                        // 3-gossip protocol broadcast package creating
                        System.out.println("Starting Creating Package Process!");
                        MetaPackage newPackage = MetaPackageUtil.createRandomMetaPackage(this.wallet);
                        IPFSPackageCenter.addPackage(newPackage);
                        System.out.println(newPackage);
                        this.gossipPackageProtocolToAllPeers("Created Package ", newPackage);

                    }else if(selectedRole.equals("validator")){
                        // 1-get packages from IPS repo or gossip protocol broadcast
                        // 2-validate packages llok env status code
                        // 3-yeah i validate or not  look env dependecy
                        // 4-guve score write transactions
                        System.out.println("Starting Validating Package Process!");
                        if(IPFSPackageCenter.getSize() > 0){
                            MetaPackage gotPackage = IPFSPackageCenter.sendRandomPackage();
                            assert gotPackage != null;
                            // if the environment status is equals the we can validate package
                            if(Objects.equals(gotPackage.getDependencyEnvStatus(), this.getEnvStatus())) {
                                System.out.println("Validate process with env assign!!");
                                Transaction validatorTx = new Transaction(this.wallet.publicKey,
                                        Peer.peers[thisPeer].wallet.publicKey,
                                        gotPackage.getScore(),
                                        this);

                                Integer creatorId = gotPackage.getCreatorId();
                                Transaction creatorTx = new Transaction(this.wallet.publicKey,
                                        Peer.peers[creatorId].wallet.publicKey,
                                        gotPackage.getScore(),
                                        this);

                                this.gossipPackageProtocolToAllPeers(validatorTx.toString(), gotPackage);
                            }
                            System.out.println("Validator process finish!!!");
                        }else {
                            System.out.println("IPFS Package Center has no Package yet!!");
                        }

                    }else{
                        System.out.println("Peer" + thisPeer + " waiting for next timestep!!");
                    }

                    // Randomly choose an amount. Slightly more than balance is possible
                    // so that we can check whether frauds are caught or not.
                    int bal = this.wallet.getBalance();
//                    if(bal>0) {
//                        int amount = rand.nextInt((int)(bal*1.2));
//
//                        Transaction newTx = new Transaction(this.wallet.publicKey,
//                                                            Peer.peers[receiver].wallet.publicKey,
//                                                            amount,
//                                                            this); // If we send this, why also send pubkey?
//
//
//                        System.out.println("Peer " + this.id +" t:"+ newTx.timeStamp+ ": Transaction broadcasted -> ID:"+newTx.transactionId+" val:"+newTx.value);
//
//                        this.broadcastToAllPeers(newTx.toString());
//                    }
                    
                } catch (InterruptedException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }                
            }
        }).start();
    }


    

    /////////////////////////////////////////////////////////////////////////////////
    ///////////////////////// BLOCKCHAIN INTERACTION METHODS ////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    public Blockchain getBlockchain() { return blockchain; }

    public PublicKey getPublicKey() { return wallet.publicKey; }

    // While the message queue has new messages, reads them and processes them
    // Here, processing is putting it into the transaction list
    // Beware that this is not a trivial task, remember that the order of 
    // Transactions is very important. This may insert a transaction to a specific position in the list
    public void startConsumer() {
        new Thread(() -> {
            while (true) {
                try {
                    String message = queue.take(); // blocks if queue is empty
                    // Turn the string message back into a transaction


                    Transaction receivedTx = Transaction.fromString(message, this);

                    // Process the message
                    System.out.println("Peer " + this.id + ": Transaction received -> " + Wallet.getStringFromPublicKey(receivedTx.sender).substring(0,5)+" ==> Number of txs in my mempool: "+blockchain.mempool.size());
                    if(receivedTx.processTransaction())
                        blockchain.mempool.add(receivedTx); // For now, just add it to the end of transactions
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// COMMUNICATION METHODS /////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    //Starts the listening server
    public void startServer() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    new Thread(() -> receiveMessage(socket)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    
    // Connects to another peer.
    public void connectToPeer(Peer peer) {
        try {
            this.clientSocket = new Socket("localhost", 6000 + peer.id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // When a message arrives, reads it from the input stream reader and puts it into the queue
    private void receiveMessage(Socket socket) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while ((message = input.readLine()) != null) {
                queue.put(message);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Sends a message to a specific peer
    public void sendMessage(int peerid, String message) {
        this.connectToPeer(Peer.peers[peerid]);
        new Thread(() -> {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Broadcasts a message to all peers
    public void broadcastToAllPeers(String message) {
        for(int peerid=0; peerid<peers.length; peerid++){
            //if(peerid!=this.id) { // LET IT ALSO SEND TO ITSELF
                this.connectToPeer(Peer.peers[peerid]);
                new Thread(() -> {
                    try {
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        out.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            //}
        }
    }

    public void gossipPackageProtocolToAllPeers(String message, MetaPackage metaPackage) {
        // Gossip protocol for if creating a new packages, reached to all their peers
        // or if validating a new packages from any peer, we can broadcast to others
        for(int peerid=0; peerid<peers.length; peerid++){
            //if(peerid!=this.id) { // LET IT ALSO SEND TO ITSELF
            this.connectToPeer(Peer.peers[peerid]);
            new Thread(() -> {
                try {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println(message + " : "+ metaPackage.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            //}
        }
    }






    // Method to mine a block
    /*public void mineBlock() {
        // As an example, we'll just take all transactions from the mempool.
        // In reality, you'd want to choose transactions based on some criteria,
        // such as the transaction fee.

        // Also, this does not handle the case where a block's size is limited,
        // and the total size of all transactions in the mempool exceeds this limit.

        //We will implement Merkle tree later

        List<Transaction> transactionsToMine = new ArrayList<>(this.mempool);
        this.mempool.clear();

        // Add the transactions to a new block and add it to the blockchain
        Block newBlock = new Block(blockchain.getLastBlock().hash, transactionsToMine, this);
        this.blockchain.addBlock(newBlock);
    }*/

}
