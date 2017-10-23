
/** Responsible for holding funds for some other object.
 * @author max
 *
 */
public class Wallet {
	public double balance;
	
	/** Constructs a Wallet class
	 * @param balance The initial balance of the wallet.
	 */
	public Wallet(double balance) {
		this.balance = balance;
	}
	
	/** Method to retrieve the balance of the wallet
	 * @return The amount of money left in the wallet.
	 */
	public double getBalance() {
		return balance;
	}

	/** Removes the amount from your balance
	 * @param amount The amount removed from your balance
	 * @return The new balance of this object
	 */
	public double removeFunds(double amount) {
		balance -= amount;
		return balance;
	}

	/** Adds the amount given to your balance
	 * @param amount The amount added to your balance
	 * @return The new balance of this object
	 */
	public double addFunds(double amount) {
		balance += amount;
		return balance;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("%.2f", balance);
	}
}
