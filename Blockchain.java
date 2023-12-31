import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Blockchain {

    public Peer peer; //Referring to the maintainer of this copy of the blockchain
    public ArrayList<Block> blocks; //Blocks in the blockchain
    public ArrayList<Transaction> mempool; //All the txs arrived. https://www.btcturk.com/bilgi-platformu/bitcoin-mempool-nedir/
    public HashMap<String, TransactionOutput> UTXOs; //Unspent tx outputs
    public int difficulty = 5;    //Current difficulty of the blockchain (later can be changed as in btc)
    public int MININGREWARD = 2 ^ 10; // Mining reward will be 1024 at the beginning
    public final int HALVINGPERIOD = 10; // After every 10 blocks added, mining reward will be halved
    //Question: How many coins are we going to get eventually then?


    public Blockchain(Peer p) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        peer = p;
        blocks = new ArrayList<Block>();
        mempool = new ArrayList<>();
        UTXOs = new HashMap<String, TransactionOutput>();
        createGenesisBlock();
    }

    // Add transactions to the blockchain
    public void addBlock(Block newBlock) {
        newBlock.mineBlock();
        blocks.add(newBlock);
        if (blocks.size() % HALVINGPERIOD == 0) MININGREWARD /= 2;
    }

    public Block getLastBlock() {
        return blocks.get(blocks.size() - 1);
    }

    public void createGenesisBlock() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        // Create a coinbase transaction for the block reward. For simplicity, we'll
        // pretend this peer's wallet address is the recipient.
        // Since this is a simulation, we can use this peer's public key.
        // In reality, this would be a more complex transaction.
        Transaction coinbase = new Transaction(this.peer.wallet.publicKey, this.MININGREWARD, this.peer);

        // Manually set a transaction ID
        coinbase.transactionId = "coinbase";

        // Create a list of transactions and add the coinbase transaction
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(coinbase);

        //this.blockchain = new Blockchain(this);

        // Create the genesis block. We'll set previousHash as "0" because there's no previous block.
        Block genesisBlock = new Block(this.peer.wallet.publicKey, this);


        // Add it to the blockchain
        blocks.add(genesisBlock);
        // Add the output transaction to UTXOs
        TransactionOutput output = new TransactionOutput(coinbase.recipient, coinbase.value, coinbase.transactionId);
        this.UTXOs.put(output.id, output);
        System.out.println("Peer" + this.peer.id + "Genesis block created.");
    }

    // Check if the blockchain is valid
    public Boolean isChainValid() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.putAll(UTXOs);

        // Loop through blockchain to check hashes:
        for (int i = 1; i < blocks.size(); i++) {
            currentBlock = blocks.get(i);
            previousBlock = blocks.get(i - 1);
            // Compare registered hash and calculated hash:
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("#Current Hashes not equal");
                return false;
            }
            // Compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            // Check if hash is solved
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            // Loop through blockchains transactions:
            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on transaction(" + t + ")");
                    return false;
                }

                for (TransactionInput input : currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if (tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if (input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for (TransactionOutput output : currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if (currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
                    System.out.println("#Transaction(" + t + ") output recipient is not who it should be");
                    return false;
                }
                if (currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }
            }
        }
        System.out.println("Blockchain is valid");
        return true;
    }
}
