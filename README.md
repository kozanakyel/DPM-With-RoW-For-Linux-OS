### STANDALONE BLOCKCHAIN PROJECT

### CSE 424

# DISTRIBUTED PACKAGE MANAGER (DPM)

UĞUR AKYEL - 20190808020

HASAN ABASOV - 20160807006

MURAT BAŞKONUŞ - 20180808019

SİMGE HATİPOĞLU - 20170808022

What is the DPM?

A project is to develop a distributed package manager, known as DPM, which aims to revolutionize the package management landscape on various Linux systems. The conventional model of central repositories will be replaced by a decentralized approach that leverages a peer-to-peer system operating over a blockchain network.
The primary objective of DPM is to enable the efficient and secure distribution of packages among users. By leveraging the power of blockchain technology, DPM ensures transparency, immutability, and tamper-proof characteristics throughout the package management process. This innovative solution not only enhances the overall reliability but also mitigates the risks associated with centralized repositories, such as single points of failure and potential security vulnerabilities.
Within the DPM ecosystem, every user has the capability to create and share their own packages. When a user generates a new package, its creation is recorded on the blockchain, allowing other users to view its existence. To retrieve and install a particular package, any client within the network can utilize the InterPlanetary File System (IPFS) protocol, retrieving the package from the required IPFS node. This decentralized retrieval mechanism not only enhances the availability of packages but also fosters a collaborative environment where users can contribute and benefit from each other's creations.
To maintain the integrity and quality of packages within the DPM network, a dual role system is implemented. Peers can act as validators, responsible for verifying the authenticity and correctness of packages, or creators, who contribute new packages to the network. Remarkably, due to the inherent nature of blockchain networks, a peer can simultaneously serve as both a validator and a creator. This arrangement ensures a balanced distribution of responsibilities and facilitates a decentralized consensus mechanism.
The heart of the DPM ecosystem lies in the Reputation of Work (RoW) mechanism, which is designed to incentivize and reward peers based on their contributions to the network. When a peer validates or creates a package, they receive a real-time score that reflects the quality, reliability, and timeliness of their work. The RoW mechanism evaluates the difficulty and conditions of the package, ensuring that peers are appropriately rewarded based on the effort and value they bring to the ecosystem. In essence, RoW can be viewed as a customized adaptation of the renowned Proof of Work (PoW) consensus mechanism, tailored specifically for the DPM environment.
![Project Logic Flow](/assets/DPMstructure.png)
Fig.1 Basic our solution diagram
By combining the advantages of peer-to-peer networks, blockchain technology, and the RoW consensus mechanism, the DPM project aims to revolutionize package management for Linux systems. This ambitious endeavor holds the potential to empower users, foster collaboration, and enhance the security and reliability of package distribution across the ecosystem. With DPM, users can look forward to a decentralized, transparent, and efficient package management experience that truly embraces the spirit of open-source software.

Real Life Scenarios for Package Management

In Linux systems like Ubuntu and Arch, package management is typically handled through package managers that provide a convenient way to install, update, and remove software packages from the Central Package Repository. Here's an overview of how package management works in these systems:

- Ubuntu (APT Package Manager):
  Updating the Package Lists: The package lists, which contain information about available software packages and their versions, are regularly updated. This can be done using the apt-get update command.
  Installing Packages: To install a package, you can use the apt-get install command followed by the package name. For example, apt-get install firefox installs the Firefox web browser.
  Updating Packages: You can update installed packages to their latest versions using the apt-get upgrade command. It fetches the latest versions from the repositories and installs them.
  Removing Packages: To remove a package, you can use the apt-get remove command followed by the package name. For example, apt-get remove firefox removes the Firefox package from the system.
- Arch Linux (Pacman Package Manager):
  Updating the Package Database: The package database, which contains information about available packages, is updated by running pacman -Sy.
  Installing Packages: To install a package, you can use the pacman -S command followed by the package name. For example, pacman -S firefox installs the Firefox web browser.
  Updating Packages: To update all installed packages to their latest versions, you can run pacman -Syu. It synchronizes the package databases, fetches updated packages, and installs them.
  Removing Packages: To remove a package, you can use the pacman -R command followed by the package name. For example, pacman -R firefox removes the Firefox package from the system. Both Ubuntu and Arch Linux have package repositories where software packages are hosted and managed. These repositories contain a vast collection of software maintained by the distribution's community or official maintainers. The package managers handle dependency resolution, ensuring that all necessary dependencies for a package are installed.
  Additionally, both Ubuntu and Arch Linux have package management utilities that provide graphical user interfaces (GUIs) for managing packages. For Ubuntu, the Software Center or GNOME Software tool provides a GUI interface, while Arch Linux has tools like Pamac or Octopi.
  These examples provide a simplified overview of package management in Ubuntu and Arch Linux. Package management systems vary across different Linux distributions, but the general principles remain similar, aiming to provide a streamlined and efficient way to handle software installation, updates, and removal on Linux systems.
  3- In DPM is actually required with Blockchain?
  a- Do you need a shared, consistent data store?
  Yes, In a distributed package management system, multiple users create, share, and consume packages. It's crucial to have a central data store or a distributed database that acts as a source of truth, ensuring data consistency and eliminating conflicts or discrepancies between different users' views of the package repository. A shared consistent data store, the DPM project can establish a reliable foundation for package distribution, version control, and dependency management. It enables users to have a consistent and synchronized view of the package repository, facilitating collaboration, interoperability, and efficient package management across the distributed network.
  b- Does more than one entity need to contribute data?
  Yes, In the DPM (Distributed Package Manager) project, multiple entities can contribute data to the system. The collaborative nature of the project encourages and relies on contributions from various participants to enrich the package ecosystem:
  Package Creators: These entities are responsible for creating and publishing packages to the DPM network. They contribute data by providing the package files, metadata (such as package name, version, description, and dependencies), and potentially other related information like documentation, licensing details, or release notes.
  Package Validators: Validators play a crucial role in ensuring the integrity and quality of packages within the DPM network. They contribute data by reviewing and validating packages, verifying that they meet predefined criteria or standards. Validators may assess factors like package authenticity, security, compliance, and adherence to best practices.
  You can find this diagram explanation detailed version in our youtube video and presentation.
  ![Peer Transaction](/assets/startpeertransaction.png)
  Fig. Peer Transaction determination process
  c- Data records, once written, are never updated or deleted?
  While immutability is a common principle in DPM projects, it's important to note that there may be exceptions or cases where data updates or deletions are necessary. For instance, if a critical security vulnerability is discovered in a package, it may be necessary to "deprecate" or mark the package as obsolete. However, the original data record remains intact, and a new version or alternative solution is provided to address the issue.

d- Sensitive identifiers will not be written to data store?
In a DPM (Distributed Package Manager) project, it is generally recommended to avoid writing sensitive identifiers to the data store or shared repositories. Sensitive identifiers refer to personally identifiable information (PII) or any other sensitive data that could potentially compromise the privacy and security of individuals or entities. Here's why sensitive identifiers are typically excluded from the data store:
Privacy Protection: Excluding sensitive identifiers helps protect the privacy of users and contributors within the DPM network. By not storing personal information, such as names, addresses, or contact details, the risk of unauthorized access, identity theft, or misuse of sensitive data is minimized.
Security Considerations: Storing sensitive identifiers increases the risk of data breaches or unauthorized access, as this information could be attractive to malicious actors. By omitting such data from the data store, the overall security posture of the DPM project is strengthened.

e- Do you want a tamperproof log of all writes to the data store?
Yes, having a tamperproof log of all writes to the data store is generally desirable in a DPM (Distributed Package Manager) project. The log serves as an important audit trail that records every write operation performed on the data store, ensuring transparency, accountability, and data integrity. Here's why a tamperproof log is beneficial:
Data Integrity and Validation: A tamperproof log provides a verifiable record of all write operations, ensuring the integrity of the data store. It allows for the validation and verification of each transaction or write operation, making it easier to detect any unauthorized modifications or tampering attempts.
Auditability and Compliance: The tamperproof log serves as a valuable tool for auditing and compliance purposes. It enables the tracking of changes, facilitating investigations, and ensuring regulatory compliance. Auditors or administrators can review the log to identify any suspicious or unauthorized activities.
Aa a result we can say, The DPM project should have a Blockchain use case.

## Gossip Protocol Role

We investigate the gossip protocol structure. Because each time any peer could be validated a created package, or any peer could create a new package or upgrade a version. So we should broadcast for new created Metapackages or validated Metapackages. In our system this package has some dependencies like the virtual environments and this environment has same structure and dependencies otherwise package validation has failed for in blockchain. And peers don’t gain any reputation scores.
A gossip protocol mechanism can be a valuable addition to a DPM (Distributed Package Manager) project to facilitate communication and information dissemination among participants in a decentralized network. Here's an explanation of how a gossip protocol mechanism can work within the DPM project:
Peer-to-Peer Communication: In a gossip protocol mechanism, each participant in the DPM network acts as a peer and can communicate directly with other peers in the network. Peers exchange information in a decentralized manner without relying on a centralized authority.
Randomized Peer Selection: To initiate communication, a peer selects a few random peers from the network. This random peer selection helps spread information efficiently throughout the network and prevents information bottlenecks or biases.

Reputation of Work (RoW)

Each package will have a specific difficulty score for this consensus mechanism. The first person to validate and the creator will earn these scores together. Additionally, if a package has been validated and scored once before, it will be checked in previous blocks and transactions. The subsequent validator will receive the half score of the previous one, and it will be added to the creator again.

    		Fig. Calculate Reputation Score from package

After reaching a certain point, the score will drop below one, and no more points can be earned from the scores. However, validators can still be seen. Thus, it will be observed that the package runs in their own systems and under the same environmental conditions. With this incentivization and reward mechanism, the retrieval of packages specified in the central area on IPFS will be ensured, and people will gain influence on the system by validating these packages and earning scores. For this purpose, the GUI has designated validator and creator fields for each transaction. The implementation challenges and errors will be discussed again in the future.

Challenges

The provided template code is written entirely according to the Bitcoin mechanism and most things, including the database side, are hardcoded. When we try to implement changes in the program, we encounter many errors. Despite attempting a few approaches to overcome this, we still encountered errors while implementing the RoW mechanism, and some things in the program still don't work correctly. To understand the implemented program and address the errors, we read the book that the template was based on from start to finish. We shared our ideas and opinions about this topic in group meetings and discussed them with our instructor. We found that we had the same mindset. However, in order to execute and obtain a result on the GUI with the database, we decided to write a parallel app that is synchronized with the DPM extension, writing each class and its methods as if writing a separate project, aiming to understand how each class and component work. We were successful in this endeavor.

    				Fig Gui for DPM Blockchain

However, there were still some deficiencies in running the consensus mechanism at the desired level. To address this, we decided to incorporate the gossip protocol method in both the peer server and client, and to experiment with the ROW consensus mechanism using "mined by." In the video presentation, we will also discuss these actions taken and the challenges encountered. Currently, on the GUI, the wallets of the creator and validator can be seen, and the next created package and its difficulty level are visible in the system as the next transaction. When creating packages, we used the classes we created, namely "Metapackages" and "MetapackageGenerator." However, we are discussing whether the package should be signed with the peer's wallet immediately upon creation or when it is included in a transaction. This is because local changes can still be made if it hasn't been included in a transaction yet. Once the transaction passes through the block, the peer's signature is already acquired, and we still encounter a validation error at this point.

Implementation BlockchainFromScratch

We have started working on the last template you sent due to the errors in e-coin. During the implementation phase, we noticed that the code was more analyzable than we thought, and we implemented the process of determining the roles of peers, packet formation, and validation as we planned. To facilitate simulation, we randomly assigned packet and peer roles. Beyond comparing the dependencies in packet formation with the environment, we also proceeded with a certain probability during the packet installation and validation phase. If we could not validate the package, we added a penalty point, thereby setting up an incentive penalty mechanism.

When transactions in the mempool reached a certain size, we created a new block to complete the simulation. The problems we encountered here were forgetting to calculate the new hash value within the block constructor during block formation. After creating a new block, we provided a new hash value with calculate hash and placed it in the blockchain. Since we still cannot solve the given and received sigbytes error, we put the verify signature section in the comment and will try this process again after fixing this error. However, with the current operations, our simulation is working efficiently and as desired.
