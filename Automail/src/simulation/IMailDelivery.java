// W05 Team 05 [Wed 05:15PM]
package simulation;

import automail.MailItem;

/**
 * a MailDelivery is used by the Robot to deliver mail once it has arrived at the correct location
 */
public interface IMailDelivery {

	/**
     * Delivers an item at its floor
     * @param mailItem the mail item being delivered.
     */
	void deliver(MailItem mailItem, double activityUnit, double deliveryCharge, double deliveryCost, double serviceFee); //New
    
}