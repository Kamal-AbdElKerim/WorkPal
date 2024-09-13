package interfaces;
import java.util.HashMap;

import interfaces.Allclass.Subscription;
public interface SubscriptionsInterface {
    void addSubscription(Subscription subscription) throws Exception;
    void updateSubscription(Subscription subscription) throws Exception;
    void deleteSubscription(int subscriptionId) throws Exception;
    Subscription getSubscriptionById(int subscriptionId) throws Exception;
    HashMap<Integer, Subscription> getAllSubscriptions() throws Exception;
    void displaySubscriptions(int userId) throws Exception;
    HashMap<Integer, Subscription> getSubscriptionsBySpaceId(int spaceId) throws Exception;
}
