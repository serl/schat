package schat;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;

import schat.events.EventListener;
import schat.tcp.Client;

public class SecureConnection implements EventListener<Object> {
	private static BigInteger g = new BigInteger("174319183213905702636270321421216113500231442153160709387027834005458019732242650423616594214969836304872686437724860099009946937641131201812728131563641245661796471280956538838149548188633995553708337415446741482564828796031537988133558498869822267855698020876476375559508973971514979352841098179217961880239");
	private static BigInteger p = new BigInteger("95386007304040349046976043285146785281461853087052942645381071462590046356903847367487755104368733390923522884525704303451364068704244953787464457490459507181663216927383690194423865950234180081757971207177754971974214404307851739691334336804452544946155644844536965893616871189143710525527342973018981621063");

	protected final Peer peer;
	protected final Client client;
	protected KeyAgreement keyAgreement;
	protected byte[] secret;

	protected static KeyPairGenerator GetKeyPairGenerator()
	{
		try {
			DHParameterSpec dhParams = new DHParameterSpec(p, g);
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
			keyGen.initialize(dhParams, new SecureRandom());
			return keyGen;
		}
		catch (Exception e) {
			System.err.println("Unable to initialize security, missing provider");
			System.exit(1);
			return null;
		}
	}

	public SecureConnection(Client c, Peer p) {
		client = c;
		peer = p;
		client.getDataEvent().addListener(this);

		if (c.isInitiator()) {
			try {
				KeyPair kp = GetKeyPairGenerator().generateKeyPair();
				client.send(kp.getPublic());
				//System.out.println("I'm Alice, I just sent my public key");

				keyAgreement = KeyAgreement.getInstance("DH");
				keyAgreement.init(kp.getPrivate());
			}
			catch (Exception e) {
				System.err.println("Unable to initialize security, connection aborted");
				e.printStackTrace();
				client.abort();
			}
		}
	}

	@Override
	public void eventHandler(Object obj) {
		if (secret == null && obj instanceof PublicKey) {
			try {
				PublicKey otherPublicKey = (PublicKey)obj;
				if (keyAgreement == null) {
					KeyPair kp = GetKeyPairGenerator().generateKeyPair();
					client.send(kp.getPublic());
					//System.out.println("I'm Bob, I just sent my public key");

					keyAgreement = KeyAgreement.getInstance("DH");
					keyAgreement.init(kp.getPrivate());
				}
				keyAgreement.doPhase(otherPublicKey, true);
				secret = keyAgreement.generateSecret();
								
			    peer.log("Exchanged a "+secret.length+"b key");
			}
			catch (Exception e) {
				System.err.println("Unable to initialize security, connection aborted");
				e.printStackTrace();
				client.abort();
			}
		}

		if (obj instanceof String)
			System.out.println("Received plaintext: "+obj);
	}

	public void send(String s) {
		try { client.send(s); }
		catch (IOException e) { }
	}

}
