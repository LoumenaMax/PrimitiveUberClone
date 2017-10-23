
/**
 * Class is responsible for all interactions between Wallets. Currently can transfer funds between two wallets.
 * @author max
 *
 */
public class Transaction {

	private Wallet spender;
	private Wallet reciever;
	private double amount;
	
	/** Constructs a Transaction class
	 * @param spender The Wallet that will pay the receiver
	 * @param reciever The Wallet that will receive payment from the spender
	 * @param amount The amount that will be transferred from spender to receiver
	 */
	public Transaction(Wallet spender, Wallet reciever, double amount) {
		this.spender = spender;
		this.reciever = reciever;
		this.amount = amount;
	}

	/** Returns the transaction amount
	 * @return The amount of money to be transferred from spender to receiver
	 */
	public double getAmount() {
		return amount;
	}
	
	/** Checks the spenders balance against the amount given.
	 * @param amount The amount to check the spenders balance against
	 * @return True if the spender has enough money to make such a purchase, false otherwise.
	 */
	public boolean checkSpenderFunds() {
		if(spender.getBalance() < amount) {
			return false;
		}
		return true;
	}
	
	/** Conducts the payment specified in the transaction
	 * @return Whether or not the payment was successful
	 */
	public boolean makePayment() {
		if(!checkSpenderFunds()) {
			return false;
		}
		spender.removeFunds(amount);
		reciever.addFunds(amount * Uber.DRIVER_SHARE);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("%.2f", amount);
	}
}
