package extremesaving.calculation.facade;

import extremesaving.calculation.dto.AccountDto;

import java.util.List;

public interface AccountService {

    List<AccountDto> getAccounts();
}