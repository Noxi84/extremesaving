package extremesaving.service;

import java.math.BigDecimal;
import java.util.Date;

public interface DataService {

    Date getLastItemAdded();

    long getTotalItems();

    BigDecimal getTotalBalance();
}